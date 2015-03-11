__author__ = 'onur'

# import logging
# import logging.handlers
#
# logger = logging.getLogger("httpstream")
# logger.addHandler(logging.handlers.RotatingFileHandler("neo4j.log", maxBytes=10000000, backupCount=10))

from py2neo import Graph, Node, neo4j, Relationship, watch
from py2neo.core import GraphError, ClientError

import time

import twitter

import bson.json_util

graph = Graph()
import logging
watch("httpstream", level=logging.WARN, out=open("neo4j.log", "w+"))

def upsert_user(user):
    """

    :param user: dictionary
    :return:
    """

    direnaj_api_logger.info('Upserting: ' + user['id_str'])

    # for key in user.keys():
    #     print key + ': ' + str(user[key]) + " - " + str(type(user[key]))

    # removing these beacuse neo4j doesn't allow nested nodes.
    if 'entities' in user:
        user['entities'] = bson.json_util.dumps(user['entities'])
    if 'status' in user:
        del user['status']

    # Find the related user. This looks complicated because of {'id_str': '', 'screen_name': 'blabla'} entries
    # coming from create_campaign
    user_node = None
    if 'id_str' in user and 'screen_name' in user and user['id_str'] != '' and user['screen_name'] != '':
        user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']}).one
        if not user_node:
            user_node = graph.cypher.execute("MATCH (u:User) WHERE u.screen_name = {screen_name} RETURN u", {'screen_name': user['screen_name']}).one
    # TODO: This is very nasty! Go get learn the proper way!
    elif (type(user['id_str']) == type('STRING') or type(user['id_str']) == type(u'STRING')) and user['id_str'] != '':
        user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']}).one
    elif (type(user['screen_name']) == type('STRING') or type(user['screen_name']) == type(u'STRING')) and user['screen_name'] != '':
        user_node = graph.cypher.execute("MATCH (u:User) WHERE u.screen_name = {screen_name} RETURN u", {'screen_name': user['screen_name']}).one

    print user_node

    if user_node:
        # First clearing the user_node properties.
        for key in user_node.properties.keys():
            user_node.properties[key] = None
        # then assigning the new properties
        for key in user.keys():
            user_node.properties[key] = user[key]
        user_node.push()
    else:
        user_node = Node.cast(user)
        user_node.labels.add("User")
        graph.create(user_node)
    return user_node


def update_task_state_in_watchlist(user, since_tweet_id, page_not_found):

    user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']}).one

    if user_node:
        # DELETE
        graph.cypher.execute("MATCH (u { id_str: {id_str} })<-[r:TIMELINE_TASK_STATE]-(task:TIMELINE_HARVESTER_TASK) "\
                             "SET r.since_tweet_id = {since_tweet_id}, r.page_not_found = {page_not_found}, r.state = 0, r.updated_at = {current_unix_time} "\
                             "RETURN r",
                             {'id_str': user['id_str'],
                              'current_unix_time': int(time.time()),
                              'since_tweet_id': since_tweet_id,
                              'page_not_found': page_not_found})

direnaj_api_logger = logging.getLogger("direnaj_api")

def init_user_to_graph_aux(campaign_node, user):

    user_node = upsert_user(user)
    campaign_rel = Relationship(campaign_node, "OBSERVES", user_node)
    try:
        graph.create_unique(campaign_rel)
    except (GraphError, ClientError), e:
        print("PROBABLY A UNIQUEPATHNOTUNIQUE error")

    timeline_task_state_rel = \
        graph.cypher.execute(
            "MATCH (u:User { id_str: {id_str} })<-[r:TIMELINE_TASK_STATE]-(task:TIMELINE_HARVESTER_TASK) RETURN r",
            {'id_str': user['id_str']}).one


    if timeline_task_state_rel:
        pass
    else:
        tx = graph.cypher.begin()
        #tx.append("MATCH (t:TIMELINE_HARVESTER_TASK {id: 1}) RETURN t")
        #tx.append("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']})
        #tx.append("CREATE (t)-[r:TIMELINE_TASK_STATE]->(u:User) RETURN r")
        tx.append("MATCH (u:User),(t:TIMELINE_HARVESTER_TASK) WHERE u.id_str = {id_str} AND t.id = 1 CREATE (u)<-[r:TIMELINE_TASK_STATE {since_tweet_id: -1, page_not_found: -1, state: 0, unlock_time: -1}]-(t) RETURN r", {'id_str': user['id_str']})
        tx.commit()



    friendfollower_task_state_rel = \
        graph.cypher.execute(
            "MATCH (u:User { id_str: {id_str} })<-[r:FRIENDFOLLOWER_TASK_STATE]-(task:FRIENDFOLLOWER_HARVESTER_TASK) RETURN r",
            {'id_str': user['id_str']}).one



    if friendfollower_task_state_rel:
        pass
    else:
        tx = graph.cypher.begin()
        #tx.append("MATCH (t:FRIENDFOLLOWER_HARVESTER_TASK {id: 1}) RETURN t")
        #tx.append("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']})
        #tx.append("CREATE (t)-[r:FRIENDFOLLOWER_TASK_STATE]->(u:User)")
        tx.append("MATCH (u:User),(t:FRIENDFOLLOWER_HARVESTER_TASK) WHERE u.id_str = {id_str} AND t.id = 1 CREATE (u)<-[r:FRIENDFOLLOWER_TASK_STATE {state: 0, unlock_time: -1}]-(t) RETURN r", {'id_str': user['id_str']})
        tx.commit()

    return user_node


