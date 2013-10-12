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

#  (r"/(friends|followers)/(crawl|view)", visFollowerHandler),

class visFollowerHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """ 
        
        """

        (friends_or_followers, crawl_or_view) = args

        print "visFollowerHandler: {} {}".format(friends_or_followers, crawl_or_view)

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


app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])
vis_root_url = 'http://' + DIRENAJ_VIS_HOST + ':' + str(DIRENAJ_VIS_PORT[DIRENAJ_VIS_ENVIRONMENT])
        
        
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
        post_response = requests.post(url=app_root_url + '/user/view', data=post_data)

        dat = json_decode(post_response.content)
        
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
        user_id = self.get_argument('user_id', None)
#        auth_user_id = self.get_argument('auth_user_id','direnaj')
        lim_count = self.get_argument('limit', None)
        
        # post_data = {"user_id": root}
        # post_response = requests.post(url=app_root_url + '/profiles/view', data=post_data)
        
        tmp = requests.post(url=app_root_url + '/profiles/view')
        
        #print tmp
        
        dat = json_decode(tmp.content)
        
        env = Environment(loader=FileSystemLoader('templates'))
        template = env.get_template('profiles/view.html')
        result = template.render(profiles=dat, len=len(dat), href=vis_root_url)

        self.write(result)
