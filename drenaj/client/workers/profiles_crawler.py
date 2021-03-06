'''
 Profiles Crawler

'''

# Change History :
# Date                                          Prog    Note
# Sat Sep 07 16:16:55 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr


import argparse

from twython import Twython, TwythonError
import requests
from tornado.escape import json_decode,json_encode

from drenaj.client.config.config import *
from drenaj.utils.drnj_time import *


environment = DRENAJ_APP_ENVIRONMENT
app_root_url = 'http://' + DRENAJ_APP_HOST + ':' + str(DRENAJ_APP_PORT[environment])

# TODO: Direnaj Login
# Create DirenjUser and Password the first time

# These mongodb indices must be defined
# > db.queue.ensureIndex({id: 1}, {unique: true})
# > db.graph.ensureIndex({id: 1})
# THESE TWO collections no more exist. -onurgu
# > db.profiles.ensureIndex({id: 1}, {unique: true})
# > db.users.ensureIndex({id: 1})

# also possible but not needed:
# > db.queue.ensureIndex({id_str: 1}, {unique: true})

#> db.graph.group({key : {id: 1}, cond: {id : {$gt: 221455715}}, reduce: function(curr, result) {result.total += 1}, initial: {total:0}}

# For some reason, the following cannot be obtained from config.py
auth_user_id = 'drenaj'
auth_password = 'tamtam'

def drnj_profiles_crawler(ids):
    keystore = KeyStore()
    twitter = Twython(keystore.app_consumer_key, keystore.app_consumer_secret, keystore.access_tokens[0][0], keystore.access_tokens[0][1])

    if len(ids)>100:
        print "Can't retrieve more than 100 profiles"
        return

    # Number of calls to the twitter API
    remain = 0

    # True if data is fetched correctly
    success = True

    # Seconds to wait before trying again twitter limit
    wait = 60;

    print "Retrieving the profiles of %d users\n" % len(ids)
    # Check if we still have some bandwidth available
    while remain<=0:
        v = twitter.get('application/rate_limit_status', {"resources": 'users'})
        remain = v["resources"]['users']["/users/lookup"]["remaining"]
        if remain>0:
            break
        print "Waiting... Twitter API rate limit reached\n"
        time.sleep(wait)

    # First, try to get the profiles
    try:
        S = twitter.get('users/lookup', {'user_id': ids,'include_entities': True})

        print '{} id''s queried, {} retrieved '.format(len(ids), len(S))

        post_data = {"user_id": json_encode(ids), "v": json_encode(S), "auth_user_id": auth_user_id, "auth_password": auth_password}
        post_response = requests.post(url=app_root_url + '/profiles/store', data=post_data)
        print "%s" % post_response.content


    except TwythonError as e:
        print e
        print "Error while fetching user profile from twitter, quitting ..."
        return



if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Direnaj Profiles Crawler')
    parser.add_argument('-u', '--user_id', help='user id', type=str, default="[]")
    parser.add_argument('-N', '--iteration', help='number of requests of 100 users each', type=int, default=1)
    args = parser.parse_args()

    root = eval(args.user_id)
    N = int(args.iteration)

    if len(root)==0:
        for i in range(N):
            # Get from scheduler
            get_response = requests.get(url=app_root_url+'/scheduler/suggestUseridToGet_profiles')
            ids = json_decode(get_response.content)
            # ids = ['505670972', '745174243', '461494325', '636874348']
            #root = 50354388; # koray
            #root = 461494325; # Taylan
            #root = 505670972; # Cem Say
            #root = 483121138; # meltem
            #root = 230412751; # Cengiz
            #root = 636874348; # Pinar Selek
            #root = 382081201; # Tolga Tuzun
            #root = 745174243; # Sarp Maden
            drnj_profiles_crawler(ids)

    else:
        ids = [str(x) for x in root]
        drnj_profiles_crawler(ids)

