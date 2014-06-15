#!/usr/bin/env python

#
# Copyright 2012 Onur Gungor <onurgu@boun.edu.tr>
#

import sys, time

import twitter

import logging, logging.handlers

# local
from direnaj.client.config.config import *

import celery
from celery.utils.log import get_task_logger

from celery.signals import worker_shutdown

class FriendFollowerHarvesterTask(celery.Task):
    name = 'friend_follower_harvester'
    max_retries = None

    def __init__(self):

        self.logger = get_task_logger(__name__)

        self.logger.info("FriendFollowerHarvester Logging set up.")

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        print "ON_FAILURE"
        self.on_shutdown()

    def on_success(self, retval, task_id, args, kwargs):
        print "ON_SUCCESS"
        self.on_shutdown()

    def on_shutdown(self):
        print "ON_SHUTDOWN"

    # By default, we use user_id_str's.
    def run(self, screen_name):
        worker_shutdown.connect(self.on_shutdown)

        print "IN RUN ===="
        print screen_name

if __name__ == "__main__":

    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument("-p", "--projectname", help="project's name", dest="projectname")
    parser.add_argument("users_filename", help="a file with rows as ""username, label""")

    args = parser.parse_args()
