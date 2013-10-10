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

import bson.json_util

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

