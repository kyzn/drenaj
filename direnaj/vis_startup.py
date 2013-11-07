from config import *

import tornado.ioloop
import tornado.web
from tornado.httpserver import HTTPServer

import visHandler

from oauthHandler import OAuthHandler
from oauthHandler import SigninHandler
from oauthHandler import SignupHandler
from oauthHandler import SignoutHandler

vis_routes_config = [
    (r"/(friends|followers)/(crawl|view)", visHandler.visFollowerHandler),
    (r"/statuses/(crawl|view)", visHandler.visStatusesHandler),
    (r"/user/(crawl|view)", visHandler.visSingleProfileHandler),
    (r"/profiles/(crawl|view)", visHandler.visUserProfilesHandler),

    (r'/sign_in', SigninHandler),
    (r'/sign_up', SignupHandler),
    (r'/sign_out', SignoutHandler),
    (r'/start_oauth', OAuthHandler),
    (r"/oauth/callback", OAuthHandler),
]

application = tornado.web.Application(vis_routes_config,
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
