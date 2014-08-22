__author__ = 'Eren Turkay <turkay.eren@gmail.com>'

import os

from tornado.ioloop import IOLoop
from tornado.testing import AsyncHTTPTestCase

from ..app import Application

# relative to direnaj_api/ directory. Tests should be called in that dir.
SAMPLE_DIRECTORY = "tests/sample/direnaj_test"

app = Application(debug=False, database='direnaj_test')


class BaseTestHandler(AsyncHTTPTestCase):
    def get_app(self):
        return app

    def get_new_ioloop(self):
        return IOLoop.instance()

    @classmethod
    def __reload_database(cls):
        # Reloading the database can be done using pure PyMongo library but mongorestore binary
        # is used for easy reloading. This is a little hack but can be forgiven. We know that
        # mongodb is present in the system.
        setting = app.settings
        cmd = "mongorestore -h {} -p {} --db {} --drop {} > /dev/null"
        os.system(cmd.format(setting['mongo_host'],
                             setting['mongo_port'],
                             setting['database'],
                             SAMPLE_DIRECTORY))

    @classmethod
    def setUpClass(cls):
        print '\nSTARTING TEST: %s' % cls.__name__
        cls.__reload_database()

    @classmethod
    def tearDownClass(cls):
        print '\nENDING TEST: %s' % cls.__name__
        # cls.__reload_database()
