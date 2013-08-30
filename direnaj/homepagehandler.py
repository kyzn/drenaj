from config import *
import drnj_time

from direnajmongomanager import *
from direnaj_auth import direnaj_simple_auth

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

import json
import time

import bson.json_util

class HomepageHandler(tornado.web.RequestHandler):
    def get(self, *args):

        tweets_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
        # running the query
        cursor = tweets_coll.find({
            'campaign_id': 'merdiven'
        },
        {
            'tweet.text': 1,
            'tweet.user.screen_name': 1
        })

        tmp = [x for x in cursor]

        self.write(bson.json_util.dumps({'results': tmp}))
        self.add_header('Content-Type', 'application/json')

