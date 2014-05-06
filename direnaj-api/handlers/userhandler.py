# UserHandlers:  Code for retrieving, storing and showing Profiles
#
# Date			Time		Prog	Note
# 31-Aug-2013	 2:18 AM	ATC

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

# TODO: Having separate code for a single and multiple profiles is not necessary
# store_*_profile functions can be merged

from config.config import *

import utils.direnajmongomanager as direnajmongomanager
import utils.drnj_time as drnj_time
from utils.drnj_time import *
from utils.direnaj_collection_templates import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

import json
import time

from schedulerMainHandler import markProtected

import bson.json_util

app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])

MAX_LIM_TO_VIEW_PROFILES = 10000

#  route: (r"/profiles/(store|view)", UserProfilesHandler),
class UserProfilesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        Note: OG: I chose to handle all options at once, using only POST requests
        for API requests. GET requests will be used for browser examination.

        """

        store_or_view = args[0]

        print 'UserProfilesHandler. Command:', store_or_view

        if ((store_or_view is not None and store_or_view == 'view') or (store_or_view is None)):
            try:
                user_id = self.get_argument('user_id', None)
                auth_user_id = self.get_argument('auth_user_id', 'direnaj')
                lim_count = self.get_argument('limit', None)
                print user_id

                if lim_count is None:
                    lim_count = 20
                else:
                    lim_count = int(lim_count)

                if lim_count > MAX_LIM_TO_VIEW_PROFILES:
                    lim_count = MAX_LIM_TO_VIEW_PROFILES

                # if no user_id is supplied.
                if user_id is None:
                    # running the query
                    cursor = direnajmongomanager.tweets_coll.find().sort('record_retrieved_at', -1).limit(lim_count)
                else:
                    cursor = direnajmongomanager.tweets_coll.find({'tweet.user.id_str': user_id}).sort('record_retrieved_at', -1).limit(lim_count)

                #tmp = [x for x in cursor]
                tmp = []
                for record in cursor:
                    user = record['tweet']['user']
                    user['record_retrieved_at'] = record['record_retrieved_at']
                    #id_str = user['id_str']
                    #user['known_followers_count'] = graph_coll.find({'friend_id_str': id_str}).count();
                    #user['known_friends_count'] = graph_coll.find({'id_str': id_str}).count();
                    id = user['id']
                    user['known_followers_count'] = \
                        direnajmongomanager.graph_coll.find({'friend_id': id}).count()
                    user['known_friends_count'] = \
                        direnajmongomanager.graph_coll.find({'id': id}).count()
                    tmp.append(user)

                result = bson.json_util.dumps(tmp)
                self.write(result)
###                 else:
###                     # TODO: View the specified user ID profiles
###                     result = json_encode({'message': 'Not implemented yet, View the specified user ID profiles'})
###                     self.write(result)

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                json_user_id = self.get_argument('user_id')
                ids = json.loads(json_user_id)
                auth_user_id = self.get_argument('auth_user_id')
                campaign_id = self.get_argument('campaign_id', 'default')
                json_data = self.get_argument('v', None)
                S = bson.json_util.loads(json_data)

                nids = store_multiple_profiles(ids, S, drnjID=auth_user_id, campaign_id=campaign_id)

                if len(nids) > 0:

                    for i in range(len(nids)):
                        markProtected(nids[i], True, auth_user_id)
                        print "User not Found, Removing from queue: ",
                        print nids[i]

                # Returns profile ids that could not be retrieved
                print nids
                self.write(bson.json_util.dumps(nids))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)


## #     (r"/user/(store|view)", UserSingleProfileHandler),
## class UserSingleProfileHandler(tornado.web.RequestHandler):
##     def get(self, *args):
##         self.post(*args)
##         #self.write("not implemented yet")
##
##     def post(self, *args):
##         """ I chose to handle all options at once, using only POST requests
##         for API requests. GET requests will be used for browser examination.
##         """
##
##         store_or_view = args[0]
##
##         print 'UserSingleProfileHandler. Command:', store_or_view
##
##         if ((store_or_view != None and store_or_view == 'view') or (store_or_view == None)):
##             try:
##                 user_id = self.get_argument('user_id', None)
##                 auth_user_id = self.get_argument('auth_user_id','direnaj')
##                 lim_count = self.get_argument('limit', None)
##                 print user_id
##                 graph_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['graph']
##                 profiles_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['profiles']
##                 profiles_history_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['profiles_history']
##
##                 # if no user_id is supplied.
##                 if user_id is None:
##                     result = json_encode({'error': 'user_id not specified'})
##                     self.write(result)
##                 else:
##                     # running the query
##                     cursor = profiles_coll.find({
##                         'id_str': str(user_id),
##                     })
##
##                     tmp = []
##                     for x in cursor:
##                         x['ctime'] = time.ctime(drnj_time2py_time(x['record_retrieved_at']))
##                         x['created_at_ctime'] = time.ctime(drnj_time2py_time(x['created_at']))
##                         tmp.append(x)
##
##                     cursor = profiles_history_coll.find({
##                         'id_str': user_id,
##                     }).sort('record_retrieved_at', -1)
##
##                     for x in cursor:
##                         x['ctime'] = time.ctime(drnj_time2py_time(x['record_retrieved_at']))
##                         x['created_at_ctime'] = time.ctime(drnj_time2py_time(x['created_at']))
##                         tmp.append(x)
##
## #                    self.write(json_encode([tmp]))
## #                    self.write(bson.json_util.dumps({'result': tmp}))
##                     self.write(bson.json_util.dumps(tmp))
##                     self.add_header('Content-Type', 'application/json')
##             except MissingArgumentError as e:
##                 # TODO: implement logging.
##                 raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
##         elif (store_or_view == 'store'):
##             try:
##                 user_id = int(self.get_argument('user_id'))
##                 auth_user_id = self.get_argument('auth_user_id')
##                 #v = self.get_argument('v', None)
##                 json_data = self.get_argument('v', None)
##                 v = json.loads(json_data)
##
##                 ret = store_multiple_profiles([user_id], [v], drnjID=auth_user_id)
##
##                 # Returns true when a new profile is discovered
##                 print (len(ret) == 0)
##                 self.write(bson.json_util.dumps(len(ret) == 0))
##
##             except MissingArgumentError as e:
##                 # TODO: implement logging.
##                 raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
##             pass


