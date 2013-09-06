''' 
    This file contains the definition of the direnaj gateway and schedulre interface
    accessible via http requests

''' 

# Change History :
# Date                                          Prog    Note
# Fri Sep 06 01:16:11 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

from followerhandler import *
from statuseshandler import *
from userhandler import *

from schedulerMainHandler import SchedulerMainHandler
from schedulerMainHandler import SchedulerReportHandler

routes_config = [
    (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
    (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),
    (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),
    (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
    (r"/user/(store|view)", UserSingleProfileHandler),
]
