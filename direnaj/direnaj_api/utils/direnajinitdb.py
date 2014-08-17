# # This module contains the
# #
# #  function generating valid document templates according to the the database schema
# #  new_<collection_name>_document()
# #
# #  db initialization code
# #
#
# import os
# from direnaj_api.utils.direnajmongomanager import *
#
# from direnaj_api.config.config import DIRENAJ_DB, MONGO_BIN_DIR, MONGO_HOST, MONGO_PORT, PROJECT_ROOT_DIR
#
#
# def drop_all_collections(environment):
#     mongo_client[DIRENAJ_DB[environment]]['graph'].drop()
#     mongo_client[DIRENAJ_DB[environment]]['profiles'].drop()
#     mongo_client[DIRENAJ_DB[environment]]['profiles_history'].drop()
#     mongo_client[DIRENAJ_DB[environment]]['graph'].drop()
#     mongo_client[DIRENAJ_DB[environment]]['graph_history'].drop()
#
# def dump_db(environment, version):
#     # TODO: make sure PROJECT_ROOT_DIR/db is there (in a portable way)
#     # FIXING: with environment variable for mongo bin path environment variable, delete comment if accepted
#     os.system('%s/mongodump -h %s --port %s --db %s --out %s/db/%s'
#             % (MONGO_BIN_DIR, MONGO_HOST, MONGO_PORT, DIRENAJ_DB[environment], PROJECT_ROOT_DIR, version) )
#
# def restore_db(from_environment, to_environment, version):
#     # TODO: make sure PROJECT_ROOT_DIR/db is there (in a portable way)
#     # FIXING: with environment variable for mongo bin path environment variable, delete comment if accepted
#     os.system('%s/mongorestore -h %s --port %s --db %s --drop %s/db/%s/%s'
#             % (MONGO_BIN_DIR, MONGO_HOST, MONGO_PORT, DIRENAJ_DB[to_environment], PROJECT_ROOT_DIR, version, DIRENAJ_DB[from_environment]) )
#
