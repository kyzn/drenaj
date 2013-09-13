# This file is a template for config.py
# It is intended to be read and filled in programmatically by configure.py
# using information contained in config.yaml

import os

PROJECT_ROOT_DIR=${project_root_dir}
MONGO_BIN_DIR=${mongo_bin_dir}

# These are the default settings
MONGO_HOST=${mongo_host}
MONGO_PORT=${mongo_port}

DIRENAJ_APP_ENVIRONMENT = ${direnaj_app_environment}

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
DB_TEST_VERSION=0.2

class KeyStore:
    def __init__(self):
        # These two variables are associated with an application.
        # These are the keys for Direnaj App
        self.app_consumer_key = "SyyLuKNdeJ9lFeItE0Bg"
        self.app_consumer_secret = "eOqzcigVBydHqDTaQLP1fcQY7wFZXPZICIBuIgOnb4"
        # Obtain the following from Twitter, see project wiki
        self.access_token_key = ${access_token_key}
        self.access_token_secret = ${access_token_secret}

        # direnaj passwords
        auth_user_id = ${auth_user_id}
        auth_password = ${auth_password}

        self.direnaj_auth_secrets = { 'auth_user_id':auth_user_id,
                              'auth_password':auth_password}
