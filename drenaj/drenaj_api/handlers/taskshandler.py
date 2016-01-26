import bson.json_util
import tornado
from tornado import gen
from tornado.web import MissingArgumentError, HTTPError

from drenaj_api.celery_app.server_endpoint import app_object

__author__ = 'onur'


class TasksHandler(tornado.web.RequestHandler):
    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        self.set_header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
        self.set_header('Access-Control-Allow-Headers',
                        'Origin, X-Requested-With, Content-Type, Accept')

    def get(self, *args):
        self.post(*args)
        # self.write("not implemented yet")

    # @drenaj_simple_auth
    @tornado.web.asynchronous
    @gen.coroutine
    def post(self, *args, **keywords):

        task_type = args[0]

        try:
            from drenaj_api.utils.drenajneo4jmanager import init_user_to_graph_aux, upsert_campaign

            task_definition = bson.json_util.loads(self.get_argument('task_definition'))
            queue = self.get_argument('queue')
            metadata = task_definition['metadata']
            campaign_id = self.get_argument('campaign_id', 'default')
            campaign_node = upsert_campaign(campaign_id)
            user = init_user_to_graph_aux(campaign_node, metadata['user'])
            # user = self.application.db.get_user_from_watchlist(metadata['user'])
            print user
            if task_type == 'timeline':
                timeline_task_state = [rel for rel in user.match_incoming('TIMELINE_TASK_STATE')][0]
                user_object = dict(user.properties)
                user_object.update({'campaign_ids': campaign_id})
                res = app_object.send_task('timeline_retrieve_userlist',
                                           [[[user_object,
                                              timeline_task_state.properties['since_tweet_id'],
                                              timeline_task_state.properties['page_not_found']]]],
                                           queue=queue)
                timeline_task_state.properties['state'] = 1
                timeline_task_state.push()
            elif task_type == 'friendfollower':
                friendfollower_task_state = \
                    [rel for rel in user.match_incoming('FRIENDFOLLOWER_TASK_STATE')][0]
                res = app_object.send_task('crawl_friends_or_followers',
                                           [[[dict(user.properties),
                                              friendfollower_task_state.properties[
                                                  'page_not_found']]]],
                                           queue=queue)
                friendfollower_task_state.properties['state'] = 1
                friendfollower_task_state.push()
            elif task_type == 'userinfo':
                userinfo_task_state = \
                    [rel for rel in user.match_incoming('USER_INFO_HARVESTER_TASK_STATE')][0]
                res = app_object.send_task('crawl_user_info',
                                           [[[dict(user.properties),
                                              0]]],
                                           queue=queue)
                userinfo_task_state.properties['state'] = 1
                userinfo_task_state.push()
            else:
                raise MissingArgumentError('timeline or friendfollower')

            self.write(bson.json_util.dumps({'result': 'successful', 'message': res.task_id}))
            self.add_header('Content-Type', 'application/json')

        except MissingArgumentError as e:
            raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
