__author__ = 'onur'

from kombu import Queue

from direnaj.client.config.config import DIRENAJ_LOCAL_QUEUE

CELERY_ACKS_LATE = True
CELERYD_PREFETCH_MULTIPLIER = 1

BROKER_URL = 'amqp://%s' % "direnaj-staging.cmpe.boun.edu.tr"

# CELERY_DEFAULT_QUEUE = 'client.%s' % DIRENAJ_CLIENT_ID

CELERY_DEFAULT_EXCHANGE = 'campaigns'

CELERY_DEFAULT_EXCHANGE_TYPE = 'topic'

CELERY_QUEUES = (
    Queue('timelines', routing_key='*.timeline.*'),
    Queue('streamings', routing_key='*.streaming.*'),
    Queue(DIRENAJ_LOCAL_QUEUE, type='direct')
)