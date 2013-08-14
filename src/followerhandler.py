from config import *

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
        # I chose to handle all options at once, using only POST requests
        # for API requests. GET requests will be used for browser examination.
        # by the way: I couldn't write this in docstring for some strange reason.

        (friends_or_followers, ids_or_list, store_or_view) = args

        print friends_or_followers
        print ids_or_list
        print store_or_view
        
        if (store_or_view != None and store_or_view == 'view'):
            try:
                user_id = self.get_argument('user_id')
                graph_coll = mongo_client[DIRENAJ_DB]['graph']
                if friends_or_followers == 'friends':
                    # ignoring for now.
                    self.write('not implemented')
                    pass
                elif friends_or_followers == 'followers':
                    cursor = graph_coll.find({'id_str': str(user_id)}).limit(20)
                    tmp = [x for x in cursor]
                    self.write(bson.json_util.dumps({'results': tmp}))
                    self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (store_or_view == 'store'):
            try:
                user_id = self.get_argument('user_id')
                json_IDS = self.get_argument('ids', None)
                IDS = json.loads(json_IDS)

                print "*******************"
                print user_id
                for i in IDS:
                    print i
                if friends_or_followers == 'friends':
                    # ignoring for now.
                    self.write('not implemented')
                    pass
                elif friends_or_followers == 'followers':
                    #TODO: drnjID obtained from crawler authentication
                    store_followers(user_id, IDS, drnjID=1)
                    self.write(json_encode(len(IDS)))
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            pass

### Check this !!! TIME
def now():
    return time.time()
            
            
def store_followers(user_id, IDS, drnjID):
    """Stores/updates list of direnaj user data using raw twitter data

    IDS -- list of user ids client crawler reports
    """
    db = mongo_client[DIRENAJ_DB]
    queue_collection = db['queue']
    graph_collection = db['graph']

    dt = now()   

    queue_query = {"_id": user_id}
    queue_document = {"$set":
                            { 
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": dt,
                            "retrieved_by": drnjID} 
                           }

    # creates entry if query does not exist
    # assume actual data is created once and never changed
    queue_collection.update(queue_query, queue_document, upsert=True)
    
    
    
    # process each user id in IDS
    for id in IDS:
        # Insert the newly discovered id into the queue
        queue_query = {"_id": id}
        queue_document = {"$set":
                            { 
                            "profile_retrieved_at": 0,
                            "friends_retrieved_at": 0,
                            "followers_retrieved_at": 0,
                            "retrieved_by": drnjID} 
                           }

        # creates entry if query does not exist
        # assume actual data is created once and never changed
        queue_collection.update(queue_query, queue_document, upsert=True)

        dt = now()  

        # Find last entry of the relationship id->user_id  (id follows user_id)=>(user_id is a friend of id)
        # This can be probably made more efficient by using .max() ..
        cur = graph_collection.find({"id_str": id, "friend_id_str": user_id}).sort('record_retrieved_at',-1).limit(1)
        
        # if cur is empty or following is false insert edge and mark time
        if cur.count()==0 or cur.next()["following"]==0:
            doc = {
              'id_str': id,
              'friend_id_str': user_id,
              'following': 1,
              'record_retrieved_at': dt,
              "retrieved_by": drnjID
            }
            graph_collection.insert(doc)
            
    return 0
