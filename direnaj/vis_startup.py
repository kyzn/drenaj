from config import *
from direnaj_routes_config import vis_routes_config

import tornado.ioloop
import tornado.web
from tornado.httpserver import HTTPServer

application = tornado.web.Application(vis_routes_config)

def bind_server(environment):
    http_server = HTTPServer(application, xheaders=True)
    http_server.listen(DIRENAJ_VIS_PORT[environment])

def start(environment):
    print "Direnaj Local Visualization and Interaction Manager Starting on port %s" % DIRENAJ_VIS_PORT[environment]
    bind_server(environment)
    tornado.ioloop.IOLoop.instance().start()
    return application

if __name__ == "__main__":
    start(DIRENAJ_VIS_ENVIRONMENT)
