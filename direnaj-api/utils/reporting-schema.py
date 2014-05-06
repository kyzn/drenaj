import sys, time

from direnaj.config import *
from direnaj.drnj_time import py_utc_time2drnj_time, drnj_time2py_time

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

campaign_name = sys.argv[1]

if len(sys.argv) > 2:
    import matplotlib
    import matplotlib.pyplot as plot
    plotGraphs = True
    # matplotlib.rc('font', size=6)
    # plot.figure(num=None, figsize=(8, 6), dpi=80, facecolor='w', edgecolor='k')
else:
    plotGraphs = False

#######
import numpy
campaign_query = {'campaign_id': campaign_name}

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

tmp = tweets_coll.aggregate(pipeline)
users = tmp['result']

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

# How many tweets?
n_tweets = tweets_coll.find(campaign_query).count()
print "How many tweets? %d" % n_tweets
hist['n_tweets'] = n_tweets
#
# How many unique users?
#
n_unique_users = len(users)
print "How many unique users? %d" % n_unique_users
hist['n_unique_users'] = n_unique_users

######
sec_title = "Histogram of user creation dates?"
#

tmp_dates = [py_utc_time2drnj_time(x['user']['created_at']) for x in users]

(hist['user_creation']['data'], hist['user_creation']['bins']) = numpy.histogram(tmp_dates, bins=100)

if plotGraphs:
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
(hist['user_n_tweets']['data'], hist['user_n_tweets']['bins']) = numpy.histogram(tmp_counts, bins=100)

if plotGraphs:
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
    if u['user']['default_profile_image'] == True:
        n_default_profile_image += 1

hist['n_default_profile_image'] = n_default_profile_image
print "%s: %0.2f%%" % (sec_title, 100*(float(n_default_profile_image)/n_unique_users))
#####
sec_title = "Histogram of tweet counts of unique users"
tmp_counts = [int(x['user']['statuses_count']) for x in users]
#
(hist['user_n_tweets_overall']['data'], hist['user_n_tweets_overall']['bins']) = numpy.histogram(tmp_counts, bins=100)

if plotGraphs:
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
print "%s: %0.2f%%" % (sec_title, 100*(float(n_lower_than_threshold)/n_unique_users))

print hist
#####
#
# Fetch the profile images of each unique user. Calculate the average color.
# Form a new image u_1 of one pixel with this color. Then use these u_x to form
# a final histogram.
#
# Treat mentions and retweets as the same and form an edge for each one of them.
# 1. Plot this graph.
# 2. Take a basic algorithm and calculate the clusterings in it.
#
