'''
Stores Retrieved followers or friends of a user

'''

# Change History :
# Date                                          Prog    Note
# Wed Aug 15 14:34:36 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

from direnaj_api.config.config import *
import utils.drnj_time as drnj_time

from direnaj_api.utils.direnajmongomanager import *
from direnaj_api.utils.direnaj_collection_templates import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado import gen
from tornado.gen import Return

from tornado.escape import json_decode,json_encode

import json
import time

import bson.json_util

class FollowerHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    @tornado.web.asynchronous
    def post(self, *args):
        """ I chose to handle all options at once, using only POST requests
        for API requests. GET requests will be used for browser examination.
        """

        (friends_or_followers, ids_or_list, store_or_view) = args

        print "FollowerHandler: {} {} {}".format(friends_or_followers, ids_or_list, store_or_view)

        auth_user_id = self.get_argument('auth_user_id')


        if ((store_or_view != None and store_or_view == 'view') or (store_or_view == None)):
            try:
                user_id = self.get_argument('user_id')
                # if no user_id is supplied.
                if user_id == '':
                    tmp = []
                else:
                    graph_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['graph']

                    # friends or followers
                    if friends_or_followers == 'followers':
                        id_field_prefix_graph_query = 'friend_'
                        id_field_prefix_graph_query_opposite = ''
                    elif friends_or_followers == 'friends':
                        id_field_prefix_graph_query = ''
                        id_field_prefix_graph_query_opposite = 'friend_'

                    # running the query
                    cursor = graph_coll.find({
                        id_field_prefix_graph_query+'id_str': str(user_id),
                    })

                    tmp = []
                    if ids_or_list == 'ids':
                        tmp = [x for x in cursor]
                    elif ids_or_list == 'list':
                        # profiles_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['profiles']
                        tweets_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                        # We need to gather the list of 'opposite' side.
                        ids = [x[id_field_prefix_graph_query_opposite+'id_str'] for x in cursor]
                        print ids
                        cursor = tweets_coll.find({'tweet.user.id_str': {'$in': ids}, 'tweet.user.history': False})
                        tmp = [x for x in (yield cursor.to_list(length=100))]
                self.write(bson.json_util.dumps({'results': tmp}))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                # Check long int/str versions
                user_id = int(self.get_argument('user_id'))
                auth_user_id = self.get_argument('auth_user_id')
                json_IDS = self.get_argument('ids', None)
                IDS = json.loads(json_IDS)

                try:
                    store_friends_or_followers(user_id, IDS, drnjID=auth_user_id, fof=friends_or_followers)
                except Return, r:
                    ret = r.value
                    # Returns number of written edges (new relations discovered)
                    print "User ID: %d, among %s" % (user_id, friends_or_followers)
                    print ret

                    self.write(json_encode(ret))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass

@gen.coroutine
def store_friends_or_followers(user_id, IDS, drnjID, fof):
    """Stores/updates list of direnaj user data using raw twitter data

    IDS -- list of user ids client crawler reports
    """
    db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
    queue_collection = db['queue']
    graph_collection = db['graph']

    num_new_discovered_users = 0;
    num_edges_inserted = 0

    dt = drnj_time.now_in_drnj_time()
    queue_query = {"id": user_id}
    # ATC: This mechanism requires finding the id twice
    # With indexing, this may not be a big problem
    # Alternative is trying to update and catching the pymongo.errors.OperationFailure exception
    n_ids = yield queue_collection.find(queue_query).count()
    id_exists =  n_ids > 0

    if id_exists:
        queue_document = {"$set":
                            {
                            fof +"_retrieved_at": dt,
                            "retrieved_by": drnjID}
                           }
        # creates entry if query does not exist
        # queue_collection.update(queue_query, queue_document, upsert=True)

        yield queue_collection.update(queue_query, queue_document)

    else:
        if fof == 'friends':
            queue_document = validate_document(new_queue_document(), {
                            "id": user_id,
                            "id_str": str(user_id),
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": dt,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID
                            })

        elif fof == 'followers':
            queue_document = validate_document(new_queue_document(),{
                            "id": user_id,
                            "id_str": str(user_id),
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": dt,
                            "retrieved_by": drnjID
                            })
        yield queue_collection.insert(queue_document)
        num_new_discovered_users += 1


    # process each user id in IDS
    for id in reversed(IDS):
        # Insert the newly discovered id into the queue
        # insert will be rejected if _id exists
        queue_document = validate_document(new_queue_document(),{
                            "id": id,
                            "id_str": str(id),
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID})

        try:
            yield queue_collection.insert(queue_document)
            num_new_discovered_users += 1
        except pymongo.errors.OperationFailure as e:
            pass

        dt = drnj_time.now_in_drnj_time()
        if fof == 'friends':
            source = user_id
            sink = id
        elif fof == 'followers':
            source = id
            sink = user_id
        else:
            return

        edge = yield graph_collection.find_one({"id": source, "friend_id": sink})

        if edge == None:
            doc = validate_document(new_graph_document(),{
             'id': source,
             'friend_id': sink,
             'id_str': str(source),
             'friend_id_str': str(sink),
             'record_retrieved_at': dt,
             "retrieved_by": drnjID
            })
            yield graph_collection.insert(doc)
            num_edges_inserted += 1;

    # TODO: Handle unfollows: Find edges that no longer exist and move old record to graph_history and add unfollow record

    raise Return({'num_new_users': num_new_discovered_users, 'num_new_edges': num_edges_inserted})
