from config import *

import time

import pymongo

from pymongo import MongoClient

mongo_client = MongoClient(MONGO_HOST, MONGO_PORT)

db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
queue_coll = db['queue']
graph_coll = db['graph']
tweets_coll = db['tweets']
queue_coll = db['queue']

colls = {
    'campaigns': db["freq_campaigns"],
    'tokens': db["freq_tokens"],
    'hashtags': db["freq_hashtags"],
    'mentions': db["freq_mentions"],
    'urls': db["freq_urls"],
    'medias': db["freq_medias"]
}

# INDEXES
#

tweets_coll.create_index([('campaign_id', pymongo.ASCENDING), ('tweet.created_at', pymongo.ASCENDING)])

for key in colls.keys():
    colls[key].create_index([('campaign_id', pymongo.ASCENDING), ('date', pymongo.ASCENDING), ('key', pymongo.ASCENDING)])


def get_campaign_list_with_freqs(skip, limit):
#    cursor = db.freq_campaigns.aggregate({"$group": { "campaign_id": "$key", "totalTweets": {"$sum": "$day_total"}}})
    cursor = colls["campaigns"].aggregate(
        [{"$group": { "_id": "$key", "total": {"$sum": "$day_total"}, "last_date": {"$max": "$date"}}},
         {"$sort": {"last_date": -1, "total": -1}}, {"$skip": skip},
         {"$limit": limit}])
    return cursor['result']

def get_campaign_with_freqs(campaign_id):
#        [{"$match": {"campaign_id": campaign_id, "date": today}},
    today = time.strftime('%Y-%m-%d', time.gmtime())
    cursor = colls['campaigns'].aggregate(
        [{"$match": {"campaign_id": campaign_id}},
         {"$sort": {"date": -1}}, {"$limit": 1},
         {"$project": {"campaign_id": 1, "date": 1, "series": {"hour": "$hour", "minute": "$minute"}}}])
    return cursor['result']

def insert_tweet(tweet_obj_array):

    # actual tweet insertion
    tweets_coll.insert(tweet_obj_array)

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

        today_str = time.strftime('%Y-%m-%d')

        hour = time.strftime('%H')
        minute = "%04d" % (int(hour)*60 + int(time.strftime('%M')))

        for key in freq:
            for item in freq[key].keys():
                count = freq[key][item]
                colls[key].update({'campaign_id': campaign_id, 'date': today_str, 'key': item},
                                  {'$inc': {('hour.%s' % hour): count, ('minute.%s' % minute): count, ('day_total'): count}}, upsert=True)
