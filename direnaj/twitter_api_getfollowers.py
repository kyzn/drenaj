# A Basic Crawler that retrieves all followers of a user and sends the data to the Service Layer
#
# 12-Aug-2013	12:33 AM	ATC		Developed using Python 2.7

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

from tornado.escape import json_encode

environment = DIRENAJ_APP_ENVIRONMENT
app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[environment])

def main(fof, root):
    twitter = Twython(consumer_key, consumer_secret, access_token_key, access_token_secret)

    cur = -1L
    
    # The friends/followers IDS to be retrieved will be stored here
    IDS = list()
    #SS = list()

    # Number of calls to the twitter API
    remain = 0
    
    # True if data is fetched correctly
    success = True
    
    # Seconds to wait before trying again twitter limit
    wait = 60;

    print "Retrieving the recent profile of user %d\n" % root
    
    # First, try to get the recent profile settings of the user
    try:
        v = twitter.get('users/show', {"user_id": root})
        print v['screen_name'], v['name']

        post_response = requests.post(url=app_root_url + '/user/store', data={"user_id": root, "v": json_encode(v)})
#       post_response = requests.post(url=app_root_url + '/user/store', data={"user_id": root, "v": v})
        
        
    except TwythonError as e:
        print e
        print "Error while fetching user profile from twitter, quitting ..."
        return 
    
    if v['protected']:
        post_response = requests.post(url=app_root_url+'/scheduler/reportProtectedUserid', data={"user_id": root, "isProtected": 1})
        print "Reported User as having a Protected Account %d" % root
        
    else:
        print "Retrieving %s of user %d\n" % (fof, root)

        while 1:
            # Check if we still have some bandwidth available
            while remain<=0:
                v = twitter.get('application/rate_limit_status', {"resources": fof})
                remain = v["resources"][fof]["/" + fof + "/ids"]["remaining"]
                if remain>0:
                    break
                print "Waiting... Twitter API rate limit reached\n"
                time.sleep(wait)

            try:
                S = twitter.get(fof + '/ids', {'user_id': root, 'cursor': cur})

                # We count the number of remaining requests to the Twitter API
                remain = remain - 1;

                IDS = IDS + S["ids"]
#               SS = SS.append(S)

                print "Total number of %s ID's retrieved so far: %d" % (fof, len(IDS))

                cur = S["next_cursor"]
                if cur==0:
                    break
            except TwythonError as e:
                print e
                success = False
                print "Error while fetching data from Twitter API"
                break

        if success:
            post_response = requests.post(url=app_root_url + '/' + fof + '/ids/store', data={"user_id": root, "ids": json_encode(IDS)})
            print "%s" % post_response.content
        else:
            post_response = requests.post(url=app_root_url+'/scheduler/reportProtectedUserid', data={"user_id": root, "isProtected": 1})
            print "Reported User as having a Protected Account %d" % root


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Direnaj Friends/Followers Crawler')
    parser.add_argument('-f', '--fof', choices=["friends", "followers"], help='("friends"|"followers")', default="followers")
    parser.add_argument('-u', '--user_id', help='the user id', type=int, default=0)
    args = parser.parse_args()

    fof = args.fof
    root = int(args.user_id)
#    if fof is None:
#        fof = "followers"
#        #fof = "friends"

    if root==0:
        # Get from scheduler


        get_response = requests.get(url=app_root_url+'/scheduler/suggestUseridToGet_' + fof)
        root = int(get_response.content)
        if root==0:
            #root = 50354388; # koray
            root = 461494325; # Taylan
            #root = 505670972; # Cem Say
            #root = 483121138; # meltem
            #root = 230412751; # Cengiz
            #root = 636874348; # Pinar Selek
            #root = 382081201; # Tolga Tuzun
            #root = 745174243; # Sarp Maden
            
        
    main(fof, root)

