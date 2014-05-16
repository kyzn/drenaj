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

#from celery.schedules import crontab
#from celery.task import periodic_task
from direnaj_api.utils.direnajmongomanager import create_batch_from_watchlist

#@periodic_task(run_every=crontab(minute='*/1'))
def check_watchlist_and_dispatch_tasks():
    batch_size = 10
    res_array = create_batch_from_watchlist(app_object, batch_size)


if __name__ == "__main__":
    app_object.start()