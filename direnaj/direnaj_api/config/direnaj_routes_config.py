'''
    This file contains the definition of the direnaj gateway and scheduler interface
    accessible via http requests

'''

from direnaj_api.handlers.followerhandler import *
from direnaj_api.handlers.statuseshandler import *
from direnaj_api.handlers.userhandler import *
from direnaj_api.handlers.campaignshandler import *

from direnaj_api.handlers.schedulerMainHandler import SchedulerMainHandler
from direnaj_api.handlers.schedulerMainHandler import SchedulerReportHandler
from direnaj_api.handlers.schedulerMainHandler import SchedulerProfilesHandler


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
