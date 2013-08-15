from config import *
from drnj_time import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web

from tornado.escape import json_decode,json_encode

class SchedulerMainHandler(tornado.web.RequestHandler):
    def get(self, *args):
    
        (friends_or_followers) = args
        
        db = mongo_client[DIRENAJ_DB]
        queue_collection = db['queue']

        # Get the N'th most frequent visited one  
        N = 10

        if friends_or_followers == 'followers':
            cur = queue_collection.find().sort('followers_retrieved_at',-1).limit(N)
        else:
            cur = queue_collection.find().sort('friends_retrieved_at',-1).limit(N)
        
        a = []
        for c in cur:
            a.append(c["_id"])
        
        
        self.write(json_encode(a[-1]))
        print "Suggest ToGet %s : User %d " % (friends_or_followers, a[-1])

    def post(self, *args):
        self.write("Not Implemented")
