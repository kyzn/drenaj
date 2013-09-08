''' 
 Profiles Crawler

''' 

# Change History :
# Date                                          Prog    Note
# Sat Sep 07 16:16:55 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr


from twython import Twython, TwythonError
import time
import requests
import json

from config import *
from drnj_time import *

import argparse

from tornado.escape import json_decode,json_encode

environment = DIRENAJ_APP_ENVIRONMENT
app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[environment])

# TODO: Direnaj Login 
# Create DirenjUser and Password the first time

# These mongodb indices must be defined
# > db.queue.ensureIndex({id: 1}, {unique: true})
# > db.graph.ensureIndex({id: 1})
# > db.profiles.ensureIndex({id: 1}, {unique: true})
# > db.users.ensureIndex({id: 1})

# also possible but not needed:
# > db.queue.ensureIndex({id_str: 1}, {unique: true})

#> db.graph.group({key : {id: 1}, cond: {id : {$gt: 221455715}}, reduce: function(curr, result) {result.total += 1}, initial: {total:0}}

# For some reason, the following cannot be obtained from config.py
auth_user_id = 'direnaj'
auth_password = 'tamtam'

def drnj_profiles_crawler(ids):
    twitter = Twython(consumer_key, consumer_secret, access_token_key, access_token_secret)

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
    parser.add_argument('-u', '--user_id', help='user id', type=int, default=0)
    args = parser.parse_args()

    root = int(args.user_id)
#    if fof is None:
#        fof = "followers"
#        #fof = "friends"

    if root==0:
        # Get from scheduler
        get_response = requests.get(url=app_root_url+'/scheduler/suggestUseridToGet_profiles')
        root = json_decode(get_response.content)
        if root==0:
            ids = ['505670972', '745174243', '461494325', '636874348']
            #root = 50354388; # koray
            #root = 461494325; # Taylan
            #root = 505670972; # Cem Say
            #root = 483121138; # meltem
            #root = 230412751; # Cengiz
            #root = 636874348; # Pinar Selek
            #root = 382081201; # Tolga Tuzun
            #root = 745174243; # Sarp Maden
        else:
            ids = root
    else:
        ids = [str(root)]
    
    drnj_profiles_crawler(ids)