def upsert_campaign(campaign_id):
    campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                         {'campaign_id': campaign_id}).one
    print campaign_node
    if not campaign_node:
        campaign_node = graph.cypher.execute("CREATE (c:Campaign {campaign_id: {campaign_id}}) RETURN c",
                                             {'campaign_id': campaign_id}).one
    return campaign_node


def init_user_to_graph(tweets):

    prev_campaign_id = ""

    if tweets:

        sample_tweet = tweets[0]
        campaign_id = sample_tweet['campaign_id']

        campaign_node = upsert_campaign(campaign_id)

        for tweet in tweets:
            user = tweet['tweet']['user']

            init_user_to_graph_aux(campaign_node, user)

def get_users_attached_to_campaign(campaign_id, skip, limit):
    attached_users_array = []

    campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                         {'campaign_id': campaign_id}).one

    if campaign_node:
        # "MATCH (user)<-[r2:FRIENDFOLLOWER_TASK_STATE|TIMELINE_TASK_STATE]-(x) RETURN user,collect(DISTINCT r2)"
        user_nodes = graph.cypher.execute("MATCH (c:Campaign { campaign_id: {campaign_id} })-[r:OBSERVES]->(user)<-[r2:FRIENDFOLLOWER_TASK_STATE|TIMELINE_TASK_STATE]-(x) RETURN user,collect(DISTINCT r2) as rlist SKIP {skip} LIMIT {limit}",
                                         {'campaign_id': campaign_id,
                                          'skip': int(skip),
                                          'limit': int(limit)})
        for user in user_nodes:
            attached_users_array += [[user.user.properties, user.rlist]]

    else:
        attached_users_array = []

    return attached_users_array

def add_to_watchlist(campaign_id, user_id_strs_to_follow, user_screen_names_to_follow):
    users_to_be_added = []

    if user_id_strs_to_follow:
        users_to_be_added = prepare_users_to_be_added(user_id_strs_to_follow)
    if user_screen_names_to_follow:
        users_to_be_added += prepare_users_to_be_added(user_screen_names_to_follow, dtype='screen_name')

    for user in users_to_be_added:

        campaign_node = upsert_campaign(campaign_id)

        print user
        print campaign_id
        print campaign_node

        user['user'] = get_user_object_from_twitter(user['user']).AsDict()
        user['user']['id_str'] = str(user['user']['id'])
        init_user_to_graph_aux(campaign_node, user['user'])

def get_user_object_from_twitter(user):

    print 'get user object Giris'

    access_credentials = {
    'consumer_key': "SyyLuKNdeJ9lFeItE0Bg",
    'consumer_secret': "eOqzcigVBydHqDTaQLP1fcQY7wFZXPZICIBuIgOnb4",
    'access_token_key': "2952826882-eEu785g7zdoRht3mopE8XWk9p7mWFXw7Hr6tYdK",
    'access_token_secret': "f3Fcqtt7nwFqBmZcHRfS41kNx4DIiJJMS2YBLJjwvP4wB"
    }

    print 'api yaratiliyor'
    api = twitter.Api(**access_credentials)
    print api

    user_object = None
    if 'id_str' in user and user['id_str'] != '':
        user_object = api.GetUser(user_id=user['id_str'])
    elif 'screen_name' in user and user['screen_name'] != '':
        user_object = api.GetUser(screen_name=user['screen_name'])

    print user_object
    print 'get user object Cikis'
    return user_object

def prepare_users_to_be_added(user_id_strs_to_follow, dtype='id_str'):
    #self.logger.debug('prepare_users_to_be_added: %s' % user_id_strs_to_follow)

    users_to_be_added = []

    lines = user_id_strs_to_follow.split('\n')
    for line in lines:
        line = line.strip()

        if dtype == 'id_str':
            user = {'user': {'id_str': line, 'screen_name': ''}}
        else:
            user = {'user': {'id_str': '', 'screen_name': line}}

        users_to_be_added.append(user)

    return users_to_be_added
    # lines = user_id_strs_to_follow.split('\n')
    # print lines
    # users_to_be_added = []
    # for line in lines:
    #     line_els = [l.strip() for l in line.split(',')]
    #     print line_els
    #     # first element must be user_id_str
    #     # second element is optional. it should be the username for easier management by humans.
    #     users_to_be_added.append(line_els[0])

