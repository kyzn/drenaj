from config import *
from drnj_time import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

import json
import time

import bson.json_util

class FollowerHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """ I chose to handle all options at once, using only POST requests
        for API requests. GET requests will be used for browser examination.
        """

        (friends_or_followers, ids_or_list, store_or_view) = args

        print friends_or_followers
        print ids_or_list
        print store_or_view

        if ((store_or_view != None and store_or_view == 'view') or (store_or_view == None)):
            try:
                user_id = self.get_argument('user_id')
                graph_coll = mongo_client[DIRENAJ_DB]['graph']

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
                    'following': 1
                })

                tmp = []
                if ids_or_list == 'ids':
                    tmp = [x for x in cursor]
                elif ids_or_list == 'list':
                    users_coll = mongo_client[DIRENAJ_DB]['users']
                    # We need to gather the list of 'opposite' side.
                    ids = [x[id_field_prefix_graph_query_opposite+'id_str'] for x in cursor]
                    print ids
                    cursor = users_coll.find({'id_str': {'$in': ids}})
                    tmp = [x for x in cursor]
                self.write(bson.json_util.dumps({'results': tmp}))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                user_id = int(self.get_argument('user_id'))
                json_IDS = self.get_argument('ids', None)
                IDS = json.loads(json_IDS)

#                print "*******************"
#                print user_id
#                for i in IDS:
#                    print i


                #TODO: drnjID obtained from crawler authentication
                ret = store_friends_or_followers(user_id, IDS, drnjID= 'dummy_for_now', fof=friends_or_followers)
                # Returns number of written edges (new relations discovered)
                print "User ID: %d, among %s" % (user_id, friends_or_followers)
                print ret

                self.write(json_encode(ret))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass

### Check this !!! TIME
def now():
    return py_time2drnj_time(time.time())


def store_friends_or_followers(user_id, IDS, drnjID, fof):
    """Stores/updates list of direnaj user data using raw twitter data

    IDS -- list of user ids client crawler reports
    """
    db = mongo_client[DIRENAJ_DB]
    queue_collection = db['queue']
    graph_collection = db['graph']

    num_new_discovered_users = 0;
    num_edges_inserted = 0

    dt = now()
    queue_query = {"_id": user_id}
    # ATC: This mechanism requires finding the _id twice
    # With indexing, this may not be a big problem
    # Alternative is trying to update and catching the pymongo.errors.OperationFailure exception
    id_exists = queue_collection.find(queue_query).count() > 0

    if id_exists:
        queue_document = {"$set":
                            {
                            fof +"_retrieved_at": dt,
                            "retrieved_by": drnjID}
                           }
        # creates entry if query does not exist
        queue_collection.update(queue_query, queue_document, upsert=True)
    else:
        if fof == 'friends':
            queue_document ={
                            "_id": user_id,
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": dt,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID
                            }

        elif fof == 'followers':
            queue_document ={
                            "_id": user_id,
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": dt,
                            "retrieved_by": drnjID
                            }
        queue_collection.insert(queue_document)
        num_new_discovered_users += 1


    # process each user id in IDS
    for id in reversed(IDS):
        # Insert the newly discovered id into the queue
        # insert will be rejected if _id exists
        queue_document = {  "_id": id,
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID}

        try:
            queue_collection.insert(queue_document)
            num_new_discovered_users += 1
        except pymongo.errors.OperationFailure as e:
            pass

        dt = now()
        if fof == 'friends':
            source = user_id
            sink = id
        elif fof == 'followers':
            source = id
            sink = user_id
        else:
            return

        # Find last entry of the relationship user_id->id  (user_id follows id)=>(id is a friend of user_id)
        # This can be probably made more efficient by using .max() ..
        cur = graph_collection.find({"id": source, "friend_id": sink}).sort('record_retrieved_at',-1).limit(1)

        # if cur is empty or following is false insert edge and mark time
        if cur.count()==0 or cur.next()["following"]==0:
            doc = {
             'id': source,
             'friend_id': sink,
             'following': 1,
             'record_retrieved_at': dt,
             "retrieved_by": drnjID
            }
            graph_collection.insert(doc)
            num_edges_inserted += 1;


    return {'num_new_users': num_new_discovered_users, 'num_new_edges': num_edges_inserted}
