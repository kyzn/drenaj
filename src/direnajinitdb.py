from config import *

import random

from direnajmongomanager import *

def init_graphs(**keywords):
    print 'WARNING: this is not working at all right now!'
    payload = []
    template = {
            "id_str": "461494325",
            "friend_id_str": "494494325",
            "following": 1,
            "record_retrieved_at": "Thu May 30 14:20:16 +0000 2013",
            "retrieved_by": 'dummy_data',
            }

    method = keywords['method']

    if method == 'randomly':
        n_samples = 100
        sample_count = 1
        while sample_count < n_samples:
            tmp = template.copy()
            tmp['id_str'] = str(int(tmp['id_str']) + random.randint(10,50))
            tmp['friend_id_str'] = str(int(tmp['id_str']) + random.randint(10,50))
            tmp['following'] = random.randint(0,1)
            payload.append(tmp)
            sample_count += 1
    else:
        print 'NOT IMPLEMENTED YET'

    graph_coll = mongo_client[DIRENAJ_DB]['graph']
    graph_coll.insert(payload, w=1)
    return
