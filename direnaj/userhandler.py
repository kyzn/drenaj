# UserHandlers:  Code for retrieving and showing Profiles
#
# Date			Time		Prog	Note
# 31-Aug-2013	 2:18 AM	ATC		

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr 

from config import *
import drnj_time

from direnajmongomanager import *
from drnj_time import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

import json
import time

import bson.json_util

class UserSingleProfileHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """ I chose to handle all options at once, using only POST requests
        for API requests. GET requests will be used for browser examination.
        """

        store_or_view = args[0]

        print 'Enter: UserSingleProfileHandler. Command:', store_or_view

        if ((store_or_view != None and store_or_view == 'view') or (store_or_view == None)):
            try:
                user_id = self.get_argument('user_id')
                print user_id
                print "View Not implemented yet..."
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                user_id = int(self.get_argument('user_id'))
                #v = self.get_argument('v', None)
                json_data = self.get_argument('v', None)
                
                # print json_data
                v = json.loads(json_data)

                
                #TODO: drnjID obtained from crawler authentication
                ret = store_single_profile(user_id, v, drnjID= 'dummy_for_now')
                
                # Returns number of written edges (new relations discovered)
                print ret

                self.write(json_encode(ret))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass

def store_single_profile(user_id, v, drnjID):
    """ 
        
    """
    print "Received recent profile of ", v['name'], ' a.k.a. ', v['screen_name']
    print 'Not implemented yet!'
    print 'TODO: Save the profile to profiles, users and queue collections'
    
    db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
    queue_collection = db['queue']
    profiles_collection = db['profiles']
    users_collection = db['users']
    
    profile_dat = {
    "id": v['id'],
    "id_str": v['id_str'],
    "created_at": py_utc_time2drnj_time(v['created_at']),
    "record_retrieved_at": now_in_drnj_time(),
    "retrieved_by": drnjID
    }
    
    
    users_dat = {
    "id": v['id'],
    "id_str": v['id_str'],
    "protected": v['protected'],
    "location": v['location'],
    "screen_name": v['screen_name'],
    "name": v['name'],
    "followers_count": v['followers_count'],
    "friends_count": v['friends_count'],
    "statuses_count": v['statuses_count'],
    "geo_enabled": v['geo_enabled'],
    "profile_image_url": v['profile_image_url'],
    "record_retrieved_at": now_in_drnj_time(),
    "retrieved_by": drnjID
    }
    
    print profile_dat
    print users_dat
    
    # Check Queue 
    queue_query = {"id": user_id}
    id_exists = queue_collection.find(queue_query).count() > 0
    dt = drnj_time.now_in_drnj_time()

    if id_exists:
        queue_document = {"$set":
                            {
                            "profile_retrieved_at": dt,
                            "retrieved_by": drnjID}
                           }
        # creates entry if query does not exist
        queue_collection.update(queue_query, queue_document, upsert=True)
    else:
        queue_document ={
                            "id": user_id,
                            "id_str": str(user_id),
                            "profile_retrieved_at": dt,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID
                        }

    # Insert to profiles 
    profiles_query = {"id": user_id}
    id_exists = profiles_collection.find(profiles_query).count() > 0
    if not id_exists:
        profiles_collection.insert(profile_dat)
    
    # Insert to users 
    users_collection.insert(users_dat)

    return 0