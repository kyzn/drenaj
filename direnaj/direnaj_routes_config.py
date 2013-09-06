'''
    This file contains the definition of the direnaj gateway and schedulre interface
    accessible via http requests

'''

from homepagehandler import *
from followerhandler import *
from statuseshandler import *
from userhandler import *

from schedulerMainHandler import SchedulerMainHandler
from schedulerMainHandler import SchedulerReportHandler

routes_config = [
    (r"/", HomepageHandler),
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
    (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
    (r"/user/(store|view)", UserSingleProfileHandler),
]
