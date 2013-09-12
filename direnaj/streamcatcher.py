#! /usr/bin/env python
# -*- coding: iso-8859-1 -*-

#
# Copyright 2012 Onur Gungor <onurgu@boun.edu.tr>
# Derived from sna-tools for direnaj

# TODO: move this to direnaj/plugins/streamers in the future. I'm not
# moving it there already due to request.

import oauth2 as oauth
import pymongo

import sys, threading, time, datetime
import pycurl
import urllib
import os
import bson.json_util

import requests

# local
from config import *

# We should ignore SIGPIPE when using pycurl.NOSIGNAL - see
# the libcurl tutorial for more info.
try:
    import signal
    from signal import SIGPIPE, SIG_IGN
    signal.signal(signal.SIGPIPE, signal.SIG_IGN)
except ImportError:
    pass

class StreamCatcher(threading.Thread):
    def __init__(self, url = "", campaign_id="", postdata = {}, keystore = KeyStore()):

        # required for threads
        super(StreamCatcher, self).__init__()

        if url == "":
            self.filter_url = "https://stream.twitter.com/1.1/statuses/filter.json"
        else:
            self.filter_url = url

        self.prev_buf = ''

        self.direnaj_auth_secrets = keystore.direnaj_auth_secrets.copy()

        token = oauth.Token(key=keystore.access_token_key,
                            secret=keystore.access_token_secret)
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

        # set libcurl options
        self.curl = pycurl.Curl()
        self.curl.setopt(pycurl.URL, self.filter_url)
        # set to 0 for removing the header
        # self.curl.setopt(pycurl.HEADER, 1)
        self.curl.setopt(pycurl.HTTPHEADER,
                        ['Authorization: ' + str(header['Authorization'])])
        # self.curl.setopt(pycurl.WRITEDATA, self.ofile)
        self.curl.setopt(pycurl.WRITEFUNCTION, self.writefunction)
        self.curl.setopt(pycurl.FOLLOWLOCATION, 1)
        self.curl.setopt(pycurl.MAXREDIRS, 5)
        self.curl.setopt(pycurl.NOSIGNAL, 1)
        self.curl.setopt(pycurl.NOPROGRESS, 0)
        self.curl.setopt(pycurl.PROGRESSFUNCTION, self.progress)
        if len(postdata) != 0:
            self.curl.setopt(pycurl.POSTFIELDS,
                            urllib.urlencode(postdata))

        self.abortEvent = threading.Event()
        self.direnaj_store_url = 'http://'+DIRENAJ_APP_HOST+':'+str(DIRENAJ_APP_PORT[DIRENAJ_APP_ENVIRONMENT])+'/statuses/store'
        self.campaign_id = campaign_id

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
        self.prev_buf = self.prev_buf + buf

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

        if self.abortEvent.wait(0.0001):
            # stop curl
            print "shutting down.."
            return 0

    # TODO: Is it possible to make this call concurrent?
    def post_to_gateway(self, params, tmp):

        # TODO: LATER:
        ####
        ####
        ####Traceback (most recent call last):
        ####  File "streamcatcher.py", line 134, in writefunction
        ####    self.post_to_gateway(params, tmp)
        ####  File "streamcatcher.py", line 151, in post_to_gateway
        ####    params=params)
        ####  File "/home/direnaj/.virtualenvs/direnaj/local/lib/python2.7/site-packages/requests/api.py", line 88, in post
        ####    return request('post', url, data=data, **kwargs)
        ####  File "/home/direnaj/.virtualenvs/direnaj/local/lib/python2.7/site-packages/requests/api.py", line 44, in request
        ####    return session.request(method=method, url=url, **kwargs)
        ####  File "/home/direnaj/.virtualenvs/direnaj/local/lib/python2.7/site-packages/requests/sessions.py", line 335, in request
        ####    resp = self.send(prep, **send_kwargs)
        ####  File "/home/direnaj/.virtualenvs/direnaj/local/lib/python2.7/site-packages/requests/sessions.py", line 438, in send
        ####    r = adapter.send(request, **kwargs)
        ####  File "/home/direnaj/.virtualenvs/direnaj/local/lib/python2.7/site-packages/requests/adapters.py", line 327, in send
        ####    raise ConnectionError(e)
        ####requests.exceptions.ConnectionError: HTTPConnectionPool(host='voltran.cmpe.boun.edu.tr', port=9999): Max retries exceeded with url: /statuses/store?tweet_data=%5B%7B%22contributors%22%3A+null%2C+%22truncated%22%3A+false%2C+%22text%22%3A+%22%5C%22%40SibelOktay%3A+100.+Yil+Mahallesi%27nde+yasayan+bir+emekli+ogretmen+Eyup+Can%27a+ODTU+Yolu+hakkinda+nefis+cevap+vermis%3A+http%3A%2F%2Ft.co%2FeHHAXz5Q3u%5C%22%22%2C+%22in_reply_to_status_id%22%3A+null%2C+%22id%22%3A+375608551106183168%2C+%22favorite_count%22%3A+0%2C+%22source%22%3A+%22%3Ca+href%3D%5C%22http%3A%2F%2Ftwitter.com%2Fdownload%2Fandroid%5C%22+rel%3D%5C%22nofollow%5C%22%3ETwitter+for+Android%3C%2Fa%3E%22%2C+%22retweeted%22%3A+false%2C+%22coordinates%22%3A+null%2C+%22entities%22%3A+%7B%22symbols%22%3A+%5B%5D%2C+%22user_mentions%22%3A+%5B%7B%22indices%22%3A+%5B1%2C+12%5D%2C+%22screen_name%22%3A+%22SibelOktay%22%2C+%22id%22%3A+22586685%2C+%22name%22%3A+%22Sibel+Oktay%22%2C+%22id_str%22%3A+%2222586685%22%7D%5D%2C+%22hashtags%22%3A+%5B%5D%2C+%22urls%22%3A+%5B%7B%22url%22%3A+%22http%3A%2F%2Ft.co%2FeHHAXz5Q3u%22%2C+%22indices%22%3A+%5B115%2C+137%5D%2C+%22expanded_url%22%3A+%22http%3A%2F%2Fhaber.sol.org.tr%2Fmedya%2Feyup-cana-emekli-ogretmenden-odtu-yaniti-haberi-79136%22%2C+%22display_url%22%3A+%22haber.sol.org.tr%2Fmedya%2Feyup-can%5Cu2026%22%7D%5D%7D%2C+%22in_reply_to_screen_name%22%3A+null%2C+%22id_str%22%3A+%22375608551106183168%22%2C+%22retweet_count%22%3A+0%2C+%22in_reply_to_user_id%22%3A+null%2C+%22favorited%22%3A+false%2C+%22user%22%3A+%7B%22follow_request_sent%22%3A+null%2C+%22profile_use_background_image%22%3A+false%2C+%22id%22%3A+350174694%2C+%22verified%22%3A+false%2C+%22profile_image_url_https%22%3A+%22https%3A%2F%2Fsi0.twimg.com%2Fprofile_images%2F378800000390884937%2Fee96a07b193f4f9dce83bd4e2fa205f2_normal.jpeg%22%2C+%22profile_sidebar_fill_color%22%3A+%22DDEEF6%22%2C+%22is_translator%22%3A+false%2C+%22geo_enabled%22%3A+true%2C+%22profile_text_color%22%3A+%22333333%22%2C+%22followers_count%22%3A+963%2C+%22protected%22%3A+false%2C+%22location%22%3A+%22%22%2C+%22default_profile_image%22%3A+false%2C+%22id_str%22%3A+%22350174694%22%2C+%22utc_offset%22%3A+null%2C+%22statuses_count%22%3A+4269%2C+%22description%22%3A+null%2C+%22friends_count%22%3A+1834%2C+%22profile_link_color%22%3A+%220084B4%22%2C+%22profile_image_url%22%3A+%22http%3A%2F%2Fa0.twimg.com%2Fprofile_images%2F378800000390884937%2Fee96a07b193f4f9dce83bd4e2fa205f2_normal.jpeg%22%2C+%22notifications%22%3A+null%2C+%22profile_background_image_url_https%22%3A+%22https%3A%2F%2Fsi0.twimg.com%2Fimages%2Fthemes%2Ftheme1%2Fbg.png%22%2C+%22profile_background_color%22%3A+%22C0DEED%22%2C+%22profile_banner_url%22%3A+%22https%3A%2F%2Fpbs.twimg.com%2Fprofile_banners%2F350174694%2F1376929887%22%2C+%22profile_background_image_url%22%3A+%22http%3A%2F%2Fa0.twimg.com%2Fimages%2Fthemes%2Ftheme1%2Fbg.png%22%2C+%22screen_name%22%3A+%22Ert_Acr%22%2C+%22lang%22%3A+%22tr%22%2C+%22profile_background_tile%22%3A+false%2C+%22favourites_count%22%3A+248%2C+%22name%22%3A+%22Eren+Acar%22%2C+%22url%22%3A+null%2C+%22created_at%22%3A+%22Sun+Aug+07+09%3A53%3A28+%2B0000+2011%22%2C+%22contributors_enabled%22%3A+false%2C+%22time_zone%22%3A+null%2C+%22profile_sidebar_border_color%22%3A+%22FFFFFF%22%2C+%22default_profile%22%3A+false%2C+%22following%22%3A+null%2C+%22listed_count%22%3A+3%7D%2C+%22geo%22%3A+null%2C+%22in_reply_to_user_id_str%22%3A+null%2C+%22possibly_sensitive%22%3A+false%2C+%22lang%22%3A+%22tr%22%2C+%22created_at%22%3A+%22Thu+Sep+05+13%3A17%3A11+%2B0000+2013%22%2C+%22filter_level%22%3A+%22medium%22%2C+%22in_reply_to_status_id_str%22%3A+null%2C+%22place%22%3A+null%7D%5D&auth_password=tamtam&auth_user_id=direnaj&campaign_id=habersolorgtr (Caused by <class 'socket.error'>: [Errno 111] Connection refused)
        ####failed writing body (in fact this might be caused by simply calling self.command('stop')


        if not tmp:
            return

        params.update({'tweet_data': bson.json_util.dumps(tmp)})
        params.update(self.direnaj_auth_secrets)

        print params
        # TODO: here, error no 111 connection refused exception must be try-catched.
        stop_trying = False
        exp_backoff_duration = 1
        while not stop_trying:
            try:
                response = requests.post(self.direnaj_store_url,
                                        params=params)
            except requests.exceptions.ConnectionError, e:
                if exp_backoff_duration > 2**2:
                    stop_trying = True
                    # TODO: log this issue at this point.
                else:
                    time.sleep(exp_backoff_duration)
                    exp_backoff_duration *= 2

        print response.content

    def run(self):
        try:
            self.curl.perform()
            self.curl.close()
        except Exception, e:
            # callback aborted
            if e[0] == 42:
                print "callback aborted"
            # gnutls: a tls packet with unexpected size is received.
            elif e[0] == 56:
                print "tls packet with unexpected size"
            elif e[0] == 23:
                print "failed writing body (in fact this might be caused by simply calling self.command('stop')"
            else:
                raise
        sys.stdout.write(".")
        sys.stdout.flush()

    def progress(self, download_t, download_d, upload_t, upload_d):
        pass

#    def join(self, command='', timeout=None):
#        if command == 'stop':
#            self.abortEvent.set()
#            super(StreamCatcher, self).join(timeout)

threads = []

import signal
def stop_all_threads(signal, frame):
    print 'Stopping all threads'
    for t in threads:
        t.command('stop')
    sys.exit(0)

import argparse

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='streamercatcher')
    parser.add_argument('campaign_id', help='campaign name')
    parser.add_argument('keyword', default='türkiye', help='campaign name')
    args = parser.parse_args()

    campaign_id = args.campaign_id
    user_input = args.keyword

    t = StreamCatcher(
            campaign_id=campaign_id,
            postdata={"track": user_input})
    t.start()
    threads.append(t)

    signal.signal(signal.SIGINT, stop_all_threads)
    signal.pause()
