import tornado.ioloop
import tornado.web

import json
from tornado.escape import json_decode
from tornado.escape import json_encode
from pymongo import MongoClient
from pymongo import ASCENDING

from datetime import datetime

MONGO_HOST='localhost'
MONGO_PORT=27017

def queue_user_id_update(id_str_list):
    """Adds new users to be explored. Does nothing if user already exists. In the future this can be scheduler trigger."""
    mongo_client = MongoClient(MONGO_HOST, MONGO_PORT)
    db = mongo_client['direnaj']
    profiles_collection = db['profiles']
    users_collection = db['users']

    inserted_ids = []
    for id_str in id_str_list:
        inserted_ids.append( profiles_collection.insert( {"_id": id_str, "created_at": 0, "retrieved_by": 1} ) ) #TODO: retrieved_by from authentication

    return inserted_ids

def store_user_data(raw_twitter_data_list):
    """Stores/updates list of direnaj user data using raw twitter data

    raw_twitter_data_list -- list of raw twitter user data client crawler reports
    """
    mongo_client = MongoClient(MONGO_HOST, MONGO_PORT)
    db = mongo_client['direnaj']
    profiles_collection = db['profiles']
    users_collection = db['users']

    # process each user in raw_twitter_data_list
    for user_data in raw_twitter_data_list:
        profile_query = {"_id": user_data['id_str']}
        profile_document = {"$set": 
                            { "created_at": user_data['created_at'], 
                              "retrieved_by": 1} #TODO: retrieved_by from authentication
                           }
        # creates entry if query does not exist
        # assume actual data is created once and never changed
        profiles_collection.update(profile_query, profile_document, upsert=True)

        # check if we have raw twitter user data or user tracking request
        if user_data['created_at'] != 0:
            # we have raw data store that
            users_collection.insert(
                {"id_str" : user_data['id_str'],
                 "protected": user_data['protected'],
                 "location" : user_data['location'],
                 "screen_name": user_data['screen_name'],
                 "name" : user_data['name'],
                 "followers_count" : user_data['followers_count'],
                 "friends_count" : user_data['friends_count'],
                 "statuses_count" : user_data['statuses_count'],
                 "geo_enabled" : user_data['geo_enabled'],
                 "profile_image_url" : user_data['profile_image_url'],
                 "record_retrieved_at" : datetime.utcnow(),
                 "retrieved_by" : 1
                })
    return 0

    
class MainHandler(tornado.web.RequestHandler):
    def get(self):
        """Returns 100 most decreipt users so that client can report new data about these users"""
        max_num = 100 # number of users to return
        mongo_client = MongoClient(MONGO_HOST, MONGO_PORT)
        db = mongo_client['direnaj']
        profiles_collection = db['profiles']
        users_collection = db['users']
        
        result = []
        # first get data about profiles without a users entry
        for user_data in profiles_collection.find({"created_at" : 0}, limit=100):
            result.append(user_data['_id'])

        # next get data about records updated least recently
        for user_data in users_collection.find(limit=(max_num-len(result))).sort('record_retrieved_at', ASCENDING):
            if user_data['id_str'] not in result:
                result.append(user_data['id_str'])

        self.write(json_encode(result))
                   
    def post(self):
        """Serves /store URL. POST request must contain a variable called json_data with following structure

        json_data -- { "operation_type": "queue_user_id_update / store_user_data",
                       "json_data" : { ... }
                     }
        """
        operation_type = self.get_argument('operation_type', None)
        if operation_type is None:
            print "operation_type is None"
            raise tornado.web.HTTPError(400)

        json_data = self.get_argument('json_data', None)
        if json_data is None:
            print "json_data is None"
            raise tornado.web.HTTPError(400)
        json_data = json.loads(json_data)

        if operation_type == 'queue_user_id_update':
            queue_user_id_update(json_data)
        elif operation_type == 'store_user_data':
            store_user_data(json_data)
        else:
            print "unkown operation type ." + operation_type + "."
            raise tornado.web.HTTPError(400)

        self.write("ok")

application = tornado.web.Application([
    (r"/store", MainHandler),
    (r"/get_decrepit_users", MainHandler),
])

if __name__ == "__main__":
    application.listen(9999)
    tornado.ioloop.IOLoop.instance().start()
