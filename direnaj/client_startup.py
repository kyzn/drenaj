import tornado.ioloop, tornado.web
from tornado.httpserver import HTTPServer

import os

from client.config.config import *
from client.frontend.routes_config import routes_config

from client.celery_app.endpoint import app_object

application = tornado.web.Application(routes_config,
                                      cookie_secret = 'vospRVBgTF6HTnghpd/za+UgiZ/NXUDUkTnYGx1d4hY=')

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
    os.system("celery multi start worker1 -")

