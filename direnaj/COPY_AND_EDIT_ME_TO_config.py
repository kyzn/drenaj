# This file contains personal preference information for each developer
# Copy this file to config.py (which is untracked .gitignore)
# Edit your preferences and authentication information
#
# 14-Aug-2013	 9:29 PM	ATC

import os

PROJECT_ROOT_DIR='/home/onur/Desktop/projects/research/projects/direnaj/repo'
MONGO_BIN_DIR=

# These are the default settings
MONGO_HOST='localhost'
MONGO_PORT=27017

DIRENAJ_APP_ENVIRONMENT = 'development'

# Direnaj Database
# DIRENAJ_DB = 'twitter_test'
DIRENAJ_DB = {}
DIRENAJ_DB['production'] = 'direnaj_prod'
DIRENAJ_DB['development'] = 'direnaj_dev'
DIRENAJ_DB['test'] = 'direnaj_test'

DIRENAJ_APP_HOST = 'localhost'
DIRENAJ_APP_PORT = {}
DIRENAJ_APP_PORT['production'] = 9998
DIRENAJ_APP_PORT['development'] = 9999
DIRENAJ_APP_PORT['test'] = 10000

## Testing related
DB_TEST_VERSION=0.1

# Obtain these from Direnaj, contact developers
auth_user_id = 'direnaj'
auth_password = 'tamtam'

# Obtain these from Twitter, see project wiki
consumer_key = " ... "
consumer_secret = " ... "
access_token_key = " ... "
access_token_secret = " ... "
