from tornado.testing import AsyncHTTPTestCase

from direnaj.config import *
from direnaj.appstartup import application

import direnaj.direnajinitdb

def clear_db():
    direnaj.direnajinitdb.restore_db('test', 'test', DB_TEST_VERSION)

# Create your base Test class.
# Put all of your testing methods here.
class TestHandlerBase(AsyncHTTPTestCase):

    def setUp(self):
        # start('test')
        clear_db()
        super(TestHandlerBase, self).setUp()

    def get_app(self):
        return application      # this is the global app that we created above.

    #def get_http_port(self):
    #    return DIRENAJ_APP_PORT['test']
