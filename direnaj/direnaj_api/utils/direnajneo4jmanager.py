__author__ = 'onur'

from py2neo import Graph, Node, neo4j, Relationship

graph = Graph()

def upsert_user(user):
    """

    :param user: dictionary
    :return:
    """

    # removing these beacuse neo4j doesn't allow nested nodes.
    if 'entities' in user:
        del user['entities']
    if 'status' in user:
        del user['status']

    # burada bir hata var.
    user_node = None
    if type(user['id_str']) == type('STRING') and user['id_str'] != '':
        user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']}).one
    elif type(user['screen_name']) == type('STRING') and user['screen_name'] != '':
        user_node = graph.cypher.execute("MATCH (u:User) WHERE u.screen_name = {screen_name} RETURN u", {'screen_name': user['screen_name']}).one

    if user_node:
        for key in user.keys():
            if type(user[key]) == type('STRING') and not user[key] == '':
                user_node.properties[key] = user[key]
        user_node.push()
    else:
        user_node = Node.cast(user)
        user_node.labels.add("User")
        graph.create(user_node)
    return user_node


def update_task_state_in_watchlist(user, since_tweet_id, page_not_found):

    user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': user['id_str']}).one.u

    if user_node:
        # DELETE
        tx = graph.cypher.begin()
        tx.append("MATCH (u { id_str: {id_str} })<-[r:TIMELINE_TASK_STATE]-(task:TIMELINE_HARVESTER_TASK) DELETE r", {'id_str': user['id_str']})
        tx.append("CREATE (u)<-[r:TIMELINE_TASK_STATE {since_tweet_id: {since_tweet_id}, page_not_found: {page_not_found}]-(task:TIMELINE_HARVESTER_TASK)",
                  {'since_tweet_id': since_tweet_id, 'page_not_found': page_not_found})
        tx.commit()


def init_user_to_graph_aux(campaign_node, user):

    user_node = upsert_user(user)
    campaign_rel = Relationship(campaign_node, "OBSERVES", user_node)
    graph.create(campaign_rel)
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
        tx.append("MATCH (u:User),(t:TIMELINE_HARVESTER_TASK) WHERE u.id_str = {id_str} AND t.id = 1 CREATE (u)<-[r:TIMELINE_TASK_STATE]-(t) RETURN r", {'id_str': user['id_str']})
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
        tx.append("MATCH (u:User),(t:FRIENDFOLLOWER_HARVESTER_TASK) WHERE u.id_str = {id_str} AND t.id = 1 CREATE (u)<-[r:FRIENDFOLLOWER_TASK_STATE]-(t) RETURN r", {'id_str': user['id_str']})
        tx.commit()


def init_user_to_graph(tweets):

    prev_campaign_id = ""

    if tweets:

        sample_tweet = tweets[0]
        campaign_id = sample_tweet['campaign_id']

        campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                             {'campaign_id': campaign_id}).one
        print campaign_node
        if not campaign_node:
            campaign_node = graph.cypher.execute("CREATE (c:Campaign {campaign_id: {campaign_id}}) RETURN c",
                                                 {'campaign_id': campaign_id}).one

        for tweet in tweets:
            user = tweet['tweet']['user']

            init_user_to_graph_aux(campaign_node, user)

def get_users_attached_to_campaign(campaign_id):
    attached_users_array = []

    campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                         {'campaign_id': campaign_id}).one

    if campaign_node:
        user_nodes = graph.cypher.execute("MATCH (c:Campaign { campaign_id: {campaign_id} })-[:OBSERVES]->(user)<-[state:TIMELINE_TASK_STATE]-(t:TIMELINE_HARVESTER_TASK) RETURN user,state",
                                         {'campaign_id': campaign_id})
        for user in user_nodes:
            attached_users_array += [user.screen_name, user.state]

    else:
        attached_users_array = []

    return attached_users_array

def add_to_watchlist(self, campaign_id, user_id_strs_to_follow, user_screen_names_to_follow):
    users_to_be_added = []

    if user_id_strs_to_follow:
        users_to_be_added = self.prepare_users_to_be_added(user_id_strs_to_follow)
    if user_screen_names_to_follow:
        users_to_be_added += self.prepare_users_to_be_added(user_screen_names_to_follow, dtype='screen_name')

    for user in users_to_be_added:

        campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                         {'campaign_id': campaign_id}).one
        print user
        print campaign_id
        print campaign_node
        if not campaign_node:
            campaign_node = graph.cypher.execute("CREATE (c:Campaign {campaign_id: {campaign_id}}) RETURN c",
                                             {'campaign_id': campaign_id}).one

        init_user_to_graph_aux(campaign_node, user['user'])

def prepare_users_to_be_added(self, user_id_strs_to_follow, dtype='id_str'):
    self.logger.debug('prepare_users_to_be_added: %s' % user_id_strs_to_follow)

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