from config import *
from drnj_time import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web
import random

from tornado.escape import json_decode,json_encode

class SchedulerMainHandler(tornado.web.RequestHandler):
    def get(self, *args):

        (friends_or_followers) = args

        db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
        queue_collection = db['queue']

        # Get the N'th most frequent visited one
        N = 100

        if friends_or_followers == 'followers':
            cur = queue_collection.find({"protected": {"$ne": True}}).sort('followers_retrieved_at',-1).limit(N)
        else:
            cur = queue_collection.find({"protected": {"$ne": True}}).sort('friends_retrieved_at',-1).limit(N)

        a = []
        for c in cur:
            a.append(c["_id"])
            #print c

        idx = random.randint(0, N-1)
        # TODO: look here again.
        if len(a) == 0:
            self.write(json_encode(0))
            print "Suggest ToGet %s : User %d " % (friends_or_followers, 0)
        else:
            self.write(json_encode(a[idx]))
            print "Suggest ToGet %s : User %d " % (friends_or_followers, a[-1])

    def post(self, *args):
        self.write("Not Implemented")

class SchedulerReportHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.write("Not Implemented")

    def post(self, *args):
        user_id = int(self.get_argument('user_id'))
        isProtected = int(self.get_argument('isProtected'))
        print isProtected
        print "Proteced or Missing user detected: %d" % int(user_id)

        db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
        queue_collection = db['queue']

        queue_query = {"_id": user_id}
        queue_document = {"$set":
                            {
                            "protected" : bool(isProtected),
                            "retrieved_by": drnjID}
                           }

        queue_collection.update(queue_query, queue_document, upsert=True)


