###############################################################################
###############################################################################
###                                                                         ###
###    Direnaj Project!                                                     ###
###                                                                         ###
###                   copyright Bogazici University                         ###
###                                                                         ###
###                                                                         ###
###############################################################################
###############################################################################

# this module contains the configuration parameters
from config import *

import argparse

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='direnaj server')
    parser.add_argument('command', help='used for serving or initializing a database (only for dev)')
    args = parser.parse_args()

    if args.command == 'init_db':
        import direnajinitdb
        print "WARNING WARNING WARNING: all collections will be RESET!"
        print "Are you sure? (y/n)"
        answer = raw_input().strip()
        if answer == 'y':
            print "AGAIN: Are you sure? (y/n)"
            answer = raw_input().strip()
            if answer == 'y':
                direnajinitdb.drop_all_collections()
        direnajinitdb.init_graphs(method='randomly')
        direnajinitdb.init_users(method='randomly')
    elif args.command == 'runserver':
        import appstartup
        appstartup.start()
