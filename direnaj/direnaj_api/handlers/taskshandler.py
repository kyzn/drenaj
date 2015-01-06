import tornado

__author__ = 'onur'

from tornado.web import MissingArgumentError, HTTPError

from tornado import gen

import bson.json_util

from direnaj_api.celery_app.server_endpoint import app_object

class TasksHandler(tornado.web.RequestHandler):

    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    #@direnaj_simple_auth
    @tornado.web.asynchronous
    @gen.coroutine
    def post(self, *args, **keywords):

        task_type = args[0]

        try:
            task_definition = bson.json_util.loads(self.get_argument('task_definition'))
            queue = self.get_argument('queue')
            metadata = task_definition['metadata']
            user = self.application.db.get_user_from_watchlist(metadata['user'])
            print user
            if task_type == 'harvest':
                res = app_object.send_task('timeline_retrieve_userlist',
                                           [[ [user['user'], user['since_tweet_id'], user['page_not_found']] ]],
                                           queue=queue)
            elif task_type == 'crawl':
                res = app_object.send_task('crawl_friends_or_followers',[[ [user['user'], user['page_not_found']] ]], queue=queue)
            else:
                raise MissingArgumentError('harvest or crawl')

            self.write(bson.json_util.dumps({'result': 'successful', 'message': res.task_id}))
            self.add_header('Content-Type', 'application/json')

        except MissingArgumentError as e:
            raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)