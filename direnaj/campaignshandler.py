import direnajmongomanager
from direnaj_auth import direnaj_simple_auth

import tornado.ioloop
import tornado.web

from tornado.web import HTTPError
from tornado.web import MissingArgumentError

import bson.json_util

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
    def post(self, *args, **keywords):

        if len(args) > 1:
            (action) = args
        else:
            action = args[0]

        print action

        verbose_response = self.get_argument('verbose', '')

        if (action == 'new'):
            self.write('not implemented yet')
        elif (action == 'view'):
            try:
                campaign_id = self.get_argument('campaign_id', 'default')
                campaign = direnajmongomanager.get_campaign_with_freqs(campaign_id)

                self.write(bson.json_util.dumps(campaign))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        elif (action == 'filter'):
            try:
                skip = int(self.get_argument('skip', 0))
                limit = int(self.get_argument('limit', 10))
                campaigns = direnajmongomanager.get_campaign_list_with_freqs(skip, limit)

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
                    hists = direnajmongomanager.get_campaign_histograms(campaign_id)
                    if hists.count() == 0:
                        re_calculate = 'yes'

                if re_calculate == 'yes':
                    hists = direnajmongomanager.calculate_campaign_histograms(campaign_id, n_bins)

                self.write(bson.json_util.dumps(hists[0]))
                self.add_header('Content-Type', 'application/json')
            except MissingArgumentError as e:
                # TODO: implement logging.
                raise HTTPError(500, 'You didn''t supply %s as an argument' % e.arg_name)
        else:
            self.write('not recognized')
