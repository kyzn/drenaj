# UserHandlers:  Code for retrieving, storing and showing Profiles
#
# Date			Time		Prog	Note
# 31-Aug-2013	 2:18 AM	ATC

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

# TODO: Having separate code for a single and multiple profiles is not necessary
# store_*_profile functions can be merged

import json

import tornado.ioloop
import tornado.web
from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado import gen
from tornado.gen import Return

import bson.json_util

#from drenaj_api.config.config import *

import utils.drnj_time as drnj_time
from utils.drnj_time import *
from drenaj_api.utils.drenaj_collection_templates import *
from schedulerMainHandler import markProtected

MAX_LIM_TO_VIEW_PROFILES = 10000

#  route: (r"/profiles/(store|view)", UserProfilesHandler),
class UserProfilesHandler(tornado.web.RequestHandler):


    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        self.set_header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
        self.set_header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept')

    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    @tornado.web.asynchronous
    @gen.coroutine
    def post(self, *args):
        """

        Note: OG: I chose to handle all options at once, using only POST requests
        for API requests. GET requests will be used for browser examination.

        """

        motor_column = self.application.db.motor_column

        store_or_view = args[0]

        print 'UserProfilesHandler. Command:', store_or_view

        if ((store_or_view is not None and store_or_view == 'view') or (store_or_view is None)):
            try:
                limit = self.get_argument('limit', 20)
                if limit > MAX_LIM_TO_VIEW_PROFILES:
                    limit = MAX_LIM_TO_VIEW_PROFILES
                campaign_or_user = args[1]
                tmp = []
                if (campaign_or_user == 'campaign'):
                    campaign_id = self.get_argument('campaign_id', None)

                    cursor = motor_column.tweets.\
                        find({'campaign_id': campaign_id,
                              'tweet.user.history': False}).\
                        sort('record_retrieved_at', -1).\
                        limit(limit)

                    for record in (yield cursor.to_list(length=100)):
                        user = record['tweet']['user']
                        user['present'] = True
                        user['record_retrieved_at'] = record['record_retrieved_at']
                        #id_str = user['id_str']
                        #user['known_followers_count'] = graph_coll.find({'friend_id_str': id_str}).count();
                        #user['known_friends_count'] = graph_coll.find({'id_str': id_str}).count();
                        id = user['id']
                        user['known_followers_count'] = \
                            yield motor_column.graph.find({'friend_id': id}).count()
                        user['known_friends_count'] = \
                            yield motor_column.graph.find({'id': id}).count()
                        tmp.append(user)

                elif (campaign_or_user == 'user'):
                    user_id_list = self.get_argument('user_id_list', '')
                    user_id_array = user_id_list.split(',')
                    for user_id_str in user_id_array:
                        record = None
                        print "USER " + user_id_str
                        if user_id_str:
                            record = yield motor_column.tweets.\
                                find_one({'tweet.user.id_str': user_id_str,
                                'tweet.user.history': False})
                        else:
                            continue
                        user = dict()
                        if record:
                            user = record['tweet']['user']
                            user['present'] = True
                            user['record_retrieved_at'] = record['record_retrieved_at']
                            #id_str = user['id_str']
                            #user['known_followers_count'] = graph_coll.find({'friend_id_str': id_str}).count();
                            #user['known_friends_count'] = graph_coll.find({'id_str': id_str}).count();
                            id = user['id']

                        else:
                            user['present'] = False
                            user['id_str'] = user_id_str
                        user['known_followers_count'] = \
                            yield motor_column.graph.find({'friend_id_str': user_id_str}).count()
                        user['known_friends_count'] = \
                            yield motor_column.graph.find({'id_str': user_id_str}).count()
                        tmp.append(user)
                else:
                    raise MissingArgumentError('campaign_id or user_id_list')



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

                try:
                    self.store_multiple_profiles(ids, S, drnjID=auth_user_id, campaign_id=campaign_id)
                except Return, r:
                    nids = r.value

                    from drenaj_api.handlers.schedulerMainHandler import markProtected

                    if len(nids) > 0:

                        for i in range(len(nids)):
                            markProtected(motor_column.queue, nids[i], True, auth_user_id)
                            print "User not Found, Removing from queue: ",
                            print nids[i]

                    # Returns profile ids that could not be retrieved
                    print nids
                    self.write(bson.json_util.dumps(nids))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)

    @gen.coroutine
    def store_multiple_profiles(self, ids, S, drnjID, campaign_id):
        """

        """
        # print "Received recent profile of ", v['name'], ' a.k.a. ', v['screen_name']

        db = self.application.db
        queue_coll = db.motor_column.queue

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

            DB_TEST_VERSION = 0.2
            tweet_dat = validate_document(new_tweet_template(), {
                "tweet": status,
                # TODO: Replace this DB_TEST_VERSION with source code
                # version later
                "drenaj_service_version": DB_TEST_VERSION,
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
            yield queue_coll.update(queue_query, queue_document, upsert=True)

            # Insert to profiles
    ##         profiles_query = {"profile.id": user_id}
    ##         prof = profiles_collection.find_and_modify(profiles_query, remove=True)
    ##         if prof is not None:
    ##             profiles_history_collection.insert(prof)
    ##
    ##         profiles_collection.insert(profile_dat)

            # this call marks the current entries as history
            # maybe we won't need this for certain queries
            db.move_to_history(user_id)

            db.insert_tweet(tweet_dat)
    #        tweets_collection.insert(tweet_dat)

            ids.remove(int(user_id))

        raise Return(ids)
