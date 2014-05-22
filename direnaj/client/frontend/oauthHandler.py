import json
import hashlib

import tornado.ioloop
import tornado.web
from tornado.web import MissingArgumentError
from tornado.escape import json_decode
import tornado.template
import tweepy
import requests

from direnaj.client.config.config import *
from direnaj.utils.direnajmongomanager import *
from direnaj.direnaj_api.utils.direnaj_collection_templates import *

app_root_url = 'http://' + DIRENAJ_APP_HOST + ':' + str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])
vis_root_url = 'http://' + DIRENAJ_VIS_HOST + ':' + str(DIRENAJ_VIS_PORT[DIRENAJ_VIS_ENVIRONMENT])

db = mongo_client[DIRENAJ_DB[DIRENAJ_APP_ENVIRONMENT]]
accounts = db['accounts']
keystore = KeyStore()

from jinja2 import Environment, FileSystemLoader

def get_api(user):
    auth = tweepy.OAuthHandler(keystore.app_consumer_key, keystore.app_consumer_secret)
    auth.set_access_token(user['creds_AccessToken'], user['creds_AccessTokenSecret'])
    api = tweepy.API(auth)

    #print "------------------------"
    #print "get api for user ", user
    #print api
    #print user['creds_AccessToken'], user['creds_AccessTokenSecret']
    #print "------------------------"

    return api

def gen_templ(template_name, **kwargs):
    if 'username' in kwargs:
        user = accounts.find_one( {'username': kwargs['username']} )
        if user and 'creds_AccessToken' in user and user['creds_AccessToken']:
            kwargs['oauth_complete'] = True
            kwargs['rem_req_num'] = get_api(user).rate_limit_status()['resources']['followers']['/followers/ids']['remaining']
        else:
            kwargs['oauth_complete'] = False

    return tornado.template.Template(
        open('templates/' + template_name).read()).generate(**kwargs)


class SigninHandler(tornado.web.RequestHandler):
    def post(self):
        m = hashlib.sha1(self.get_argument('pass')).hexdigest()
        user = accounts.find_one( {'username': self.get_argument('email'), 
                                   'sha1_password': m} )
        if( user is None ):
            self.write( gen_templ('sign_in_nok.html') )
        else:
            self.set_secure_cookie( 'tai_user', user['username'] )

            if 'access_token_key' in user and 'access_token_secret' in user:
                self.write( gen_templ('sign_in_complete.html') )
            else:
                self.write( gen_templ('sign_in_ok.html', username = user['username']) )

class SignupHandler(tornado.web.RequestHandler):
    def post(self):
        m = hashlib.sha1(self.get_argument('pass')).hexdigest()
        email = self.get_argument('email')
        user = accounts.find_one( {'username': email } )

        if( user is not None ):
            self.write( gen_templ('sign_in_nok.html') )
        else:
            user = { 'username': email,
                     'sha1_password': m }
            self.set_secure_cookie( 'tai_user', email )
            accounts.insert(user)
            self.write( gen_templ( 'sign_in_ok.html', username = email ) )

class SignoutHandler(tornado.web.RequestHandler):
    def post(self):
        self.clear_all_cookies()
        self.write( gen_templ( 'sign_in.html' ) )
        #self.redirect('/vis/profiles/view')

class OAuthHandler(tornado.web.RequestHandler):
    def post(self):
        user = accounts.find_one( {'username': self.get_secure_cookie('tai_user')} )
        if user:
            # http://pythonhosted.org/tweepy/html/auth_tutorial.html
            auth = tweepy.OAuthHandler(keystore.app_consumer_key, keystore.app_consumer_secret)
            try:
                redirect_url = auth.get_authorization_url()

                accounts.update( {'username': user['username']},
                                 {'$set': { 'request_key': auth.request_token.key,
                                            'request_secret': auth.request_token.secret}} )

                self.write( redirect_url )
            except tweepy.TweepError:
                print 'Error! Failed to get request token.'

        else:
            self.write('false')

    def get(self):
        # user is redirected from twitter
        user = accounts.find_one( {'username': self.get_secure_cookie('tai_user')} )
        if user:
            auth = tweepy.OAuthHandler(keystore.app_consumer_key, keystore.app_consumer_secret)
            auth.set_request_token( user['request_key'], user['request_secret'] )
            try:
                verifier = self.get_argument('oauth_verifier')
                auth.get_access_token( verifier )

                accounts.update( {'username': user['username']},
                                 {'$set': { 'creds_AccessToken': auth.access_token.key,
                                            'creds_AccessTokenSecret': auth.access_token.secret }} )

                #self.write(gen_templ('base.html', username = user['username'] ))

                lim_count = self.get_argument('limit', None)
                if lim_count is None:
                    lim_count = 20
                dat = {"limit": lim_count}

                tmp = requests.post(url=app_root_url + '/profiles/view',data=dat)
                dat = json_decode(tmp.content)

                env = Environment(loader=FileSystemLoader('templates'))
                template = env.get_template('profiles/view.html')

                user = accounts.find_one( {'username': user['username']} )
                if user and 'creds_AccessToken' in user and user['creds_AccessToken']:
                    oauth_complete = True
                    rem_req_num = get_api(user).rate_limit_status()['resources']['followers']['/followers/ids']['remaining']
                else:
                    oauth_complete = False
                    rem_req_num = -1

                result = template.render(profiles=dat, len=len(dat), href=vis_root_url,
                                         username = self.get_secure_cookie('tai_user'),
                                         oauth_complete = oauth_complete,
                                         rem_req_num = rem_req_num)

                self.write(result)

            except Exception as e:
                print 'Error! Failed to get access token.'
                print e

