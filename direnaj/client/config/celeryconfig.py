__author__ = 'onur'

from client.config.config import DIRENAJ_CLIENT_ID

from kombu import Queue

BROKER_URL = 'amqp://%s' % "direnaj-staging.cmpe.boun.edu.tr"

# CELERY_DEFAULT_QUEUE = 'client.%s' % DIRENAJ_CLIENT_ID

CELERY_DEFAULT_EXCHANGE = 'campaigns'

CELERY_DEFAULT_EXCHANGE_TYPE = 'topic'

CELERY_QUEUES = (
    Queue('timelines', routing_key='*.timeline.*'),
    Queue('streamings', routing_key='*.streaming.*'),
)