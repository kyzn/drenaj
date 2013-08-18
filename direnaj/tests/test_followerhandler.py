


import pytest

import tornado.testing
from tornado.testing import AsyncHTTPTestCase

import bson.json_util

print __package__

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

# They are runnable via nosetests as well.
class TestBucketHandler(TestHandlerBase):
    def test_deneme(self):

        import urllib

        print self.get_http_port()
        # Example on how to hit a particular handler as POST request.
        # In this example, we want to test the redirect,
        # thus follow_redirects is set to False
        post_args = {'user_id': '461494372'}
        response = self.fetch(
                            '/followers/ids',
                            method='POST',
                            body=urllib.urlencode(post_args),
                            follow_redirects=False)


# http://localhost:9999/followers/ids/view?user_id=461494372

# response:
# TODO: find a way to represent string in multilines! -onurgu
        expected_response_obj = bson.json_util.loads(u'{"results": [  {                        "friend_id_str": "461494372",                        "retrieved_by": "dummy_data",                        "record_retrieved_at": "Thu May 30 14:20:16 +0000 2013",                        "id_str": "461494360",                        "following": 1,                        "_id": {                            "$oid": "520d2f206ea62501d9c64ac9"                            }                        }                    ]                }')

        print response.body
        obj = bson.json_util.loads(response.body)

        self.maxDiff = None
        self.assertEqual(obj, expected_response_obj)
        # On successful, response is expected to redirect to /tutorial
        # self.assertEqual(response.code, 302)
        #self.assertTrue(response.headers['Location'].endswith('/tutorial'),
        #                "response.headers['Location'] did not ends with /tutorial")

# Apparently needed in this module for testing.main()
#def all():
#    return TestBucketHandler('test_deneme')
#
#if __name__ == '__main__':
#    tornado.testing.main()

#@pytest.fixture(scope="class")
#def db_class(request):
#    from .. import direnajinitdb
#    class DummyDB:
#        pass
#    # set a class attribute on the invoking test context
#    request.cls.db = DummyDB()
#
#@pytest.mark.usefixtures("db_class")
#class FollowerHandlerTest(unittest.TestCase):
#    from .. import FollowerHandler
#    def test_method1(self):
#        fh = FollowerHandler()
#        fh.post
#        assert hasattr(self, "db")
#        assert 0, self.db   # fail for demo purposes
#
#    def test_method2(self):
#        assert 0, self.db   # fail for demo purposes
#
## http://localhost:9999/followers/list/view?user_id=461494372
