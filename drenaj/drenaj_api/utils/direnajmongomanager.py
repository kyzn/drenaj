"""Refactored DirenajMongoManager

This class accepts mongo specific configuration and collects previous plain functions
inside the class.

There are some problems with mongodb and tornado interaction, so some parts need to be done in a synchronous way.
Thus, the manager opens 2 connections to mongodb server using motor and pymongo libraries. In the problematic places,
pymongo is used. However, most part of the interaction is done using motor library
"""

__author__ = 'Eren Turkay <turkay.eren@gmail.com> \
              Onur Gungor <onurgu@boun.edu.tr>'

from tornado import gen
from tornado.gen import Return

import pymongo
import motor

import logging

import time
from utils.drnj_time import py_utc_time2drnj_time, drnj_time2py_time, now_in_drnj_time, xdays_before_now_in_drnj_time

from drenaj_api.utils.drenajneo4jmanager import add_to_watchlist

'''NOTES:

create_indices

'''

class Column(object):
    def __init__(self, mongo_connection):
        """Data structure for holding mongodb columns and interacting with them

        We keep 2 connections to mongodb server using motor and pymongo libraries for async/sync processes. To keep
        the code clean, mongo_connection is passed from the __init__ of DirenajMongoManager stating which library
        to use.

        :param mongo_connection: mongodb client to use
        :type mongo_connection: Motor or PyMongo mongo client
        """
        self.queue = mongo_connection['queue']
        self.graph = mongo_connection['graph']
        self.tweets = mongo_connection['tweets']
        self.accounts = mongo_connection['accounts']
        self.histograms = mongo_connection['histograms']
        self.campaigns = mongo_connection['campaigns']
        self.watchlist = mongo_connection['watchlist']
        self.pre_watchlist = mongo_connection['pre_watchlist']

        self.freq_campaigns = mongo_connection["freq_campaigns"]
        self.freq_tokens = mongo_connection["freq_tokens"]
        self.freq_hastags = mongo_connection["freq_hashtags"]
        self.freq_mentions = mongo_connection["freq_mentions"]
        self.freq_urls = mongo_connection["freq_urls"]
        self.freq_medias = mongo_connection["freq_medias"]
        self.freq_histograms = mongo_connection['histograms']