def create_batch_from_watchlist(app_object):
    n_users = {'timeLineTask': 2 , 'userInfoTask': 12, 'friendFollowerTask': 1}
    # Using pymongo (thus synchronous) because motor caused problems sat the first try.
    # it doesn't matter for now as this code is run from a celery client on the server.

    # first, find locked edges with overdue time
    print 'Current Unix Time {}'.format(int(time.time()))
    graph.cypher.execute("MATCH (u:User)<-[r:TIMELINE_TASK_STATE|FRIENDFOLLOWER_TASK_STATE]-(t) WHERE r.state = 1 AND r.unlock_time < {current_unix_time} SET r.state = 0, r.unlock_time = -1, r.updated_at = {current_unix_time}", {'current_unix_time': int(time.time())})


    timeline_task_states = graph.cypher.execute("MATCH (u:User)<-[r:TIMELINE_TASK_STATE]-(t) WHERE r.state = 0 and (r.unlock_time = -1 OR r.unlock_time < {current_unix_time}) WITH r ORDER BY r.updated_at LIMIT {n_users} MATCH (u2:User)<-[r]-(t2) SET r.state = 1, r.unlock_time = {unix_time_plus_two_hours} RETURN DISTINCT r", {'n_users': n_users['timeLineTask'], 'unix_time_plus_two_hours': int(time.time())+ (2*3600),'current_unix_time': int(time.time())})
    print timeline_task_states

    ### Now, use this batch_array to call TimelineRetrievalTask.
    res_array = []

    # # FIXME: burada kalmistik. sanirim id_str ve screen_name bos geliyor.
    for task_state in timeline_task_states:
         print task_state.r.nodes[0].labels

         id_str = task_state.r.nodes[1].properties['id_str']
         if id_str == None:
             id_str = ''
         screen_name = task_state.r.nodes[1].properties['screen_name']
         if screen_name == None:
             screen_name = ''
         since_tweet_id = task_state.r.properties['since_tweet_id']
         page_not_found = task_state.r.properties['page_not_found']

         job_definition = [{'id_str': id_str, 'screen_name': screen_name}, since_tweet_id, page_not_found]
         res = app_object.send_task('timeline_retrieve_userlist', [[job_definition]], queue='timelines')
         res_array.append(res)


    res_array = []
    ff_task_states = graph.cypher.execute("MATCH (u:User)<-[r:FRIENDFOLLOWER_TASK_STATE]-(t) WHERE (u.followers_count < 300000 AND u.friends_count < 300000) and r.state = 0 and (r.unlock_time = -1 "
                                          "OR r.unlock_time < {current_unix_time}) WITH r ORDER BY r.updated_at LIMIT {n_users}"
                                          " MATCH (u2:User)<-[r]-(t2) SET r.state = 1, r.unlock_time = {unix_time_plus_two_hours} RETURN DISTINCT r",
                                          {'n_users': n_users['friendFollowerTask'], 'unix_time_plus_two_hours': int(time.time())+ (2*3600),'current_unix_time': int(time.time())})
    print ff_task_states

    for ff_task_state in ff_task_states:
        id_str = ff_task_state.r.nodes[1].properties['id_str']
        if id_str == None:
            id_str = ''
        screen_name = ff_task_state.r.nodes[1].properties['screen_name']
        if screen_name == None:
            screen_name = ''

        page_not_found = ff_task_state.r.properties['page_not_found']
        job_definition = [{'id_str': id_str, 'screen_name': screen_name}, page_not_found]
        res = app_object.send_task('crawl_friends_or_followers', [[job_definition]], queue='friendfollowers')
        res_array.append(res)


    res_array = []
    userInfo_task_states = graph.cypher.execute("MATCH (u:User)<-[r:USER_INFO_HARVESTER_TASK_STATE]-(t) WHERE r.state = 0 WITH r ORDER BY r.updated_at LIMIT {n_users} "
                                                "MATCH (u2:User)<-[r]-(t2) SET r.state = 1 RETURN DISTINCT r",
                                                {'n_users': n_users['userInfoTask']})
    print userInfo_task_states

    for userInfo_task_state in userInfo_task_states:
        id_str = userInfo_task_state.r.nodes[1].properties['id_str']
        if id_str == None:
            id_str = ''
        screen_name = userInfo_task_state.r.nodes[1].properties['screen_name']
        if screen_name == None:
            screen_name = ''

        page_not_found = userInfo_task_state.r.properties['page_not_found']
        job_definition = [{'id_str': id_str, 'screen_name': screen_name}, page_not_found]
        res = app_object.send_task('crawl_user_info', [[job_definition]], queue='userinfo')
        res_array.append(res)
