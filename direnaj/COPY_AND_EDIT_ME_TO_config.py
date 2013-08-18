# This file contains personal preference information for each developer
# Copy this file to config.py (which is untracked .gitignore)
# Edit your preferences and authentication information
#
# 14-Aug-2013	 9:29 PM	ATC

import os

PROJECT_ROOT_DIR='/home/onur/Desktop/projects/research/projects/direnaj/repo'

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

# Obtain these from Twitter, see project wiki
consumer_key = "hQUgcQ8GffQb9lcteoLy9g"
consumer_secret = "ADyiEvLtupE6J8JRo9J8onJ9oHgOaq7KFjKpa6QoDk"
access_token_key = "461494325-1L0bBBGL4okKhwYKChNM2cVbG1tp5hh5w9xIvblP"
access_token_secret = "W5W9PHcWBYTBrVJWbLpOOEZAZXGeGU3edBCUzwXR4"
