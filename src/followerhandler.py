from config import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

import json

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
            # ignoring for now.
            self.write('not implemented')
            pass
