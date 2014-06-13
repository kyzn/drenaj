import direnaj_api.utils.direnajmongomanager as direnajmongomanager
from direnaj_api.utils.direnaj_auth import direnaj_simple_auth

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

from tornado.gen import Return

import bson.json_util

from pymongo.errors import OperationFailure

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

    @direnaj_simple_auth
    @tornado.web.asynchronous
    def post(self, *args, **keywords):

        if len(args) > 1:
            (action) = args
        else:
            action = args[0]

        print action

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
                try:
                    direnajmongomanager.get_campaign(campaign_id)
                except Return, r:
                    campaign = r.value
                    self.write(bson.json_util.dumps(campaign))
                    self.add_header('Content-Type', 'application/json')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'list'):
            try:
                try:
                    self.write('IN\n')
                    direnajmongomanager.get_campaigns_list()
                    self.write('OUT\n')
                except Return, r:
                    self.write('CATCHED\n')
                    campaigns = r.value
                    self.write(bson.json_util.dumps(campaigns))
                    self.add_header('Content-Type', 'application/json')
                self.write('TRYOUT\n')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'view_freqs'):
            try:
                campaign_id = self.get_argument('campaign_id', 'default')
                try:
                    direnajmongomanager.get_campaign_with_freqs(campaign_id)
                except Return, r:
                    campaign = r.value
                    self.write(bson.json_util.dumps(campaign))
                    self.add_header('Content-Type', 'application/json')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'filter'):
            try:
                skip = int(self.get_argument('skip', 0))
                limit = int(self.get_argument('limit', 10))
                print("FILTER: ", "skip: ", skip, ", limit", limit)
                try:
                    direnajmongomanager.get_campaign_list_with_freqs(skip, limit)
                    print("END FILTER: ", "skip: ", skip, ", limit", limit)
                except Return, r:
                    campaigns = r.value
                    self.write(bson.json_util.dumps(campaigns))
                    self.add_header('Content-Type', 'application/json')

            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'histograms'):
            try:
                campaign_id = self.get_argument('campaign_id', 'default')
                re_calculate = self.get_argument('re_calculate', 'no')
                n_bins = self.get_argument('n_bins', "100")

                if re_calculate == 'no':
                    try:
                        direnajmongomanager.get_campaign_histograms(campaign_id)
                    except Return, r:
                        hists = r.value
                        if hists.count() == 0:
                            re_calculate = 'yes'

                if re_calculate == 'yes':
                    try:
                        direnajmongomanager.calculate_campaign_histograms(campaign_id, n_bins)
                    except Return, r:
                        hists = r.value

                self.write(bson.json_util.dumps(hists[0]))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        else:
            self.write('not recognized')
