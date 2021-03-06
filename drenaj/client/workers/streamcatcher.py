#! /usr/bin/env python
# -*- coding: iso-8859-1 -*-

#
# Copyright 2012 Onur Gungor <onurgu@boun.edu.tr>
# Derived from sna-tools for drenaj

# TODO: move this to drenaj/plugins/streamers in the future. I'm not
# moving it there already due to request.


'''

Example for autodoc

'''

import oauth2 as oauth

import multiprocessing

import sys, time, datetime

import bson.json_util

import requests
import httplib

# local
from drenaj.client.config.config import *


class StreamCatcher(multiprocessing.Process):
    '''
    This is a fantastic class!
    '''
    def __init__(self, url = "", campaign_id="", postdata = {}, keystore = KeyStore()):

        signal.signal(signal.SIGINT, signal.SIG_IGN)

        signal.signal(signal.SIGTERM, self.on_terminate)

        # required for threads
        super(StreamCatcher, self).__init__()

        if url == "":
            self.filter_url = "https://stream.twitter.com/1.1/statuses/filter.json"
        else:
            self.filter_url = url

        self.prev_buf = ''

        self.drenaj_auth_secrets = keystore.drenaj_auth_secrets.copy()
        self.access_tokens = keystore.acquire_access_tokens()

        self.requests_session = requests.Session()
        self.requests_session.stream = True

        self.postdata = postdata
        self.keystore = keystore

        self.filter_request = self.prepare_request(postdata, keystore)

        self.abortEvent = multiprocessing.Event()
        self.drenaj_store_url = 'http://'+DRENAJ_APP_HOST+':'+str(DRENAJ_APP_PORT[DRENAJ_APP_ENVIRONMENT])+'/statuses/store'
        self.campaign_id = campaign_id

        self.streamer_id = ":".join([campaign_id, postdata['track']])

    def on_terminate(self, signal_no, frame):
        print "Cleaning up for streamer_id=%s..." % self.streamer_id
        self.keystore.release_access_tokens(self.access_tokens)
        self.requests_session.close()
        os.kill(os.getppid(), signal.SIGINT)
        sys.exit(0)

    def prepare_request(self, postdata, keystore=KeyStore()):
        authorization_header = self.sign_request(postdata, keystore)
        req = requests.Request('POST', self.filter_url)
        req.headers.update({'Authorization': authorization_header})
        req.data.update(postdata)
        return req.prepare()

    def sign_request(self, postdata, keystore=KeyStore()):
        token = oauth.Token(key=self.access_tokens[0][0],
                            secret=self.access_tokens[0][1])
        consumer = oauth.Consumer(key=keystore.app_consumer_key,
                                  secret=keystore.app_consumer_secret)

        signature_method = oauth.SignatureMethod_HMAC_SHA1()

        params = {
                'oauth_version': "1.0",
                'oauth_nonce': oauth.generate_nonce(),
                'oauth_timestamp': int(time.time())
        }

        # Set our token/key parameters
        params['oauth_token'] = token.key
        params['oauth_consumer_key'] = consumer.key

        # prepare POST request on our own.
        for key, value in postdata.items():
            params[key] = value

        req = oauth.Request(method="POST",
                            url=self.filter_url,
                            parameters=params,
                            is_form_encoded=True)

        # Sign the request.
        req.sign_request(signature_method, consumer, token)

        header = req.to_header(realm='Firehose')
        return header['Authorization']

    def command(self, command_name):
        if command_name == 'stop':
            self.abortEvent.set()

    def datetime_hook(self, dct):
        if 'created_at' in dct:
            time_struct = time.strptime(dct['created_at'], "%a %b %d %H:%M:%S +0000 %Y") #Tue Apr 26 08:57:55 +0000 2011
            dct['created_at'] = datetime.datetime.fromtimestamp(time.mktime(time_struct))
            return dct
        return dct

    def writefunction(self, buf):

        print buf
        # mongo
        self.prev_buf = self.prev_buf + (buf+'\r\n')

        params = {'campaign_id': self.campaign_id }

        if '\r\n' in self.prev_buf:
           parts = self.prev_buf.split('\r\n')
           if len(parts) > 1:
               tmp = []
               for p in parts[0:-1]:
                   if len(p) > 0:
                       # obj = json.loads(p, object_hook=self.datetime_hook)
                       tmp.append(bson.json_util.loads(p))
               self.prev_buf = parts[-1]
           else:
               tmp = [bson.json_util.loads(parts[0])]

               self.prev_buf = ''
           # TODO: check for this.
           # When Twitter closes connection on itself, i.e. another streaming api connection is made with the same credentials, the response is like this:
           # {"disconnect":{"code":7,"stream_name":"thbounsigmalab1-statuses227378","reason":"admin logout"}}

           self.post_to_gateway(params, tmp)

    # TODO: Is it possible to make this call concurrent?
    def post_to_gateway(self, params, tmp):

        if not tmp:
            return

        params.update({'tweet_data': bson.json_util.dumps(tmp)})
        params.update(self.drenaj_auth_secrets)

        print params
        # TODO: here, error no 111 connection refused exception must be try-catched.
        stop_trying = False
        exp_backoff_duration = 1
        response = None
        while not stop_trying:
            try:
                response = requests.post(self.drenaj_store_url,
                                        data=params)
                stop_trying = True
            except requests.exceptions.ConnectionError, e:
                if exp_backoff_duration > 2**2:
                    stop_trying = True
                    # TODO: log this issue at this point.
                else:
                    time.sleep(exp_backoff_duration)
                    exp_backoff_duration *= 2

        if response:
            print response.content

    def run(self):
        self.r = self.requests_session.send(self.filter_request)

        while True:
            try:
                for line in self.r.iter_lines():
                    print line
                    self.writefunction(line)
                    if self.abortEvent.wait(0.0001):
                        # stop
                        print "shutting down.."
                        return 0
            # TODO: log these
            except httplib.IncompleteRead, e:
                print "IncompleteRead exception is catched. We'll restart the connection"
                self.filter_request = self.prepare_request(self.postdata, self.keystore)
                self.r = self.requests_session.send(self.filter_request)
                ## contine running
            except TypeError, e:
                print e
                print "Restarting"
                self.filter_request = self.prepare_request(self.postdata, self.keystore)
                self.r = self.requests_session.send(self.filter_request)
                ## contine running
            except Exception, e:
                print e
                raise e

threads = []

import signal
def stop_all_threads(signal, frame):
    print 'Stopping all threads'
    for t in threads:
        t.terminate()
    sys.exit(0)

import argparse

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='streamercatcher')
    parser.add_argument('campaign_id', help='campaign name')
    parser.add_argument('keyword', default='t�rkiye', help='campaign name')
    parser.add_argument('--language')
    args = parser.parse_args()

    campaign_id = args.campaign_id
    if args.language:
        lang = args.language
    else:
        lang = None
    user_input = args.keyword

    if lang:
        postdata = {"track": user_input, "language": lang}
    else:
        postdata = {"track": user_input}

    t = StreamCatcher(
            campaign_id=campaign_id,
        postdata=postdata)
    t.start()
    threads.append(t)

    signal.signal(signal.SIGINT, stop_all_threads)
    signal.pause()
    #while True:
    #    time.sleep(0.5)
