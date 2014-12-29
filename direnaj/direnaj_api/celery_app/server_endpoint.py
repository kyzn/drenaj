__author__ = 'onur'

from celery import Celery

import direnaj_api.config.server_celeryconfig as celeryconfig

app_object = Celery()

app_object.config_from_object(celeryconfig)

@app_object.task
def deneme(x, seconds):
    print "Sleeping for printing %s for %s seconds.." % (x, seconds)
    import time
    time.sleep(seconds)
    print x

from celery.utils.log import get_task_logger

logger = get_task_logger(__name__)

import os
from direnaj_api.utils.direnajmongomanager import DirenajMongoManager
from direnaj_api.utils.direnajneo4jmanager import create_batch_from_watchlist
from direnaj_api.app import Application

#@periodic_task(run_every=crontab(minute='*/1'))
@app_object.task(name='check_watchlist_and_dispatch_tasks')
def check_watchlist_and_dispatch_tasks():
    batch_size = 10

    # we can give an optional parameter for specific settings for this use.
    app = Application()

    # TODO: This might break!!! (and pycharm doesn't seem to recognize WARNs)
    create_batch_from_watchlist(app_object, batch_size)
    #app.db.create_batch_from_watchlist(app_object, batch_size)
    #create_batch_from_watchlist(app_object, batch_size)


if __name__ == "__main__":
    app_object.start()