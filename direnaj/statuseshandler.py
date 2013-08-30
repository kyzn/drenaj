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

class StatusesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    @direnaj_simple_auth
    def post(self, *args, **keywords):
        """
        `view`
            - params:
                - user_id: user_id of the twitter user that we want to
                retrieve all tweets.
        `store`
            - params:
                - tweet_data: array of tweets in json format.
        `retweets`
            - params:
                - tweet_id: `id` of the tweet that we want to
                retrieve all retweets.
        `filter`
            - not implemented yet.
        """

        if len(args) > 1:
            (action) = args
        else:
            action = args[0]

        print action

        verbose_response = self.get_argument('verbose', '')

        if (action == 'view'):
            try:
                user_id = self.get_argument('user_id')
                # if no user_id is supplied.
                if user_id == '':
                    tmp = []
                else:

                    tweets_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                    # running the query
                    cursor = tweets_coll.find({
                        'id_str': str(user_id),
                    })

                    tmp = [x for x in cursor]

                self.write(bson.json_util.dumps({'results': tmp}))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'store'):
            try:
                tweet_data = self.get_argument('tweet_data')
                if tweet_data:
                    tweet_array = bson.json_util.loads(tweet_data)
                    # TODO: Sanity check the data!
                    # For example, treat 'entities', 'user' specially.
                    tmp = [{
                        "tweet": tweet_obj,
                        # TODO: Replace this DB_TEST_VERSION with source code
                        # version later
                        "direnaj_service_version": DB_TEST_VERSION,
                        "retrieved_by": keywords['drnjID'],
                        "record_retrieved_at": drnj_time.now_in_drnj_time(),
                    } for tweet_obj in tweet_array]
                    tweets_coll = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                    tweets_coll.insert(tmp)
                else:
                    tmp = []

                if verbose_response:
                    self.write(bson.json_util.dumps({'results': tmp}))
                else:
                    self.write(bson.json_util.dumps({'results': 'ok'})
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'retweets'):
            try:
                tweet_id = self.get_argument('tweet_id')
                self.write('not implemented yet')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'filter'):
            self.write('not implemented yet')
        else:
            # Tornado will make sure that this point will not be reached, if
            # it's regexp based router works correctly
            raise HTTPError(404, 'No such method')
