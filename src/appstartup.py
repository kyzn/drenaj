from config import *

import tornado.ioloop
import tornado.web

from mainhandler import MainHandler
from followerhandler import FollowerHandler
from schedulerMainHandler import SchedulerMainHandler

def start():
    application = tornado.web.Application([
        (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
        (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
        ])
    print "Direnaj Service Layer Starting on port %s" % DIRENAJ_APP_PORT
    application.listen(DIRENAJ_APP_PORT)
    tornado.ioloop.IOLoop.instance().start()
