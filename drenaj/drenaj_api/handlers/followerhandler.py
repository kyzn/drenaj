'''
Stores Retrieved followers or friends of a user

'''

# Change History :
# Date                                          Prog    Note
# Wed Aug 15 14:34:36 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

#from drenaj_api.config.config import *
import utils.drnj_time as drnj_time

from drenaj_api.utils.drenaj_collection_templates import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado import gen
from tornado.gen import Return

import time

import bson.json_util

from py2neo import Graph

from drenaj_api.utils.drenajneo4jmanager import init_user_to_graph_aux

from drenaj_api.celery_app.server_endpoint import app_object

graph = Graph()

class FollowerHandler(tornado.web.RequestHandler):


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

                    # friends or followers
                    if friends_or_followers == 'followers':
                        id_field_prefix_graph_query = 'friend_'
                        id_field_prefix_graph_query_opposite = ''
                    elif friends_or_followers == 'friends':
                        id_field_prefix_graph_query = ''
                        id_field_prefix_graph_query_opposite = 'friend_'

                    tx = graph.cypher.begin()
                    if friends_or_followers == 'followers':
                        tx.append("MATCH (u { id_str:{id_str} })<--(u2) RETURN u2", {"id_str": str(user_id)})
                    elif friends_or_followers == 'friends':
                        tx.append("MATCH (u { id_str:{id_str} })-->(u2) RETURN u2", {"id_str": str(user_id)})
                    results = tx.commit()
                    # take only the first line
                    results = results[0]

                    tmp = []
                    if ids_or_list == 'ids':
                        tmp = [x.u2.properties['id_str'] for x in results]
                    elif ids_or_list == 'list':
                        tmp = [x.u2.properties for x in results]
                self.write(bson.json_util.dumps({'results': tmp}))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                # Check long int/str versions
                id_str = str(self.get_argument('id_str'))
                campaign_id = self.get_argument('campaign_id', 'default')
                user_objects_str = self.get_argument('user_objects', '[]')

                res = app_object.send_task('store_friendsfollowers_in_neo4j_offline',
                                           [[ id_str, campaign_id, user_objects_str, friends_or_followers ]],
                                           queue="offline_jobs")

                self.write(bson.json_util.dumps({'status': res.task_id}))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass
        elif store_or_view == 'store_user_info':
            try:
                user_object = self.get_argument('user_object', '{}')
                user_object = bson.json_util.loads(user_object)

                user_node = init_user_to_graph_aux('default', user_object)

                if user_node:
                    tx = graph.cypher.begin()
                    tx.append("MATCH (u:User { id_str: {id_str} })<-[r]-(t:USER_INFO_HARVESTER_TASK {id: 1}) DELETE r", {'id_str': user_object['id_str']})
                    tx.commit()
                else:
                    self.write(bson.json_util.dumps({'status': 'error'}))

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass

    # NOTE: Not really used. Will be replaced when we bring Neo4J integration.
    @gen.coroutine
    def store_friends_or_followers(self, user_id, IDS, drnjID, fof):
        """Stores/updates list of drenaj user data using raw twitter data

        IDS -- list of user ids client crawler reports
        """
        # db = mongo_client[DRENAJ_DB[DRENAJ_APP_ENVIRONMENT]]
        queue_collection = self.application.db.motor_column.queue
        graph_collection = self.application.db.motor_column.graph

        num_new_discovered_users = 0
        num_edges_inserted = 0

        dt = drnj_time.now_in_drnj_time()
        queue_query = {"id": user_id}

        print queue_query

        # ATC: This mechanism requires finding the id twice
        # With indexing, this may not be a big problem
        # Alternative is trying to update and catching the pymongo.errors.OperationFailure exception
        n_ids = yield queue_collection.find(queue_query).count()
        id_exists =  n_ids > 0

        if id_exists:
            print 'ID EXISTS'
            queue_document = {"$set":
                                {
                                fof +"_retrieved_at": dt,
                                "retrieved_by": drnjID}
                               }
            # creates entry if query does not exist
            # queue_collection.update(queue_query, queue_document, upsert=True)

            yield queue_collection.update(queue_query, queue_document)

        else:
            print 'ID NOT EXISTS'
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
            print "QUEUE DOCUMENT: " + queue_document
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

            yield queue_collection.insert(queue_document)
            num_new_discovered_users += 1

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
                num_edges_inserted += 1

        # TODO: Handle unfollows: Find edges that no longer exist and move old record to graph_history and add unfollow record

        raise Return({'num_new_users': num_new_discovered_users, 'num_new_edges': num_edges_inserted})
