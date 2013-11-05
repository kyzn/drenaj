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

routes_config = [
    (r"/scheduler/suggestUseridToGet_profiles", SchedulerProfilesHandler),
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
    (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
#    (r"/user/(store|view)", UserSingleProfileHandler),
    (r"/profiles/(store|view)", UserProfilesHandler),
]