class DirenajMongoManager(object):
    def __init__(self, mongo_host, mongo_port, mongo_db):
        """Mongo DB manager for Direnaj specific tasks

        This class initialises mongodb connection using Motor. It allows us to pass mongodb arguments
        when initializing the class. In Tornado application, this database manager is initialized.

        :param mongo_host: Mongodb host
        :type mongo_host: str
        :param mongo_port: MongoDB port
        :type mongo_port: str
        :param mongo_db: Database to use
        :type mongo_db: str
        """

        self.logger = logging.getLogger()

        # We get some errors in motor client. In some parts, pymongo client needs to be used. Thus,
        # create two connection with different libraries and interact with them.
        motor_client = motor.MotorClient(mongo_host, mongo_port)
        pymongo_client = pymongo.MongoClient(mongo_host, mongo_port)

        self.pymongo_column = Column(pymongo_client[mongo_db])
        self.motor_column = Column(motor_client[mongo_db])

        self.colls = {
                'campaigns': self.motor_column.freq_campaigns,
                'tokens': self.motor_column.freq_tokens,
                #'hashtags': self.motor_column.freq_hashtags,
                'mentions': self.motor_column.freq_mentions,
                'urls': self.motor_column.freq_urls,
                'medias': self.motor_column.freq_medias,
                'histograms': self.motor_column.freq_histograms,
        }

    def get_user_from_watchlist(self, user):
        pre_watchlist_column = self.pymongo_column.pre_watchlist
        watchlist_column = self.pymongo_column.watchlist

        if user['id_str']:
            cursor = pre_watchlist_column.find_one({'state': 0, 'user.id_str': user['id_str']},
                                                   fields=['user', 'since_tweet_id', 'page_not_found'])
            if cursor:
                pre_watchlist_column.update({'_id': cursor['_id']}, {'$set': {'state': 1}})
                return cursor
            else:
                cursor = watchlist_column.find_one({'state': 0, 'user.id_str': user['id_str']},
                                                   fields=['user', 'since_tweet_id', 'page_not_found'])
                if cursor:
                    watchlist_column.update({'_id': cursor['_id']}, {'$set': {'state': 1}})
                    return cursor
        else:
            cursor = pre_watchlist_column.find_one({'state': 0, 'user.screen_name': user['screen_name']},
                                                   fields=['user', 'since_tweet_id', 'page_not_found'])
            if cursor:
                pre_watchlist_column.update({'_id': cursor['_id']}, {'$set': {'state': 1}})
            else:
                cursor = watchlist_column.find_one({'state': 0, 'user.screen_name': user['screen_name']},
                                                   fields=['user', 'since_tweet_id', 'page_not_found'])
                watchlist_column.update({'_id': cursor['_id']}, {'$set': {'state': 1}})
                return cursor
            return cursor



    @gen.coroutine
    def update_watchlist(self, user, since_tweet_id, page_not_found):
        pre_watchlist_column = self.motor_column.pre_watchlist
        watchlist_column = self.motor_column.watchlist

        self.logger.debug(user)
        doc = yield pre_watchlist_column.find_one({'user.id_str': user['id_str'], 'state': 1})
        if not doc:
            doc = yield pre_watchlist_column.find_one({'user.screen_name': user['screen_name'], 'state': 1})
        if doc:
            yield pre_watchlist_column.remove({'_id': doc['_id']})
            del doc['_id']
        else:
            doc = yield watchlist_column.find_one({'user.id_str': user['id_str'], 'state': 1})
            if not doc:
                return
        if 'campaign_ids' in doc['user']:
            user.update({'campaign_ids': doc['user']['campaign_ids']})

        doc['user'] = user
        doc['since_tweet_id'] = since_tweet_id
        doc['page_not_found'] = page_not_found
        doc['updated_at'] = now_in_drnj_time()
        doc['state'] = 0

        yield watchlist_column.update({'user.id_str': doc['user']['id_str']}, doc, upsert=True)
        # watchlist_coll.update({'user.id_str': user_id_str}, {'$set': {'since_tweet_id': since_tweet_id,
        #                                                               'page_not_found': page_not_found,
        #                                                               'updated_at': now_in_drnj_time()}})

    @gen.coroutine
    def create_campaign(self, params):
        campaigns_column = self.motor_column.campaigns

        params.update({'created_at': now_in_drnj_time()})

        user_id_strs_to_follow = str(params["user_id_strs_to_follow"])
        user_screen_names_to_follow = str(params["user_screen_names_to_follow"])
        # removing it to be used elsewhere
        params.pop("user_id_strs_to_follow", None)
        params.pop("user_screen_names_to_follow", None)
        add_to_watchlist(params['campaign_id'], user_id_strs_to_follow, user_screen_names_to_follow)

        yield campaigns_column.insert(params)

    def get_campaign(self, campaign_id):
        return self.motor_column.campaigns.find_one({'campaign_id': campaign_id})

    def get_campaigns_list(self):
        #cursor = yield campaigns_coll.find({}).to_list()
        #raise Return(cursor)
        return self.motor_column.campaigns.find({})

    def get_campaign_list_with_freqs(self, skip, limit):
        import sys
        self.logger.debug("GET_CAMPAIGN_LIST_WITH_FREQS: ", "skip: ", skip, ", limit", limit)

        cursor = self.motor_column.freq_campaigns.aggregate(
            [{"$group": {"_id": "$key", "total": {"$sum": "$day_total"}, "last_date": {"$max": "$date"}}},
             {"$sort": {"last_date": -1, "total": -1}}, {"$skip": skip},
             {"$limit": limit}])
        self.logger.debug("GET_CAMPAIGN_LIST_WITH_FREQS: ", "skip: ", skip, ", limit", limit)
        sys.stdout.flush()
        return cursor

    def get_campaign_with_freqs(self, campaign_id):
        #[{"$match": {"campaign_id": campaign_id, "date": today}},

        cursor = self.motor_column.freq_campaigns.aggregate(
            [{"$match": {"campaign_id": campaign_id}},
             {"$sort": {"date": -1}}, {"$limit": 1},
             {"$project": {"campaign_id": 1, "date": 1, "series": {"hour": "$hour", "minute": "$minute"}}}])
        return cursor
        #raise Return(cursor['result'])

    @gen.coroutine
    def check_auth(self, username, password):
        db_user = yield self.motor_column.accounts.find({"drenajID": username,
                                                         "password": password}).to_list(length=100)
        raise Return(db_user)

    def get_users_attached_to_campaign(self, campaign_id):
        pre_watchlist_column = self.motor_column.pre_watchlist
        watchlist_column = self.motor_column.watchlist

        campaign_query = {'user.campaign_ids': campaign_id}
        pre_watchlist_cursor = pre_watchlist_column.find(campaign_query)
        watchlist_cursor = watchlist_column.find(campaign_query)

        return [pre_watchlist_cursor, watchlist_cursor]

    def get_users_related_with_campaign(self, campaign_id):
        campaign_query = {'campaign_id': campaign_id}

        projection_query = {'tweet.user.id_str': 1,
                            'tweet.user.default_profile_image': 1,
                            'tweet.user.profile_image_url': 1,
                            'tweet.user.created_at': 1,
                            'tweet.user.followers_count': 1,
                            'tweet.user.friends_count': 1,
                            'tweet.user.statuses_count': 1,
                            'tweet.user.utc_offset': 1,
                            'tweet.user.lang': 1,
                            'tweet.user.timezone': 1,
                            }

        group_query = {'_id': '$tweet.user.id_str',
                       'n_user_tweets': {'$sum': 1},
                       'user': {'$first': '$tweet.user'}}

        pipeline = [{'$match': campaign_query},
                    {'$project': projection_query},
                    {'$group': group_query}]

        return [self.motor_column.tweets.aggregate(pipeline),
                self.motor_column.tweets.find(campaign_query)]

    def get_campaign_histograms(self, campaign_id):
        hists = self.motor_column.histograms.find({'campaign_id': campaign_id}).sort([('created_at', -1)])
        return hists

    def move_to_history(self, user_id):
        yield self.motor_column.tweets.update({'tweet.user.id_str': user_id, 'tweet.user.history': False},
                                 {'$set': {'tweet.user.history': True}})

    @gen.coroutine
    def insert_tweet(self, tweet_obj_array):

        # actual tweet insertion
        yield self.motor_column.tweets.insert(tweet_obj_array)

        for tweet_obj in tweet_obj_array:

            # build the analytics
            freq = {}

            campaign_id = tweet_obj['campaign_id']

            # strip unnecessary fields
            tweet_obj = tweet_obj['tweet']

            freq['campaigns'] = {campaign_id: 1}

        # freq['tokens'] = {'ali': 1, 'veli': 1}

            freq['hashtags'] = {}
            if 'entities' in tweet_obj and 'hashtags' in tweet_obj['entities']:
                for hashtag in tweet_obj['entities']['hashtags']:
                    if 'text' in hashtag:
                        item_key = hashtag['text']
                        if item_key in freq['hashtags']:
                            freq['hashtags'][item_key] += 1
                        else:
                            freq['hashtags'][item_key] = 1
                    else:
                        # log this missing attribute.
                        pass

            freq['mentions'] = {}
            if 'entities' in tweet_obj and 'user_mentions' in tweet_obj['entities']:
                for mention in tweet_obj['entities']['user_mentions']:
                    if 'id_str' in mention:
                        item_key = "|".join([mention['id_str'], mention['screen_name']])
                        if item_key in freq['mentions']:
                            freq['mentions'][item_key] += 1
                        else:
                            freq['mentions'][item_key] = 1
                    else:
                        # log this missing attribute.
                        pass

            freq['urls'] = {}
            if 'entities' in tweet_obj and 'urls' in tweet_obj['entities']:
                for url in tweet_obj['entities']['urls']:
                    if 'expanded_url' in url:
                        item_key = url['expanded_url']
                        if item_key in freq['urls']:
                            freq['urls'][item_key] += 1
                        else:
                            freq['urls'][item_key] = 1
                    else:
                        # log this missing attribute.
                        pass

            if 'created_at' in tweet_obj:
                # turns out that we've already transformed into drnj_time
                t = drnj_time2py_time(tweet_obj['created_at'])
            else:
                t = time.time()

            gm_t = time.gmtime(t)

            today_str = time.strftime('%Y-%m-%d', gm_t)
            hour = time.strftime('%H', gm_t)
            minute = "%04d" % (int(hour)*60 + int(time.strftime('%M', gm_t)))

            for key in freq:
                for item in freq[key].keys():
                    count = freq[key][item]

                    yield self.colls[key].update({'campaign_id': campaign_id, 'date': today_str, 'key': item},
                                      {'$inc': {('hour.%s' % hour): count, ('minute.%s' % minute): count, ('day_total'): count},
                                       '$set': {'last_updated_minute': minute}}, upsert=True)


    @staticmethod
    def prepare_hist_and_plot(self, n_tweets, users, n_bins, campaign_id):
        import numpy
        import matplotlib.pyplot as plot

        plot_graphs = False

        hist = {
            'user_creation': {
                'data': None,
                'bins': None,
            },
            'user_n_tweets': {
                'data': None,
                'bins': None,
            },
            'user_n_tweets_overall': {
                'data': None,
                'bins': None,
            },
            'n_tweets': None,
            'n_unique_users': None,
            'n_default_profile_image': None,
            'n_lower_than_threshold': None,
        }

        self.logger.debug("How many tweets? %d" % n_tweets)
        hist['n_tweets'] = n_tweets

        # TODO: abort if there are more than 200000 tweets.
        if n_tweets > 200000:
            return
        #
        # How many unique users?
        #
        n_unique_users = len(users)
        self.logger.debug("How many unique users? %d" % n_unique_users)
        hist['n_unique_users'] = n_unique_users

        ######
        sec_title = "Histogram of user creation dates?"
        #

        tmp_dates = []
        for x in users:
            tmp_date = x['user']['created_at']
            if type(tmp_date) != float:
                tmp_date = py_utc_time2drnj_time(tmp_date)
            tmp_dates.append(tmp_date)
    #    tmp_dates = [py_utc_time2drnj_time(x['user']['created_at']) for x in users]

        (hist['user_creation']['data'], hist['user_creation']['bins']) = numpy.histogram(tmp_dates, bins=n_bins)

        if plot_graphs:
            bins = hist['user_creation']['bins'][:-1]
            width = (hist['user_creation']['bins'][1] - hist['user_creation']['bins'][0])/2
            plot.bar(bins, hist['user_creation']['data'], width=width, align='center')

            xticklabels = [time.strftime('%d %b %Y', time.gmtime(drnj_time2py_time(x))) for x in bins]

            plot.xticks(bins, xticklabels)
            plot.title(sec_title)
            #plot.show()
            plot.savefig('1.pdf', dpi=600)

        #####
        sec_title = "Histogram of number of tweets of each user in this campaign"
        tmp_counts = [int(x['n_user_tweets']) for x in users]
        #
        (hist['user_n_tweets']['data'], hist['user_n_tweets']['bins']) = numpy.histogram(tmp_counts, bins=n_bins)

        if plot_graphs:
            bins = hist['user_n_tweets']['bins'][:-1]
            data = hist['user_n_tweets']['data']
            width = (hist['user_n_tweets']['bins'][1] - hist['user_n_tweets']['bins'][0])/2
            plot.bar(bins, data, width=width, align='center')

            xticklabels = bins

            plot.xticks(bins, xticklabels)
            plot.title(sec_title)
            #plot.show()
            plot.savefig('2.pdf', dpi=600)

        #####
        sec_title = "What percentage of them used the default profile image?"
        #
        n_default_profile_image = 0
        for u in users:
            if u['user']['default_profile_image']:
                n_default_profile_image += 1

        hist['n_default_profile_image'] = n_default_profile_image
        self.logger.debug("%s: %0.2f%%" % (sec_title, 100*(float(n_default_profile_image)/n_unique_users)))
        #####
        sec_title = "Histogram of tweet counts of unique users"
        tmp_counts = [int(x['user']['statuses_count']) for x in users]

        (hist['user_n_tweets_overall']['data'],
         hist['user_n_tweets_overall']['bins']) = numpy.histogram(tmp_counts, bins=n_bins)

        if plot_graphs:
            bins = hist['user_n_tweets_overall']['bins'][:-1]
            data = hist['user_n_tweets_overall']['data']
            width = (hist['user_n_tweets_overall']['bins'][1] - hist['user_n_tweets_overall']['bins'][0])/2
            plot.bar(bins, data, width=width, align='center')

            xticklabels = bins

            plot.xticks(bins, xticklabels)
            plot.title(sec_title)
            #plot.show()
            plot.savefig('3.pdf', dpi=600)
        #
        sec_title = "What percentage of them have lower than 5 tweets?"
        n_lower_than_threshold = 0
        for u in users:
            if u['user']['statuses_count'] < 5:
                n_lower_than_threshold += 1

        hist['n_lower_than_threshold'] = n_lower_than_threshold
        self.logger.debug("%s: %0.2f%%" % (sec_title, 100*(float(n_lower_than_threshold)/n_unique_users)))

        self.logger.debug(hist)

        # converting numpy.array's to normal python lists.
        for k in hist.keys():
            if type(hist[k]) == dict:
                for k2 in hist[k].keys():
                    if type(hist[k][k2]) == type(numpy.array([])):
                        hist[k][k2] = list(hist[k][k2])

        hist = {'campaign_id': campaign_id,
                'histogram': hist,
                'created_at': now_in_drnj_time()}
        return hist
