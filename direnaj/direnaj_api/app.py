"""Main application for managing Direnaj API application.

This application provides set of command-line arguments for starting the application, dumping database, initial
database generation, etc. You can get help on command-line arguments by specifying ./app.py --help.

positional arguments:
  {runserver,dumpdb}
    runserver         run direnaj server
    dumpdb            dump direnaj database

"""

__author__ = 'Eren Turkay <turkay.eren@gmail.com>'
__version__ = 0.1

# Note for PyCharm users. We use os module just below but somehow PyCharm reports
# that it is unneeded. Please do not remove it.
import os
import sys

# We need to make sure that 'direnaj' source directory is in the first path to look. Otherwise
# handlers cannot import utils.drnj_time because direnaj_api directory also has utils/ package. Since
# this app.py is in direnaj_api directory, python interpreter first looks whether utils are present or not
# within this directory. Utils/ exist but drnj_time does not, and it fails :(
#
# FIXME: Fix the naming of the utils/ directory and modules. Take a look at what is needed and organize the project.
#
# -- Eren, 16.07.2014
APP_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.insert(0, APP_ROOT)

import tornado.web
from tornado.ioloop import IOLoop
from tornado.httpserver import HTTPServer

import logging
import logging.handlers
from tornado.log import LogFormatter

from direnaj_api.handlers.followerhandler import *
from direnaj_api.handlers.statuseshandler import *
from direnaj_api.handlers.userprofileshandler import *
from direnaj_api.handlers.campaignshandler import *
from direnaj_api.handlers.taskshandler import *

from direnaj_api.handlers.schedulerMainHandler import SchedulerMainHandler
from direnaj_api.handlers.schedulerMainHandler import SchedulerReportHandler
from direnaj_api.handlers.schedulerMainHandler import SchedulerProfilesHandler

from direnaj_api.utils.direnajmongomanager_new import DirenajMongoManager

logger = logging.getLogger()


class Application(tornado.web.Application):
    def __init__(self, **overrides):
        """Initialization of Direnaj API application.

        This class defines handlers and settings, then initializes tornado.web.application. To override a setting of
        the application, provide related keys when initializing the Application() class. For example, to start this
        application with overridden database setting, you can use the following::

            http_server = tornado.httpserver.HTTPServer(Application(database='production', debug=False))
            http_server.listen(options.port)
            tornado.ioloop.IOLoop.instance().start()

        :param overrides: key=value argument list for settings dictionary in the class.
        :return: Application() instance
        """
        if not overrides:
            overrides = {}

        handlers = [
            (r"/scheduler/suggestUseridToGet_profiles", SchedulerProfilesHandler),
            (r"/scheduler/suggestUseridToGet_(friends|followers)", SchedulerMainHandler),
            (r"/scheduler/reportProtectedUserid", SchedulerReportHandler),

            (r"/(friends|followers)/(ids|list)/?(store|view)?", FollowerHandler),

            (r"/statuses/(store|view|filter|retweets)", StatusesHandler),
            #(r"/user/(store|view)", UserSingleProfileHandler),

            (r"/profiles/(store)", UserProfilesHandler),
            (r"/profiles/(view)/(campaign|user)", UserProfilesHandler),

            (r"/campaigns/(new|list|filter|histograms)", CampaignsHandler),
            (r"/campaigns/(view)/?(freqs|histograms|watched_users)?", CampaignsHandler),

            (r"/tasks/(harvest|crawl)", TasksHandler),
        ]

        # default settings for the application
        settings = {
            'debug': True,
            'database': 'direnaj_test',
            'mongo_host': 'localhost',
            'mongo_port': 27017,
            'log_file_prefix': "tornado.log",
        }

        # override any available settings, or add a setting to the application.
        settings.update(overrides)

        # initialize direnaj database
        self.db = DirenajMongoManager(
            settings.get('mongo_host'),
            settings.get('mongo_port'),
            settings.get('database')
        )

        tornado.web.Application.__init__(self, handlers, **settings)


