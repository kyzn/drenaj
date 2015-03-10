from direnaj_api.utils.direnaj_auth import direnaj_simple_auth

import direnaj_api.utils.direnajneo4jmanager as direnajneo4jmanager

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado import gen
from tornado.gen import Return

import bson.json_util

from pymongo.errors import OperationFailure

from py2neo import Relationship



class CampaignsHandler(tornado.web.RequestHandler):

    ## def datetime_hook(self, dct):
    ##     # TODO: this only checks for the first level 'created_at'
    ##     # We should think about whether making this recursive.
    ##     if 'created_at' in dct:
    ##         time_struct = time.strptime(dct['created_at'], "%a %b %d %H:%M:%S +0000 %Y") #Tue Apr 26 08:57:55 +0000 2011
    ##         dct['created_at'] = datetime.datetime.fromtimestamp(time.mktime(time_struct))
    ##         return bson.json_util.object_hook(dct)
    ##     return bson.json_util.object_hook(dct)

    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    #@direnaj_simple_auth
    @tornado.web.asynchronous
    @gen.coroutine
    def post(self, *args, **keywords):
        # Refactoring note: the new direnaj database manager is now a class and it's initialized
        # in Tornado application. The old code just imported the manager module as 'direnajmongomanager'/
        # Instead of search&replace procedure, assign the new db instance to direnajmongomanager.
        direnajmongomanager = self.application.db

        print args

        action = args[0]

        print action
        print args


        verbose_response = self.get_argument('verbose', '')

        if (action == 'new'):
            try:
                campaign_id = self.get_argument('campaign_id')
                description = self.get_argument('description', '')
                campaign_type = self.get_argument('campaign_type', '') # timeline, streaming or both.
                query_terms = self.get_argument('query_terms', '')
                user_id_strs_to_follow = self.get_argument('user_id_strs_to_follow', '')
                user_screen_names_to_follow = self.get_argument('user_screen_names_to_follow', '')
                try:
                    direnajmongomanager.create_campaign(
                        {
                            'campaign_id': campaign_id,
                            'description': description,
                            'campaign_type': campaign_type,
                            'query_terms': query_terms,
                            'user_id_strs_to_follow': user_id_strs_to_follow,
                            'user_screen_names_to_follow': user_screen_names_to_follow,
                        })
                    result = 'success'
                except OperationFailure:
                    result = 'failure'

                self.write({'status': result})
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'view'):
            try:
                campaign_id = self.get_argument('campaign_id', 'default')
                subcommand = args[1]
                if subcommand == None:
                    cursor = direnajmongomanager.get_campaign(campaign_id)
                    campaign = yield cursor
                    if campaign:
                        self.write(bson.json_util.dumps(campaign))
                    else:
                        self.write(bson.json_util.dumps({}))
                    self.add_header('Content-Type', 'application/json')
                if subcommand == "watched_users":
                    skip = self.get_argument('skip', 0)
                    limit = self.get_argument('limit', 100)
                    attached_users_array = direnajneo4jmanager.get_users_attached_to_campaign(campaign_id, skip, limit)
                    attached_users_response = {'watched_users': [], 'campaign_id': campaign_id}

                    for item in attached_users_array:
                        x = dict()
                        y = dict()
                        for rel in item[1]:
                            if rel.type == 'TIMELINE_TASK_STATE':
                                x = dict(rel.properties)
                            elif rel.type == 'FRIENDFOLLOWER_TASK_STATE':
                                y = dict(rel.properties)
                        attached_users_response['watched_users'] += [[item[0], x, y]]

                    self.write(bson.json_util.dumps(attached_users_response))
                    self.add_header('Content-Type', 'application/json')
                elif subcommand == "freqs":
                    cursor = direnajmongomanager.get_campaign_with_freqs(campaign_id)
                    cursor = yield cursor
                    campaign = cursor['result']
                    if campaign:
                        self.write(bson.json_util.dumps(campaign))
                    else:
                        self.write(bson.json_util.dumps({}))
                    self.add_header('Content-Type', 'application/json')
                elif subcommand == 'histograms':
                    re_calculate = self.get_argument('re_calculate', 'no')
                    n_bins = self.get_argument('n_bins', "100")

                    if re_calculate == 'no':

                        cursor = direnajmongomanager.get_campaign_histograms(campaign_id)
                        hists = yield cursor
                        if hists.count() == 0:
                            re_calculate = 'yes'

                    if re_calculate == 'yes':
                        results = direnajmongomanager.get_users_related_with_campaign(campaign_id)
                        tmp = yield results[0]
                        users = tmp['result']

                        # How many tweets?
                        tmp = yield results[1]
                        n_tweets = tmp.count()

                        hist = direnajmongomanager.prepare_hist_and_plot(n_tweets, users, n_bins, campaign_id)
                        hists = [hist]

                        yield self.application.db.motor_column.histograms.insert(hist)

                    self.write(bson.json_util.dumps(hists[0]))
                    self.add_header('Content-Type', 'application/json')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'edit'):
            try:
                campaign_id = self.get_argument('campaign_id')
                subcommand = args[1]
                if subcommand == 'add_watched_users':
                    new_watched_users = self.get_argument('new_watched_users','')
                    direnajmongomanager.add_to_watchlist(campaign_id, new_watched_users)
                    self.write(bson.json_util.dumps({'result': 'successful'}))
                    self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)

        elif (action == 'list'):
            try:
                cursor = direnajmongomanager.get_campaigns_list()
                campaigns = yield cursor.to_list(length=None)
                self.write(bson.json_util.dumps(campaigns))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'filter'):
            try:
                skip = int(self.get_argument('skip', 0))
                limit = int(self.get_argument('limit', 10))
                print("FILTER: ", "skip: ", skip, ", limit", limit)
                cursor = direnajmongomanager.get_campaign_list_with_freqs(skip, limit)
                print("END FILTER: ", "skip: ", skip, ", limit", limit)
                cursor = yield cursor
                campaigns = cursor['result']
                print("GCLWF: EXCEPTION: ", "campaigns: ", campaigns)
                self.write(bson.json_util.dumps(campaigns))
                self.add_header('Content-Type', 'application/json')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        else:
            self.write('not recognized')
