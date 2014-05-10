__author__ = 'onur'

from celery import Celery

import client.config.celeryconfig as celeryconfig

app_object = Celery()

app_object.config_from_object(celeryconfig)

from client.workers.timelineharvester import TimelineRetrievalTask

if __name__ == "__main__":
    app_object.start()

# I created a new class deriving from celery.Task, I call it using res = MyClass.apply([arg1], queue='myqueue')
# the problem is that it works for once but not twice from the same shell. btw, I run
# 'celery shell -A client.celery_app.endpoint -b amqp://myrabbitmq' on the commandline
# What do you think?
# The variables like self.myvar is updated in the second call, but returned 'res' is empty, i.e. res.info is
# empty res.status is 'SUCCESSFUL'