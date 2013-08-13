from config import *

import tornado.ioloop
import tornado.web

from mainhandler import MainHandler
from followerhandler import FollowerHandler
from subhandler import SubHandler

def start():
    application = tornado.web.Application([
        (r"/store", MainHandler),
        (r"/get_decrepit_users", MainHandler),
        (r"/sub", SubHandler),
        (r"/sub_post", SubHandler),
        (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
        ])
    print "Direnaj Service Layer Started"
    application.listen(DIRENAJ_APP_PORT)
    tornado.ioloop.IOLoop.instance().start()
