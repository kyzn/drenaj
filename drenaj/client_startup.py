import tornado.ioloop, tornado.web
from tornado.httpserver import HTTPServer

from drenaj.client.config.config import *
from drenaj.client.frontend.routes_config import routes_config

from jinja2 import Environment, FileSystemLoader
import drenaj.utils.drnj_time as drnj_time

application = tornado.web.Application(routes_config,
                                      cookie_secret = 'vospRVBgTF6HTnghpd/za+UgiZ/NXUDUkTnYGx1d4hY=')

print('PATHPATHPATH')
print(os.path.join(os.path.dirname(__file__),'client/templates'))

application.settings['env'] = Environment(loader=FileSystemLoader(os.path.join(os.path.dirname(__file__),'client/templates')))
application.settings['env'].globals['drnj_time'] = drnj_time

def bind_server(environment):
    http_server = HTTPServer(application, xheaders=True)
    http_server.listen(DRENAJ_VIS_PORT[environment])

import signal, os, sys

def start(environment, n_child_processes=4):
    # run the worker
    os.system("celery multi start worker -l debug -f worker.log -c %s -A drenaj.client.celery_app.client_endpoint" % n_child_processes)
    signal.signal(signal.SIGINT, stop_all_workers)
    # run the web service
    print "Direnaj Local Visualization and Interaction Manager Starting on port %s" % DRENAJ_VIS_PORT[environment]
    bind_server(environment)
    tornado.ioloop.IOLoop.instance().start()
    return application

def stop_all_workers(signal, frame):
    os.system("celery multi stop worker")
    print("How dare you? Bye bye!")
    sys.exit(0)

def get_access_token():

    import sys

    # parse_qsl moved to urlparse module in v2.6
    try:
        from urlparse import parse_qsl
    except:
        from cgi import parse_qsl

    import oauth2 as oauth

    REQUEST_TOKEN_URL = 'https://api.twitter.com/oauth/request_token'
    ACCESS_TOKEN_URL  = 'https://api.twitter.com/oauth/access_token'
    AUTHORIZATION_URL = 'https://api.twitter.com/oauth/authorize'
    SIGNIN_URL        = 'https://api.twitter.com/oauth/authenticate'

    keystore = KeyStore()

    access_tokens = []

    consumer_key    = keystore.app_consumer_key
    consumer_secret = keystore.app_consumer_secret

    if consumer_key is None or consumer_secret is None:
        print 'You need to edit this script and provide values for the'
        print 'consumer_key and also consumer_secret.'
        print ''
        print 'The values you need come from Twitter - you need to register'
        print 'as a developer your "application".  This is needed only until'
        print 'Twitter finishes the idea they have of a way to allow open-source'
        print 'based libraries to have a token that can be used to generate a'
        print 'one-time use key that will allow the library to make the request'
        print 'on your behalf.'
        print ''
        sys.exit(1)

    while True:
        signature_method_hmac_sha1 = oauth.SignatureMethod_HMAC_SHA1()
        oauth_consumer             = oauth.Consumer(key=consumer_key, secret=consumer_secret)
        oauth_client               = oauth.Client(oauth_consumer)

        print 'Requesting temp token from Twitter'

        resp, content = oauth_client.request(REQUEST_TOKEN_URL, 'GET')

        if resp['status'] != '200':
            print 'Invalid respond from Twitter requesting temp token: %s' % resp['status']
        else:
            request_token = dict(parse_qsl(content))

            print ''
            print 'Please visit this Twitter page and retrieve the pincode to be used'
            print 'in the next step to obtaining an Authentication Token:'
            print ''
            print '%s?oauth_token=%s' % (AUTHORIZATION_URL, request_token['oauth_token'])
            print ''

            pincode = raw_input('Pincode? ')

            if not pincode:
                print('You did not enter any pincode, finishing setup.')
                break

            token = oauth.Token(request_token['oauth_token'], request_token['oauth_token_secret'])
            token.set_verifier(pincode)

            print ''
            print 'Generating and signing request for an access token'
            print ''

            oauth_client  = oauth.Client(oauth_consumer, token)
            resp, content = oauth_client.request(ACCESS_TOKEN_URL, method='POST', body='oauth_callback=oob&oauth_verifier=%s' % pincode)
            access_token  = dict(parse_qsl(content))

            if resp['status'] != '200':
                print 'The request for a Token did not succeed: %s' % resp['status']
                print access_token
            else:
                print 'Your Twitter Access Token key: %s' % access_token['oauth_token']
                print '          Access Token secret: %s' % access_token['oauth_token_secret']
                print ''

            access_tokens.append([access_token['oauth_token'], access_token['oauth_token_secret']])

    for access_token in access_tokens:
        keystore.insert_access_token(access_token[0], access_token[1])

def main():
    import argparse
    parser = argparse.ArgumentParser(description='drenaj client')
    parser.add_argument('command', help='used for starting or setup')
    parser.add_argument('-c', '--n_children', default=1, help='number of child processes')
    args = parser.parse_args()

    keystore = KeyStore()
    if args.command == 'runserver':
        keystore.release_access_tokens()
        if keystore.no_access_tokens():
            get_access_token()
            if not keystore.no_access_tokens():
                start(DRENAJ_VIS_ENVIRONMENT, args.n_children)
            else:
                print("Please complete the setup process correctly to configure your access token key and secret.")
                sys.exit(1)
        else:
            start(DRENAJ_VIS_ENVIRONMENT, args.n_children)
    elif args.command == 'setup':
        get_access_token()
        if not keystore.no_access_tokens():
            start(DRENAJ_VIS_ENVIRONMENT, args.n_children)
        else:
            print("Please complete the setup process correctly to configure your access token key and secret.")
            sys.exit(1)
    elif args.command == 'release_access_tokens':
        keystore.release_access_tokens()
        print("Access tokens released.")

if __name__ == "__main__":
    main()