class CommandHandler(object):
    @staticmethod
    def runserver(opts):
        """Command handler for runserver

        This creates a tornado application instance and runs them. The settings for the application
        are defined in the argument parser. Look for runserver parser for the available options

        :param opts: options defined for this command. This is passed by argparse module
        :type opts: Namespace() class from argparse module
        :return: None
        :rtype: None
        """
        logger.info('Starting Direnaj API on port {0} using database {1} (mongo host: {2}, port: {3}). DEBUG: {4}'.format(
            opts.port,
            opts.database,
            opts.mongo_host,
            opts.mongo_port,
            opts.debug
        ))

        app = Application(debug=opts.debug,
                          database=opts.database,
                          mongo_host=opts.mongo_host,
                          mongo_port=opts.mongo_port)

        http_server = HTTPServer(app)
        http_server.listen(opts.port)
        IOLoop.instance().start()

    @staticmethod
    def dumpdb(opts):
        print '{0} NOT IMPLEMENTED YET'.format(opts.command)
        sys.exit(1)


def create_argument_parser():
    import argparse

    main_description = """
    Manage Direnaj API server.

    You can get detailed help on the positional arguments (subcommands) by providing "argument --help"
    """
    main_parser = argparse.ArgumentParser(description=main_description, add_help=False)

    # Base parser which is a parent of the subparsers. This parser defines debug option
    # which is common for all subparsers.
    base_parser = argparse.ArgumentParser(add_help=False)
    base_parser.add_argument('--debug', action='store_true', help='set logging verbosity to debug')
    base_parser.add_argument('-v', '--verbosity', action='store',
                             help='set verbosity in logging module',
                             choices=['info', 'warning', 'debug'],
                             default='info')

    subparsers = main_parser.add_subparsers(dest='command')

    parser_runserver = subparsers.add_parser('runserver', description='Start Direnaj API Tornado application',
                                             formatter_class=argparse.ArgumentDefaultsHelpFormatter,
                                             help='run direnaj server',
                                             parents=[base_parser])
    parser_runserver.add_argument('-p', '--port', action='store', type=int, default=9999,
                                  help='set direnaj api http port to listen')
    parser_runserver.add_argument('-d', '--database', action='store', type=str, default='direnaj_prod',
                                  help='set which mongodb database to use')
    parser_runserver.add_argument('-m', '--mongo-host', action='store', type=str, default='localhost',
                                  help='set which mongodb host to connect')
    parser_runserver.add_argument('-t', '--mongo-port', action='store', type=int, default=27017,
                                  help='set which mongodb port to connect')
    parser_runserver.set_defaults(function=CommandHandler.runserver)

    parser_dumpdb = subparsers.add_parser('dumpdb', description='Dump Direnaj database',
                                          formatter_class=argparse.ArgumentDefaultsHelpFormatter,
                                          help='dump direnaj database',
                                          parents=[base_parser])
    parser_dumpdb.add_argument('-f', '--file', action='store', type=str,
                               help='set file to write the dump. if not provided, stdout is used')
    parser_dumpdb.set_defaults(function=CommandHandler.dumpdb)

    return main_parser

def enable_logging(level):
    channel = logging.StreamHandler()
    channel.setFormatter(LogFormatter(color=True))

    logger.addHandler(channel)
    logger.setLevel(getattr(logging, level.upper()))

if __name__ == '__main__':
    parser = create_argument_parser()

    if len(sys.argv) == 1:
        parser.print_help()
        sys.exit(0)

    # We define sub parsers and these sub parsers include -h/--help option by default. When we enable
    # help for top-level parser, we get error. Thus, just have a little hack for the first parameter of the
    # script to print the help information.
    elif len(sys.argv) == 2 and (sys.argv[1] == '-h' or sys.argv[1] == '--help'):
        parser.print_help()
        sys.exit(0)

    # Parse the arguments and dispatch them to their corresponding handler. The class passed to the
    # functions is Namespace() from argparse.
    args = parser.parse_args()

    if args.verbosity == 'debug':
        args.debug = True

    enable_logging(args.verbosity)
    args.function(args)