from config import *

import tornado.ioloop
import tornado.web

from followerhandler import FollowerHandler
from schedulerMainHandler import SchedulerMainHandler
from schedulerMainHandler import SchedulerReportHandler

def start():
    application = tornado.web.Application([
        (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
        (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
        (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
        ])
    print "Direnaj Service Layer Started"
    application.listen(DIRENAJ_APP_PORT)
    tornado.ioloop.IOLoop.instance().start()