def store_multiple_profiles(ids, S, drnjID, campaign_id):
    """

    """
    # print "Received recent profile of ", v['name'], ' a.k.a. ', v['screen_name']


    print S
    for i in range(len(S)):
        status = None
        if 'status' in S[i]:
            status = S[i]['status']
            del S[i]['status']
        else:
            status = {}
            status['text'] = None

        status['user'] = S[i]
        status['user']['history'] = False

        tweet_dat = validate_document(new_tweet_template(), {
            "tweet": status,
            # TODO: Replace this DB_TEST_VERSION with source code
            # version later
            "direnaj_service_version": DB_TEST_VERSION,
            "campaign_id": campaign_id,
            "record_retrieved_at": drnj_time.now_in_drnj_time(),
            "retrieved_by": drnjID,
        }, fail=False)

        print tweet_dat

        user_id = S[i]['id_str']

        # print profile_dat

        # Check Queue
        now = drnj_time.now_in_drnj_time()
        queue_query = {"id": user_id}
        queue_document = validate_document(new_queue_document(), {
            "id": int(user_id),
            "id_str": user_id,
            "profile_retrieved_at": now,
            "$setOnInsert": {
                "friends_retrieved_at": 0,
                "followers_retrieved_at": 0,
            },
            "retrieved_by": drnjID
        })

        # creates entry if query does not exist
        direnajmongomanager.queue_coll.update(queue_query, queue_document, upsert=True)

        # Insert to profiles
##         profiles_query = {"profile.id": user_id}
##         prof = profiles_collection.find_and_modify(profiles_query, remove=True)
##         if prof is not None:
##             profiles_history_collection.insert(prof)
##
##         profiles_collection.insert(profile_dat)

        # this call marks the current entries as history
        # maybe we won't need this for certain queries
        direnajmongomanager.move_to_history(user_id)

        direnajmongomanager.insert_tweet(tweet_dat)
#        tweets_collection.insert(tweet_dat)

        ids.remove(int(user_id))

    return ids


## def store_single_profile(user_id, v, drnjID):
##     """
##
##     """
##     print "Received recent profile of ", v['name'], ' a.k.a. ', v['screen_name']
##
##     db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
##     queue_collection = db['queue']
##     profiles_collection = db['profiles']
##     profiles_history_collection = db['profiles_history']
##
##     profile_dat = validate_document(new_profile_document(), {
##         "profile": v,
##        # {
##        #     "id": v['id'],
##        #     "id_str": v['id_str'],
##        #     "created_at": py_utc_time2drnj_time(v['created_at']),
##        #     "protected": v['protected'],
##        #     "location": v['location'],
##        #     "screen_name": v['screen_name'],
##        #     "name": v['name'],
##        #     "followers_count": v['followers_count'],
##        #     "friends_count": v['friends_count'],
##        #     "statuses_count": v['statuses_count'],
##        #     "geo_enabled": v['geo_enabled'],
##        #     "profile_image_url": v['profile_image_url']
##        # },
##         "record_retrieved_at": now_in_drnj_time(),
##         "retrieved_by": drnjID
##     })
##
##     # print profile_dat
##
##     # Check Queue
##     queue_query = {"id": user_id}
##     id_exists = queue_collection.find(queue_query).count() > 0
##     now = drnj_time.now_in_drnj_time()
##
##     if id_exists:
##         queue_document = {"$set":
##                             {
##                             "profile_retrieved_at": now,
##                             "retrieved_by": drnjID}
##                            }
##         # creates entry if query does not exist
##         queue_collection.update(queue_query, queue_document)
##     else:
##         queue_document = validate_document(new_queue_document(),{
##                             "id": user_id,
##                             "id_str": str(user_id),
##                             "profile_retrieved_at": now,
##                             "friends_retrieved_at": 0,
##                             "followers_retrieved_at": 0,
##                             "retrieved_by": drnjID
##                         })
##
##     # Insert to profiles
##
##     profiles_query = {"profile.id": user_id}
##     prof = profiles_collection.find_and_modify(profiles_query, remove=True)
##     if prof is not None:
##         profiles_history_collection.insert(prof)
##
##     profiles_collection.insert(profile_dat)
##
##     return id_exists
##
