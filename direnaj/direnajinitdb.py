from config import *

import random

from direnajmongomanager import *

graph_template = {
        "id_str": "461494325",
        "friend_id_str": "494494325",
        "following": 1,
        "record_retrieved_at": "Thu May 30 14:20:16 +0000 2013",
        "retrieved_by": 'dummy_data',
        }

user_template = {
        "id_str": "461494325",
        "protected": False,
        "location": "Istanbul",
        "screen_name": "AliTaylanCemgil",
        "name": "Ali Taylan Cemgil",
        "followers_count": 155,
        "friends_count": 68,
        "statuses_count": 111,
        "geo_enabled": 1,
        "profile_image_url": "http:\/\/a0.twimg.com\/profile_images\/3580500548\/0e33ddc524",
        "record_retrieved_at": "Thu May 30 14:20:16 +0000 2013",
        "retrieved_by": 'dummy_data',
        }

def drop_all_collections(environment):
    mongo_client[DIRENAJ_DB[environment]]['graph'].drop()
    mongo_client[DIRENAJ_DB[environment]]['users'].drop()

def dump_db(environment, version):
    # TODO: make sure PROJECT_ROOT_DIR/db is there (in a portable way)
    os.system('mongodump -h %s --port %s --db %s --out %s/db/%s'
            % (MONGO_HOST, MONGO_PORT, DIRENAJ_DB[environment], PROJECT_ROOT_DIR, version) )

def restore_db(from_environment, to_environment, version):
    # TODO: make sure PROJECT_ROOT_DIR/db is there (in a portable way)
    os.system('mongorestore -h %s --port %s --db %s --drop %s/db/%s/%s'
            % (MONGO_HOST, MONGO_PORT, DIRENAJ_DB[to_environment], PROJECT_ROOT_DIR, version, DIRENAJ_DB[from_environment]) )

def init_graphs(**keywords):
    payload = []

    method = keywords['method']
    environment = keywords['environment'] if 'environment' in keywords.keys() else 'test'

    if method == 'randomly':
        n_samples = 100
        sample_count = 1
        while sample_count < n_samples:
            tmp = graph_template.copy()
            tmp['id_str'] = str(int(tmp['id_str']) + random.randint(10,50))
            tmp['friend_id_str'] = str(int(tmp['id_str']) + random.randint(10,50))
            tmp['following'] = random.randint(0,1)

            # insert both the id_str and friend_id_str in users collection.
            user_clones = [user_template.copy() for i in range(0, 2)]
            user_clones[0]['id_str'] = tmp['id_str']
            user_clones[1]['id_str'] = tmp['friend_id_str']
            init_users(method='manual_input', input_array=user_clones)

            payload.append(tmp)
            sample_count += 1
    else:
        print 'NOT IMPLEMENTED YET'

    graph_coll = mongo_client[DIRENAJ_DB[environment]]['graph']
    graph_coll.insert(payload, w=1)
    return

def init_users(**keywords):
    payload = []

    method = keywords['method']
    environment = keywords['environment'] if 'environment' in keywords.keys() else 'test'

    id_str_list_for_query = []

    if method == 'randomly':
        n_samples = 100
        sample_count = 1
        while sample_count < n_samples:
            tmp = user_template.copy()
            tmp['id_str'] = str(int(tmp['id_str']) + random.randint(10,25))
            id_str_list_for_query.append(tmp['id_str'])
            payload.append(tmp)
            sample_count += 1
    elif method == 'manual_input':
        id_str_list_for_query = [x['id_str'] for x in keywords['input_array']]
        payload = keywords['input_array']
    else:
        print 'NOT IMPLEMENTED YET'

    users_coll = mongo_client[DIRENAJ_DB][environment]['users']
    for idx, id_str in enumerate(id_str_list_for_query):
        print idx
        print id_str
        users_coll.update({'id_str': id_str}, payload[idx], w=1, upsert=True)
    return
