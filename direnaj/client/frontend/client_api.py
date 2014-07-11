'''
Client side API which will be used at our clients' machines

'''

# Change History :
# Date                                          Prog    Note
# Thu Oct 10 10:09:20 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

import tornado.ioloop
import tornado.web
from tornado.web import MissingArgumentError
from tornado.escape import json_decode
import requests
import bson.json_util
from jinja2 import Environment, FileSystemLoader

from direnaj.client.config.config import *

from direnaj.client.workers.streamcatcher import StreamCatcher
from direnaj.client.workers.twitter_api_getfollowers import drnj_graph_crawler
import direnaj.utils.drnj_time as drnj_time


threads = []

## import signal, sys
## def stop_all_threads(signal, frame):
##     print 'Stopping all threads'
##     for t in threads:
##         t.terminate()
##     sys.exit(0)
##
## signal.signal(signal.SIGINT, stop_all_threads)

app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])
vis_root_url = 'http://' + DIRENAJ_VIS_HOST + ':' + str(DIRENAJ_VIS_PORT[DIRENAJ_VIS_ENVIRONMENT])

keystore = KeyStore()

#  (r"/(friends|followers)/(crawl|view)", visFollowerHandler),

class visFollowerHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (friends_or_followers, crawl_or_view) = args

        print "visFollowerHandler: {} {}".format(friends_or_followers, crawl_or_view)
        if crawl_or_view=='crawl':
            user_id = self.get_argument('user_id', None)
            res = drnj_graph_crawler(friends_or_followers, int(user_id))
            env = Environment(loader=FileSystemLoader('direnaj/client/templates'))
            template = env.get_template('profiles/crawl_graph_notification.html')
            result = template.render(user_id=user_id, fof=friends_or_followers, res=res, href=vis_root_url)

            self.write(result)

        else:
            pass

#        auth_user_id = self.get_argument('auth_user_id')

#    (r"/statuses/(crawl|view)", visStatusesHandler),
class visStatusesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visStatusesHandler: {} ".format(crawl_or_view)




#    (r"/user/(crawl|view)", visSingleProfileHandler),
class visSingleProfileHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visSingleProfileHandler: {} ".format(crawl_or_view)

        user_id = self.get_argument('user_id', None)
        post_data = {"user_id": user_id}
        post_response = requests.post(url=app_root_url + '/profiles/view', data=post_data)

        #dat = json_decode(post_response.content)
        dat = bson.json_util.loads(post_response.content)
        print dat

        env = Environment(loader=FileSystemLoader('direnaj/client/templates'))
        env.globals['drnj_time'] = drnj_time
        template = env.get_template('profiles/history_view.html')
        result = template.render(profiles=dat)

        self.write(result)

#    (r"/profiles/(crawl|view)", visUserProfilesHandler),
class visUserProfilesHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        (crawl_or_view) = args

        print "visUserProfilesHandler: {} ".format(crawl_or_view)

        if crawl_or_view=='crawl':
            pass
        else:
            user_id = self.get_argument('user_id', None)
            # auth_user_id = self.get_argument('auth_user_id','direnaj')
            lim_count = self.get_argument('limit', None)
            if lim_count is None:
                lim_count = 20
            # post_data = {"user_id": root}
            # post_response = requests.post(url=app_root_url + '/profiles/view', data=post_data)
            dat = {"limit": lim_count}

            tmp = requests.post(url=app_root_url + '/profiles/view',data=dat)

            #print tmp

            dat = json_decode(tmp.content)

            env = Environment(loader=FileSystemLoader('direnaj/client/templates'))
            template = env.get_template('profiles/view.html')
            result = template.render(profiles=dat, len=len(dat), href=vis_root_url,
                                     username = self.get_secure_cookie('tai_user'),
                                     oauth_complete = True,
                                     rem_req_num = -1)

            self.write(result)

#    (r"/campaigns/(list|new|create_thread|kill_thread)", visCampaignsHandler),
class visCampaignsHandler(tornado.web.RequestHandler):
    def get(self, *args):
        self.post(*args)
        #self.write("not implemented yet")

    def post(self, *args):
        """

        """

        env = Environment(loader=FileSystemLoader('direnaj/client/templates'))

        (command) = args[0]

        print "visCampaignsHandler: {} ".format(command)
        direnaj_auth_secrets = keystore.direnaj_auth_secrets.copy()

        if command == "new":
            campaign_id = self.get_argument('campaign_id')
            campaign_type = self.get_argument('campaign_type')
            description = self.get_argument('description')
            query_terms = self.get_argument('query_terms')
            user_id_strs_to_follow = self.get_argument('user_id_strs_to_follow')
            user_screen_names_to_follow = self.get_argument('user_screen_names_to_follow')
            post_data = {"campaign_id": campaign_id,
                         "campaign_type": campaign_type,
                         "description": description,
                         "query_terms": query_terms,
                         "user_id_strs_to_follow": user_id_strs_to_follow,
                         "user_screen_names_to_follow": user_screen_names_to_follow}
            post_data.update(direnaj_auth_secrets)
            post_response = requests.post(url=app_root_url + '/campaigns/new', data=post_data)
            print post_response.content
            dat = bson.json_util.loads(post_response.content)

            self.createThread(campaign_id, query_terms)

            template = env.get_template('campaigns/new.html')
            result = template.render(result=dat)
        elif command == "list":
            print "list"
            post_response = requests.post(url=app_root_url + '/campaigns/list', data=direnaj_auth_secrets)
            print "ok"
            dat = bson.json_util.loads(post_response.content)

            template = env.get_template('campaigns/list.html')
            result = template.render(campaigns=dat, threads=threads)
        elif command == "create_thread":
            campaign_id = self.get_argument('campaign_id')
            query_terms = self.get_argument('query_terms')

            self.createThread(campaign_id, query_terms)

            template = env.get_template('campaigns/new.html')
            result = template.render(result={'status': 'success'})
        elif command == "kill_thread":
            campaign_id = self.get_argument('campaign_id')

            self.killThread(campaign_id)

            template = env.get_template('campaigns/new.html')
            result = template.render(result={'status': 'success'})

        self.write(result)

    def createThread(self, campaign_id, query_terms):
        t = StreamCatcher(
                campaign_id=campaign_id,
                postdata={"track": query_terms})
        t.start()
        threads.append([t, campaign_id, query_terms])

    def killThread(self, campaign_id):
        i = 0
        while True:
            if i == len(threads):
                break
            else:
                thread = threads[i]
                if thread[1] == campaign_id:
                    threads.pop(i)
                    thread[0].terminate()
                else:
                    i = i + 1