__author__ = 'onur'

from celery import Celery

import drenaj.client.config.client_celeryconfig as celeryconfig

app_object = Celery()

app_object.config_from_object(celeryconfig)

from drenaj.client.workers.timelineharvester import TimelineRetrievalTask
from drenaj.client.workers.friendfollowerharvester import FriendFollowerHarvesterTask
from drenaj.client.workers.userInfoHarvester import UserInfoHarvesterTask

@app_object.task
def deneme(x, seconds):
    print "Sleeping for printing %s for %s seconds.." % (x, seconds)
    import time
    time.sleep(seconds)
    print x

if __name__ == "__main__":
    app_object.start()