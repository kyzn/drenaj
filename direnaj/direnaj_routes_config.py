'''
    This file contains the definition of the direnaj gateway and scheduler interface
    accessible via http requests

'''

from followerhandler import *
from statuseshandler import *
from userhandler import *

from schedulerMainHandler import SchedulerMainHandler
from schedulerMainHandler import SchedulerReportHandler
from schedulerMainHandler import SchedulerProfilesHandler

from oauthHandler import OAuthHandler
from oauthHandler import SigninHandler
from oauthHandler import SignupHandler
from oauthHandler import SignoutHandler

routes_config = [
    (r"/scheduler/suggestUseridToGet_profiles", SchedulerProfilesHandler),
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
    (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
    (r"/user/(store|view)", UserSingleProfileHandler),
    (r"/profiles/(store|view)", UserProfilesHandler),
]

from visHandler import *


vis_routes_config = [
    (r"/(friends|followers)/(crawl|view)", visFollowerHandler),
    (r"/statuses/(crawl|view)", visStatusesHandler),
    (r"/user/(crawl|view)", visSingleProfileHandler),
    (r"/profiles/(crawl|view)", visUserProfilesHandler),

    (r'/sign_in', SigninHandler),
    (r'/sign_up', SignupHandler),
    (r'/sign_out', SignoutHandler),
    (r'/start_oauth', OAuthHandler),
    (r"/oauth/callback", OAuthHandler),
]
