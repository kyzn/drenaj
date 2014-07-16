'''
    This file contains the definition of the direnaj gateway and scheduler interface
    accessible via http requests

'''

from direnaj_api.handlers.followerhandler import *
from direnaj_api.handlers.statuseshandler import *
from direnaj_api.handlers.userprofileshandler import *
from direnaj_api.handlers.campaignshandler import *
from direnaj_api.handlers.taskshandler import *

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

    (r"/profiles/(store)", UserProfilesHandler),
    (r"/profiles/(view)/(campaign|user)", UserProfilesHandler),

    (r"/campaigns/(new|list|filter|histograms)", CampaignsHandler),
    (r"/campaigns/(view)/?(freqs|histograms|watched_users)?", CampaignsHandler),


    (r"/tasks/(harvest|crawl)", TasksHandler),
]
