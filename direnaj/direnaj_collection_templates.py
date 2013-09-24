'''
    Contains definitions of document templates in direnaj collections

    Usage :
    <collection>_doc = drnj_doc(new_<collection>_document(), data_dict)

'''

# Change History :
# Date                                          Prog    Note
# Wed Sep 04 14:20:42 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

from drnj_time import py_utc_time2drnj_time

def drnj_doc(new_doc, data):
    """Copy the fields in data to the new document

    Typical usage is
    drnj_doc(new_queue_document(), {"id": 23932})

    - creates a database document with all fields and possible default values
    - and sets the field values from data
    - some fields in the new document may not be set

    raises a NameError exception if <data> contains fields that do not exist in <new_doc>

    :param new_doc: new document.
    :type new_doc: dict
    :param data: A dictionary which includes the fields to be filled in.
    :type data: dict
    :returns:  new_doc -- xxx.
    :raises: NameError

    """
    for fn in data.iterkeys():
        if new_doc.has_key(fn):
            new_doc[fn] = data[fn]
        else:
            raise NameError('Target doc does not contain the field {}'.format(fn))

    return new_doc

def drnj_copy2doc(new_doc, data):
    """Tries to set all the fields in new_doc from data

    Typical usage is
    ``drnj_copy2doc(new_queue_document(), {"id": 23932})``

    - creates a database document with all fields and possible default values
    - and sets the field values from data

    """
    for fn in new_doc.iterkeys():
        if data.has_key(fn):
            new_doc[fn] = data[fn]
        else:
            pass
            # print 'Target doc does not contain the field {}'.format(fn)

    return new_doc


def new_queue_document():
    rec = {
    "id": 0,
    "id_str": "0",
    "profile_retrieved_at": 0,
    "friends_retrieved_at": 0,
    "followers_retrieved_at": 0,
    "protected": False,
    "retrieved_by": "",
    "user_data": 0,             # Extra data relevant for the scheduling task
    }
    return rec

def new_profiles_document():
    rec = {
    "id": 0,
    "id_str": "0",
    "created_at": 0,
    "protected": False,
    "location": "",
    "screen_name": "",
    "name": "",
    "followers_count": 0,
    "friends_count": 0,
    "statuses_count": 0,
    "geo_enabled": 0,
    "profile_image_url": "",
    "record_retrieved_at": 0,
    "retrieved_by": "",
    }
    return rec

def new_profiles_history_document():
    return new_profiles_record()

# Fill time with py_utc_time2drnj_time("Thu May 30 14:20:16 +0000 2013")
def new_graph_document():
    rec = {
        "id": 0,
        "id_str": "0",
        "friend_id": 1,
        "friend_id_str": "1",
        "record_retrieved_at": 0,
        "retrieved_by": "",
        }
    return rec

def new_graph_history_document():
    rec = {
        "id": 0,
        "id_str": "0",
        "friend_id": 1,
        "friend_id_str": "1",
        "following": 1,
        "record_retrieved_at": 0,
        "retrieved_by": "",
        }
    return rec

