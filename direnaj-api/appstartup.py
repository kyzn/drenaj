from config.config import *
from config.direnaj_routes_config import routes_config

import tornado.ioloop
import tornado.web
from tornado.httpserver import HTTPServer

application = tornado.web.Application(routes_config)

def bind_server(environment):
    http_server = HTTPServer(application, xheaders=True)
    http_server.listen(DIRENAJ_APP_PORT[environment])

def start(environment):
    print "Direnaj Service Layer Starting on port %s" % DIRENAJ_APP_PORT[environment]
    bind_server(environment)
    tornado.ioloop.IOLoop.instance().start()
    return application

if __name__ == "__main__":
    start(DIRENAJ_APP_ENVIRONMENT)
