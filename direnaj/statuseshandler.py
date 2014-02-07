from config import *
import drnj_time

from direnaj_collection_templates import *

import direnajmongomanager
from direnajmongomanager import *
from direnaj_auth import direnaj_simple_auth

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

from jinja2 import Environment, FileSystemLoader

import json
import time, datetime

import bson.json_util

class StatusesHandler(tornado.web.RequestHandler):

    ## def datetime_hook(self, dct):
    ##     # TODO: this only checks for the first level 'created_at'
    ##     # We should think about whether making this recursive.
    ##     if 'created_at' in dct:
    ##         time_struct = time.strptime(dct['created_at'], "%a %b %d %H:%M:%S +0000 %Y") #Tue Apr 26 08:57:55 +0000 2011
    ##         dct['created_at'] = datetime.datetime.fromtimestamp(time.mktime(time_struct))
    ##         return bson.json_util.object_hook(dct)
    ##     return bson.json_util.object_hook(dct)

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
                # TODO: we need to get here again.
                user_id = self.get_argument('user_id')
                # if no user_id is supplied.
                if user_id == '':
                    tmp = []
                else:

                    tweets_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                    # running the query
                    cursor = tweets_coll.find({
                        'tweet.user_id_str': str(user_id),
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
                campaign_id = self.get_argument('campaign_id', 'default')
                if tweet_data:
                    #tweet_array = bson.json_util.loads(tweet_data, object_hook=self.datetime_hook)
                    tweet_array = bson.json_util.loads(tweet_data)
                    tmp_tweets = []
                    tmp_hashtags = []
                    tmp_urls = []
                    tmp_user_mentions = []
                    tmp_medias = []
                    tmp_coordinates = []
                    tmp_status_id_strs = []
                    tweets_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                    hashtags_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['hashtags']
                    urls_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['urls']
                    user_mentions_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['user_mentions']
                    medias_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['medias']
                    coordinates_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['coordinates']
                    # TODO: Sanity check the data!
                    # For example, treat 'entities', 'user' specially.
                    for tweet_obj in tweet_array:
                        tmp_status_id_strs.append(tweet_obj['id_str'])
                        tweet_obj['user']['history'] = False
                        tmp_tweets.append(validate_document(new_tweet_template(), {
                            "tweet": tweet_obj,
                            # TODO: Replace this DB_TEST_VERSION with source code
                            # version later
                            "direnaj_service_version": DB_TEST_VERSION,
                            "retrieved_by": keywords['drnjID'],
                            "campaign_id": campaign_id,
                            "record_retrieved_at": drnj_time.now_in_drnj_time(),
                        }, fail=False))
                        ### TODO: add these users later.
                        ### tmp_users.append(validate_document(new_user_template(), tweet_obj['user']))
                        if 'entities' in tweet_obj:
                            if 'hashtags' in tweet_obj['entities']:
                                tmp_hashtags.append([tweet_obj['id_str'], campaign_id, tweet_obj['created_at']] + tweet_obj['entities']['hashtags'])
                            if 'urls' in tweet_obj['entities']:
                                tmp_urls.append([tweet_obj['id_str'], campaign_id, tweet_obj['created_at']] + tweet_obj['entities']['urls'])
                            if 'user_mentions' in tweet_obj['entities']:
                                tmp_user_mentions.append([tweet_obj['id_str'], campaign_id, tweet_obj['created_at']] + tweet_obj['entities']['user_mentions'])
                            if 'media' in tweet_obj['entities']:
                                tmp_medias.append([tweet_obj['id_str'], campaign_id, tweet_obj['created_at']] + tweet_obj['entities']['media'])
                        if 'coordinates' in tweet_obj and tweet_obj['coordinates']:
                            tmp_coordinates.append([tweet_obj['id_str'], campaign_id, tweet_obj['created_at'], tweet_obj['coordinates']])
                    # TODO: parametrize these 4 for loops later.
                    for el in tmp_hashtags:
                        status_id = el[0]
                        created_at = el[2]
                        campaign_id = el[1]
                        for hashtag in el[3:]:
                            hashtags_coll.insert(validate_document(new_hashtag_template(),
                                {"hashtag": hashtag, "campaign_id": campaign_id, "status_id_str": status_id, "created_at": created_at}, fail=False))
                    for el in tmp_urls:
                        status_id = el[0]
                        created_at = el[2]
                        campaign_id = el[1]
                        for url in el[3:]:
                            urls_coll.insert(validate_document(new_url_template(),
                                {"url": url, "campaign_id": campaign_id, "status_id_str": status_id, "created_at": created_at}, fail=False))
                    for el in tmp_user_mentions:
                        status_id = el[0]
                        created_at = el[2]
                        campaign_id = el[1]
                        for user_mention in el[3:]:
                            user_mentions_coll.insert(validate_document(new_user_mention_template(),
                                {"user_mention": user_mention, "campaign_id": campaign_id, "status_id_str": status_id, "created_at": created_at}, fail=False))
                    for el in tmp_medias:
                        status_id = el[0]
                        created_at = el[2]
                        campaign_id = el[1]
                        for media in el[3:]:
                            medias_coll.insert(validate_document(new_media_template(),
                                {"media": media, "campaign_id": campaign_id, "status_id_str": status_id, "created_at": created_at}, fail=False))
                    for el in tmp_coordinates:
                        status_id = el[0]
                        created_at = el[2]
                        campaign_id = el[1]
                        coordinates = el[3]
                        coordinates_coll.insert(validate_document(new_coordinates_template(),
                                {"coordinates": coordinates, "campaign_id": campaign_id, "status_id_str": status_id, "created_at": created_at}, fail=False))
                    direnajmongomanager.insert_tweet(tmp_tweets)
#                    tweets_coll.insert(tmp_tweets)
                else:
                    tmp = []

                if verbose_response:
                    self.write(bson.json_util.dumps({'results': tmp}))
                else:
                    self.write(bson.json_util.dumps({'results': 'ok'}))
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
            try:
                campaign_id = self.get_argument('campaign_id')
                skip = self.get_argument('skip', 0)
                limit = self.get_argument('limit', 100)
                res_format = self.get_argument('format', 'json')
                since_datetime = self.get_argument('since_datetime', -1)
                until_datetime = self.get_argument('until_datetime', -1)
                sort_by_datetime = self.get_argument('sort_by_datetime', 0)

                tweets_coll = direnajmongomanager.mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]['tweets']
                query_string = {'campaign_id' : '%s' % campaign_id}
                if since_datetime != -1:
                    if 'tweet.created_at' not in query_string:
                        query_string['tweet.created_at'] = {}
                    query_string['tweet.created_at']['$gte'] = float(since_datetime)
                if until_datetime != -1:
                    if 'tweet.created_at' not in query_string:
                        query_string['tweet.created_at'] = {}
                    query_string['tweet.created_at']['$lt'] = float(until_datetime)
                sort_string = []
                if sort_by_datetime != 0:
                    sort_string = [('tweet.created_at', pymongo.ASCENDING)] # ascending
                print query_string
                cursor = tweets_coll.find(query_string)
                if sort_string:
                    cursor = cursor.sort(sort_string)
                cursor = cursor.skip(int(skip)).limit(int(limit))
                           # TODO: removing because of complaint:
                           # TypeError: if no direction is specified, key_or_list must be an instance of list
                           # .sort({"$natural" : 1})\
                tmp = [x for x in cursor]
                if res_format == 'json':
                    self.write(bson.json_util.dumps(
                            {'results': tmp,
                            # TODO: Replace this DB_TEST_VERSION with source code
                            # version later
                            "direnaj_service_version": DB_TEST_VERSION,
                            "requested_by": keywords['drnjID'],
                            "campaign_id": campaign_id,
                            "served_at": drnj_time.now_in_drnj_time(),
                             'skip': int(skip),
                             'limit': int(limit)}))
                    self.add_header('Content-Type', 'application/json')
                elif res_format == 'html':
                    env = Environment(loader=FileSystemLoader('templates'))

                    template = env.get_template('statuses/filter.html')
                    result = template.render(statuses=[x['tweet'] for x in tmp])
                    self.write(result)

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
            # db.tweets.find({'campaign_id' : 'syria'}).sort({$natural : 1}).skip(10).limit(14)
        else:
            # Tornado will make sure that this point will not be reached, if
            # it's regexp based router works correctly
            raise HTTPError(404, 'No such method')
