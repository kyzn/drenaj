from config import *

import tornado.ioloop
import tornado.web
from tornado.httpserver import HTTPServer

from followerhandler import *
from schedulerMainHandler import SchedulerMainHandler
from schedulerMainHandler import SchedulerReportHandler

routes_config = [
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
]

application = tornado.web.Application(routes_config)

def bind_server(environment):
    http_server = HTTPServer(application)
    http_server.listen(DIRENAJ_APP_PORT[environment])
    print 'oldu mu simdi?'

def start(environment):
    print "Direnaj Service Layer Starting on port %s" % DIRENAJ_APP_PORT[environment]
    bind_server(environment)
    tornado.ioloop.IOLoop.instance().start()
    return application

if __name__ == "__main__":
    start(DIRENAJ_APP_ENVIRONMENT)
