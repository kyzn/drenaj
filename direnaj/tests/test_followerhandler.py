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

class TestBucketHandler(TestHandlerBase):
    def test_deneme(self):

        import urllib

        print self.get_http_port()
        # Example on how to hit a particular handler as POST request.
        post_args = {'user_id': '461494372'}
        response = self.fetch(
                            '/followers/ids',
                            method='POST',
                            body=urllib.urlencode(post_args),
                            follow_redirects=False)

        # http://localhost:9999/followers/ids/view?user_id=461494372

        # response:
        expected_response_obj = bson.json_util.loads('''{
                "results": [
                    {
                        "friend_id_str": "461494372",
                        "retrieved_by": "dummy_data",
                        "record_retrieved_at": "Thu May 30 14:20:16 +0000 2013",
                        "id_str": "461494360",
                        "following": 1,
                        "_id": {
                            "$oid": "520d2f206ea62501d9c64ac9"
                            }
                        }
                    ]
                }''')

        print response.body
        obj = bson.json_util.loads(response.body)

        self.maxDiff = None
        self.assertEqual(obj, expected_response_obj)
#
## http://localhost:9999/followers/list/view?user_id=461494372
