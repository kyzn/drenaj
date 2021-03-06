import random

import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.escape import json_encode


class SchedulerProfilesHandler(tornado.web.RequestHandler):
    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        self.set_header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
        self.set_header('Access-Control-Allow-Headers',
                        'Origin, X-Requested-With, Content-Type, Accept')

    @tornado.web.asynchronous
    def get(self, *args):
        '''
        deneme

        :param args:
        :return:
        '''

        (num) = args

        queue_collection = self.application.db.motor_column.queue

        print num
        # if N>100:
        N = 100
        # TODO: Needs redesign, current strategy too naive

        cur = queue_collection.find({"protected": False}).sort('profile_retrieved_at', 1).limit(N)

        a = []
        for c in (yield cur.to_list(length=100)):
            a.append(c["id_str"])
            # print c

        #        idx = random.randint(0, N-1)
        if len(a) == 0:
            self.write(json_encode(0))
        else:
            self.write(json_encode(a))
            print a

    def post(self, *args):
        '''
        post deneme

        :param args:
        :return:
        '''
        self.write("Not Implemented")


class SchedulerMainHandler(tornado.web.RequestHandler):
    @tornado.web.asynchronous
    def get(self, *args):
        '''
        deneme 2
        :param args:
        :return:
        '''

        (friends_or_followers) = args

        queue_collection = self.application.db.motor_column.queue

        # Get the N'th most frequent visited one
        N = 100

        if friends_or_followers == 'followers':
            cur = queue_collection.find({"protected": {"$ne": True}}).sort('followers_retrieved_at',
                                                                           -1).limit(N)
        else:
            cur = queue_collection.find({"protected": {"$ne": True}}).sort('friends_retrieved_at',
                                                                           -1).limit(N)

        a = []
        for c in (yield cur.to_list(length=100)):
            a.append(c["id"])
            # print c

        idx = random.randint(0, N - 1)
        # TODO: Needs redesign, current strategy too naive
        if len(a) == 0:
            self.write(json_encode(0))
            print "Suggest ToGet %s : User %d " % (friends_or_followers, 0)
        else:
            self.write(json_encode(a[idx]))
            print "Suggest ToGet %s : User %d " % (friends_or_followers, a[-1])

    def post(self, *args):
        self.write("Not Implemented")


class SchedulerReportHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.write("Not Implemented")

    # @drenaj_simple_auth
    @tornado.web.asynchronous
    def post(self, *args, **kwargs):
        user_id = int(self.get_argument('user_id'))
        isProtected = int(self.get_argument('isProtected'))

        print "Protected or Disabled Twitter user account: %d" % int(user_id)
        markProtected(self.application.db.motor_column.queue, user_id, isProtected,
                      kwargs["drnjID"])


@gen.coroutine
def markProtected(queue_collection, user_id, isProtected, drnjID):
    queue_query = {"id": int(user_id)}
    queue_document = {"$set":
        {
            "protected": bool(isProtected),
            "retrieved_by": drnjID}
    }
    # print queue_query, queue_document
    yield queue_collection.update(queue_query, queue_document, upsert=True)
