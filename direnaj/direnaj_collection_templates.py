'''
    Contains definitions of document templates in direnaj collections

    Usage :
    >>>> <collection_item>_doc = validate_document(new_<collection_item>_template(), data_dict)

    i.e.

    >>>> tweet_doc = validate_document(new_tweet_template(), { 'id_str': 'adadasd'})


'''

# Change History :
# Date                                          Prog    Note
# Wed Sep 04 14:20:42 2013                      ATC     Created, Python 2.7.3

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr

from drnj_time import py_utc_time2drnj_time

def validate_document(document_template, document_data, fail=True):

    """
    Creates a ``dict`` object which can be inserted to the database using a template.

    These templates are defined in :py:mod:`direnaj_collection_templates`

    Typical usage is

    >>> validate_document(new_queue_document(), {"id": 23932})

    This functions
    - initializes a base object from `document_template`
    - sets the field values from `document_data`
    - some fields in the `document_template` may not be set

    raises a NameError exception if `document_data` contains fields that do not
    exist in `document_template`

    :param document_template: a `dict` which contains a document template
    :type document_template: dict
    :param document_data: a `dict` which includes the (field, value) pairs to be filled in.
    :type document_data: dict
    :returns: a ``dict`` object which contains all the required fields filled with the user supplied values. default values are used if necessary.
    :rtype: dict
    :raises: NameError

    """

    new_document = {}
    for field in document_data.iterkeys():
        if field in document_template:
            template_value = document_template[field]
            if type(template_value) == dict:
                new_document[field] = validate_document(document_template[field], document_data[field], fail)
            elif type(template_value) == True:
                #: accept anything
                new_document[field] = document_data[field]
            elif type(template_value) == type(lambda x: x):
                #: if template entry is a function
                #: transform the value with the function
                # new_document[field] = apply(template_value, [field, document_data[field]])
                new_document[field] = template_value(field, document_data[field])
            else:
                #: if no rules are defined accept.
                new_document[field] = document_data[field]
        else:
            if fail:
                #: fail when a field which does not exist in the template is attempted.
                raise NameError('Target doc does not contain the field {}'.format(field))
            else:
                pass

    return new_document

def is_drnj_time(field_name, value):
    if type(value) == float:
        return value
    elif type(value) == str or type(value) == unicode:
        return py_utc_time2drnj_time(value)
    else:
        raise TypeError('Field: ''%s'' is not of type drnj_time' % field_name)

def is_boolean(field_name, value):
    if type(value) == bool:
        return value
    else:
        raise TypeError('Field: ''%s'' is not of type bool' % field_name)

def is_string(field_name, string):
    if string == None:
        return string
    elif type(string) == str or type(string) == unicode:
        return string
    else:
        raise TypeError('Field: ''%s'' is not of type string' % field_name)

def is_integer(field_name, number):
    if type(number) == int or type(number) == long:
        return number
    elif type(number) == type(None):
        return None
    else:
        raise TypeError('Field: ''%s'' is not of type int' % field_name)

def nullify(field_name, value):
    return None

def new_tweet_template():
    '''
    Tweet object template in our database.

    True: accept anything
    nullify: transform everything into null

    '''
    template = {
        "tweet": {
            "id" : is_integer,
            "id_str": is_string,
            "text":  is_string,
            "user_id_str" : is_string, #: a foreign key into `profiles'
            "coordinates": True,
            "geometry": {
                "geo": nullify,
                "coordinates": True,
                "place": True,
            },
            "in_reply_to_status_id_str": is_string,
            "retweet_count": is_integer,
            "favorite_count": is_integer,
            "favorited": is_boolean,
            "retweeted": is_boolean,
            "truncated": is_boolean,
            "retweeted_status_id_str": is_string,
            "lang": is_string, #: lang: <enum: {"tr", "en", etc.}>,
            "filter_level": is_string, #: filter_level: <enum: {"medium", "low", etc.}>,
            "entities": {
                "hashtags": True,
                "media": True,
                "urls": True,
                "user_mentions": True
            },
            "user": True,
            "source_id": is_integer, #: reference to `sources' collection.
            "created_at": is_drnj_time,
        },
        "campaign_id": is_string,
        "direnaj_service_version": True,
        "retrieved_by": True,
        "record_retrieved_at": is_drnj_time
    }

    return template


def new_profile_document():

    template = {
        "profile": {
            "id": is_integer,
            "id_str": is_string,

            "screen_name": is_string,
            "name": is_string,
            "description": is_string,

            "followers_count": is_integer,
            "friends_count": is_integer,
            "statuses_count": is_integer,
            "favourites_count": is_integer,
            "listed_count": is_integer,

            "geo_enabled": is_boolean,
            "utc_offset": is_integer,
            "time_zone": is_string,
            "location": is_string,
            "lang": is_string,

            "verified": is_boolean,
            "protected": is_boolean,

            "profile_image_url": is_string,
            "profile_background_image_url": is_string,
            "profile_banner_url": is_string,

            "url": is_string,

            "entities": {
                "hashtags": True,
                "media": True,
                "urls": True,
                "user_mentions": True
            },

            "created_at": is_drnj_time,
        },
        "record_retrieved_at": is_drnj_time,
        "retrieved_by": is_string,
    }

    return template

def new_hashtag_template():

    template = {
        "hashtag": True,
        "status_id_str": is_string,
        "campaign_id": is_string,
        "created_at": is_drnj_time,
    }

    return template

def new_url_template():

    template = {
        "url": True,
        "status_id_str": is_string,
        "campaign_id": is_string,
        "created_at": is_drnj_time,
    }

    return template

def new_user_mention_template():

    template = {
        "user_mention": True,
        "status_id_str": is_string,
        "campaign_id": is_string,
        "created_at": is_drnj_time,
    }

    return template

def new_media_template():

    template = {
        "media": True,
        "status_id_str": is_string,
        "campaign_id": is_string,
        "created_at": is_drnj_time,
    }

    return template


def new_coordinates_template():

    template = {
        "coordinates": True,
        "status_id_str": is_string,
        "campaign_id": is_string,
        "created_at": is_drnj_time,
    }

    return template


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
    "campaign_id": is_string,
    }
    return rec

## def new_profiles_document():
##     rec = {
##     "id": 0,
##     "id_str": "0",
##     "created_at": 0,
##     "protected": False,
##     "location": "",
##     "screen_name": "",
##     "name": "",
##     "followers_count": 0,
##     "friends_count": 0,
##     "statuses_count": 0,
##     "geo_enabled": 0,
##     "profile_image_url": "",
##     "record_retrieved_at": 0,
##     "retrieved_by": "",
##     }
##     return rec

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

