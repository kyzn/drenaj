__author__ = 'onur'

from kombu import Queue

CELERY_ACKS_LATE = True
CELERYD_PREFETCH_MULTIPLIER = 1

BROKER_URL = 'amqp://%s' % "direnaj-staging.cmpe.boun.edu.tr"

CELERY_DEFAULT_QUEUE = 'control'

CELERY_DEFAULT_EXCHANGE = 'campaigns'

CELERY_DEFAULT_EXCHANGE_TYPE = 'topic'

CELERY_QUEUES = (
    Queue('timelines', routing_key='*.timeline.*'),
    Queue('streamings', routing_key='*.streaming.*'),
    Queue('control', routing_key='control'),
)

from datetime import timedelta

CELERYBEAT_SCHEDULE = {
    'dispatch_timeline_harvester_tasks_every_three_minutes': {
        'task': 'check_watchlist_and_dispatch_tasks',
        'schedule': timedelta(seconds=60*3),
    },
}