def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False

def lookup(screenname, tai_user):
    #print "looking up ", screenname, "for user", tai_user
    api = get_api(tai_user)
    # store full data about the looked up user

    if is_number(screenname):
        target_user = api.get_user(user_id=int(screenname))
        screenname = target_user.screen_name
    else:
        target_user = api.get_user(screen_name=screenname)

    twitter_user.update({'twitter_id_str': target_user.id_str},
                        {'$set':
                             {'twitter_id_str' : target_user.id_str,
                              'twitter_name' : target_user.name,
                              'twitter_created_at' : target_user.created_at,
                              'twitter_lang' : target_user.lang,
                              'twitter_followers_count' : target_user.followers_count,
                              'twitter_protected' : target_user.protected,
                              'twitter_description' : target_user.description,
                              'twitter_statuses_count' : target_user.statuses_count,
                              'twitter_friends_count' : target_user.friends_count,
                              'twitter_screen_name' : target_user.screen_name,
                              }
                         }, upsert = True )

    # delete previous follows for this user
    twitter_follow.remove({'twitter_id_str': target_user.id_str})

    for user in tweepy.Cursor(api.followers_ids, screen_name=screenname).items():
        twitter_follow.insert( { 'twitter_id_str' : target_user.id_str,
                                 'twitter_follower_id_str': str(user) } )


class GraphexpolorerHandler(tornado.web.RequestHandler):
    def post(self):
        screenname = self.get_argument('screenname')
        # if screenname is not in database
        # or if updated very long time ago (TODO)
        # lookup screenname followers

        # use our_user's credentials if available
        login = self.get_secure_cookie('tai_user')
        if login:
            tai_user = accounts.find_one( {'username': login} )
        else:
            tai_user = accounts.find_one( {'username': 'can5'} )

        lookup(screenname, tai_user)
        self.write( 'Updated' )

def get_children(twitter_id_str):
    user_twitter_data = twitter_user.find_one( {'twitter_id_str':  twitter_id_str} )
    if user_twitter_data:
        init_parent_name = user_twitter_data['twitter_screen_name']
        children = []

        # see weight of followers
        followers = list( twitter_follow.find( {'twitter_id_str': twitter_id_str }) )
        for follower in followers:
            fof = twitter_user.find_one( {'twitter_id_str': follower['twitter_follower_id_str'] } )

            if fof:
                size = fof['twitter_followers_count']
                name = fof['twitter_screen_name']
                no_data = False
            else:
                size = 1
                name = follower['twitter_follower_id_str']
                no_data = True

            children.append( { 'name': name, 'size': size, 'no_data': no_data} )

        return {'name': init_parent_name, 'children': children}
    else:
        return None # send more data request message


class SmallReadHandler(tornado.web.RequestHandler):
    def post(self):
        login = self.get_secure_cookie('tai_user', None)
        if login:
            tai_user = accounts.find_one( {'username': login} )
        else:
            tai_user = accounts.find_one( {'username': 'can5'} )

        if tai_user:
            req_data = self.get_argument('req_data')
            if req_data == 'rem_req_num':
                rem_req_num = get_api(tai_user).rate_limit_status()['resources']['followers']['/followers/ids']['remaining']
                self.write("Remaining requests: " + str(rem_req_num))
                return

            if req_data == 'graph_data':
                init_screenname = self.get_argument('init_screenname')
                user_twitter_data = twitter_user.find_one( {'twitter_screen_name':  init_screenname} )
                if user_twitter_data is not None:
                    init_screenname_id_str = user_twitter_data['twitter_id_str']
                    data = get_children( init_screenname_id_str )
                    self.write( json.dumps(data) );
                    return

        self.write('no')
