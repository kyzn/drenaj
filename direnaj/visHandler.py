'''
visHandler

Contains the visualization handlers

'''

# Change History :
# Date                                          Prog    Note
# Thu Oct 10 10:09:20 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr



from config import *
import drnj_time

from direnajmongomanager import *
from direnaj_collection_templates import *

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.escape import json_decode,json_encode

import json
import time
import requests

import bson.json_util
from jinja2 import Environment, FileSystemLoader

import hashlib
import tweepy

app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])
vis_root_url = 'http://' + DIRENAJ_VIS_HOST + ':' + str(DIRENAJ_VIS_PORT[DIRENAJ_VIS_ENVIRONMENT])

db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
accounts = db['accounts']
keystore = KeyStore()

def get_api(user):
    auth = tweepy.OAuthHandler(keystore.app_consumer_key, keystore.app_consumer_secret)
    auth.set_access_token(user['creds_AccessToken'], user['creds_AccessTokenSecret'])
    api = tweepy.API(auth)

    #print "------------------------"
    #print "get api for user ", user
    #print api
    #print user['creds_AccessToken'], user['creds_AccessTokenSecret']
    #print "------------------------"

    return api


from twitter_api_getfollowers import drnj_graph_crawler

#  (r"/(friends|followers)/(crawl|view)", visFollowerHandler),

class visFollowerHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (friends_or_followers, crawl_or_view) = args

        user = accounts.find_one( {'username': self.get_secure_cookie('tai_user')} )
        if user:
            access_token_key = user['creds_AccessToken']
            access_token_secret = user['creds_AccessTokenSecret']
        else:
            access_token_key = keystore.access_token_key
            access_token_secret = keystore.access_token_secret

        print "visFollowerHandler: {} {}".format(friends_or_followers, crawl_or_view)
        if crawl_or_view=='crawl':
            user_id = self.get_argument('user_id', None)
            res = drnj_graph_crawler(friends_or_followers, int(user_id), access_token_key, access_token_secret)
            env = Environment(loader=FileSystemLoader('templates'))
            template = env.get_template('profiles/crawl_graph_notification.html')
            result = template.render(user_id=user_id, fof=friends_or_followers, res=res, href=vis_root_url)

            self.write(result)

        else:
            pass

#        auth_user_id = self.get_argument('auth_user_id')

#    (r"/statuses/(crawl|view)", visStatusesHandler),
class visStatusesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visStatusesHandler: {} ".format(crawl_or_view)




#    (r"/user/(crawl|view)", visSingleProfileHandler),
class visSingleProfileHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visSingleProfileHandler: {} ".format(crawl_or_view)

        user_id = self.get_argument('user_id', None)
        post_data = {"user_id": user_id}
        post_response = requests.post(url=app_root_url + '/profiles/view', data=post_data)

        #dat = json_decode(post_response.content)
        dat = bson.json_util.loads(post_response.content)
        print dat

        env = Environment(loader=FileSystemLoader('templates'))
        template = env.get_template('profiles/history_view.html')
        result = template.render(profiles=dat)

        self.write(result)

#    (r"/profiles/(crawl|view)", visUserProfilesHandler),
class visUserProfilesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visUserProfilesHandler: {} ".format(crawl_or_view)

        user = accounts.find_one( {'username': self.get_secure_cookie('tai_user')} )
        if user:
            access_token_key = user['creds_AccessToken']
            #access_token_secret = user['creds_AccessTokenSecret']

        if user and 'creds_AccessToken' in user and user['creds_AccessToken']:
            oauth_complete = True
            rem_req_num = get_api(user).rate_limit_status()['resources']['friends']['/friends/ids']['remaining']
        else:
            oauth_complete = False
            rem_req_num = -1
            access_token_key = 'shared'

        if crawl_or_view=='crawl':
            pass
        else:
            user_id = self.get_argument('user_id', None)
            # auth_user_id = self.get_argument('auth_user_id','direnaj')
            lim_count = self.get_argument('limit', None)
            if lim_count is None:
                lim_count = 20
            # post_data = {"user_id": root}
            # post_response = requests.post(url=app_root_url + '/profiles/view', data=post_data)
            dat = {"limit": lim_count}

            tmp = requests.post(url=app_root_url + '/profiles/view',data=dat)

            #print tmp

            dat = json_decode(tmp.content)

            env = Environment(loader=FileSystemLoader('templates'))
            template = env.get_template('profiles/view.html')
            result = template.render(profiles=dat, len=len(dat), href=vis_root_url,
                                     username = self.get_secure_cookie('tai_user'),
                                     oauth_complete = oauth_complete,
                                     rem_req_num = rem_req_num,
                                     access_token_key = access_token_key)

            self.write(result)
