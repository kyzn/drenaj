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
import argparse

from direnaj_api.config.config import *

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='direnaj server')
    parser.add_argument('command', help='used for serving or initializing a database (only for dev)')
    args = parser.parse_args()

    if args.command == 'init_db':
        import direnaj_api.utils.direnajinitdb as direnajinitdb
        # TODO: using this dumb trick, because I don't want to change the commandline arguments now.
        if True:
            print "WARNING WARNING WARNING: all collections will be RESET!"
            print "Are you sure? (y/n)"
            answer = raw_input().strip()
            if answer == 'y':
                print "AGAIN: Are you sure? (y/n)"
                answer = raw_input().strip()
                if answer == 'y':
                    direnajinitdb.restore_db('test', DIRENAJ_APP_ENVIRONMENT, DB_TEST_VERSION)
                    direnajinitdb.drop_all_collections(DIRENAJ_APP_ENVIRONMENT)
        else:
            print "WARNING WARNING WARNING: all collections will be RESET!"
            print "Are you sure? (y/n)"
            answer = raw_input().strip()
            if answer == 'y':
                print "AGAIN: Are you sure? (y/n)"
                answer = raw_input().strip()
                if answer == 'y':
                    direnajinitdb.drop_all_collections(DIRENAJ_APP_ENVIRONMENT)
                    direnajinitdb.init_graphs(environment=DIRENAJ_APP_ENVIRONMENT, method='randomly')
                    direnajinitdb.init_users(environment=DIRENAJ_APP_ENVIRONMENT, method='randomly')
    elif args.command == 'dump_db':
        import direnaj_api.utils.direnajinitdb as direnajinitdb
        direnajinitdb.dump_db(DIRENAJ_APP_ENVIRONMENT, DB_TEST_VERSION)
    elif args.command == 'runserver':
        import direnaj_api.appstartup as appstartup
        appstartup.start(DIRENAJ_APP_ENVIRONMENT)
    else:
        print "Unrecognized command.."
        import sys
        sys.exit(1)