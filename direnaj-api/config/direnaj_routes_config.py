'''
    This file contains the definition of the direnaj gateway and scheduler interface
    accessible via http requests

'''

from handlers.followerhandler import *
from handlers.statuseshandler import *
from handlers.userhandler import *
from handlers.campaignshandler import *

from handlers.schedulerMainHandler import SchedulerMainHandler
from handlers.schedulerMainHandler import SchedulerReportHandler
from handlers.schedulerMainHandler import SchedulerProfilesHandler


routes_config = [
    (r"/scheduler/suggestUseridToGet_profiles", SchedulerProfilesHandler),
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
    (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
#    (r"/user/(store|view)", UserSingleProfileHandler),
    (r"/profiles/(store|view)", UserProfilesHandler),
    (r"/campaigns/(new|list|view|view_freqs|filter|histograms)", CampaignsHandler),
]
