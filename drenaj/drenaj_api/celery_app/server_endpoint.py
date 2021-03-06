__author__ = 'onur'

from celery import Celery

import drenaj_api.config.server_celeryconfig as celeryconfig

app_object = Celery()

app_object.config_from_object(celeryconfig)

@app_object.task
def deneme(x, seconds):
    print "Sleeping for printing %s for %s seconds.." % (x, seconds)
    import time
    time.sleep(seconds)
    print x

from celery.utils.log import get_task_logger

logger = get_task_logger(__name__)

from drenaj_api.utils.drenajneo4jmanager import create_batch_from_watchlist
from drenaj_api.app import Application

#@periodic_task(run_every=crontab(minute='*/1'))

@app_object.task(name='check_watchlist_and_dispatch_tasks')
def check_watchlist_and_dispatch_tasks():

    # we can give an optional parameter for specific settings for this use.
    app = Application()

    # TODO: This might break!!! (and pycharm doesn't seem to recognize WARNs)
    create_batch_from_watchlist(app_object)
    #app.db.create_batch_from_watchlist(app_object, batch_size)
    #create_batch_from_watchlist(app_object, batch_size)

from py2neo import Graph, GraphError, Relationship, watch, Node
from drenaj_api.utils.drenajneo4jmanager import upsert_user
import time, bson
graph = Graph()

import logging
watch("httpstream", level=logging.WARN, out=open("neo4j-client.log", "w+"))

# this is required to handle long commits..
from py2neo.packages.httpstream import http
http.socket_timeout = 9999

@app_object.task(name='init_user_to_graph_offline')
def init_user_to_graph_offline(args):
    campaign_id, user_objects_str = args
    user_objects = bson.json_util.loads(user_objects_str)

    #tx = graph.cypher.begin()
    #tx.append("MERGE (c:Campaign {campaign_id: {campaign_id}}) RETURN c", {'campaign_id': campaign_id})

    #tx.commit()

    for user in user_objects:

        user_node = upsert_user(user)

        tx = graph.cypher.begin()

        tx.append("MATCH (c:Campaign {campaign_id: {campaign_id}}) WITH c LIMIT 1 MERGE (c)-[r:OBSERVES]->(u:User {id_str: {id_str}}) RETURN r", {'campaign_id': campaign_id,
                                                                                                                         'id_str': user['id_str']})

        tx.append("MERGE (u:User { id_str: {id_str} })<-[r:TIMELINE_TASK_STATE]-(task:TIMELINE_HARVESTER_TASK) "
                  "ON CREATE SET r.since_tweet_id = -1, r.page_not_found = -1, r.state = 0, r.unlock_time = {unlock_time} RETURN r",
                  {'id_str': user['id_str'], 'unlock_time': int(time.time())+(2*3600)})

        tx.append("MERGE (u:User { id_str: {id_str} })<-[r:FRIENDFOLLOWER_TASK_STATE]-(task:FRIENDFOLLOWER_HARVESTER_TASK) "
                  "ON CREATE SET r.state = 0, r.unlock_time = {unlock_time} RETURN r",
                  {'id_str': user['id_str'], 'unlock_time': int(time.time())+(2*3600)})
        tx.commit()

        #init_user_to_graph_aux(campaign_node, user)

@app_object.task(name='store_friendsfollowers_in_neo4j_offline')
def store_friendsfollowers_in_neo4j_offline(args):
    id_str, campaign_id, user_objects_str, friends_or_followers = args
    user_objects = bson.json_util.loads(user_objects_str)

    friends_or_followers_update_statement = []
    if friends_or_followers == "followers":
        friends_or_followers_update_statement += ["MATCH (u:User {id_str: '%s'})<-[r:FOLLOWS]-(u2) DELETE r" % (id_str)]
    elif friends_or_followers == "friends":
        friends_or_followers_update_statement += ["MATCH (u:User {id_str: '%s'})-[r:FOLLOWS]->(u2) DELETE r" % (id_str)]
    for user in user_objects:
        if friends_or_followers == "followers":
            friends_or_followers_update_statement += ["MERGE (u:User {id_str: '%s'})<-[r:FOLLOWS]-(u2:User {id_str: '%s'})<-[r2:USER_INFO_HARVESTER_TASK_STATE {state: 0, updated_at: %d}]-(t2:USER_INFO_HARVESTER_TASK {id: 1}) RETURN u2.id_str" \
                                                     % (id_str, user['id_str'], int(time.time())) ]
        elif friends_or_followers == "friends":
            friends_or_followers_update_statement += ["MERGE (u:User {id_str: '%s'})-[r:FOLLOWS]->(u2:User {id_str: '%s'})<-[r2:USER_INFO_HARVESTER_TASK_STATE {state: 0, updated_at: %d}]-(t2:USER_INFO_HARVESTER_TASK {id: 1}) RETURN u2.id_str" \
                                                      % (id_str, user['id_str'], int(time.time()))]

    #friends_or_followers_update_statement = friends_or_followers_update_statement[:-7]
    #friends_or_followers_update_statement += "RETURN u"

    tx = graph.cypher.begin()
    tx.append("MERGE (c:Campaign {campaign_id: {campaign_id}})", {'campaign_id': campaign_id})
    tx.append("MERGE (u:User {id_str: {id_str}}) " \
                     "WITH u " \
                     "MATCH (u)<-[r:FRIENDFOLLOWER_TASK_STATE]-(t:FRIENDFOLLOWER_HARVESTER_TASK) " \
                     "SET r.state = 0, r.updated_at = {current_unix_time}", {'id_str': id_str, 'current_unix_time': int(time.time())})
    #logger.debug(long_statement)
    tx.process()
    for statement in friends_or_followers_update_statement:
        tx.append(statement)
        tx.process()
    tx.commit()

    # root_user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': id_str}).one
    #
    # if root_user_node:
    #
    #     # deleting this edge because we want the next job to be explicitly ordered by some other means.
    #     graph.cypher.execute("MATCH (u:User)<-[r:FRIENDFOLLOWER_TASK_STATE]-(t:FRIENDFOLLOWER_HARVESTER_TASK) "\
    #                          "WHERE u.id_str = {id_str} SET r.state = 0, r.updated_at = {current_unix_time}"\
    #                          "RETURN r", {'id_str': id_str, 'current_unix_time': int(time.time())})
    #
    #     if friends_or_followers == 'followers':
    #         # DELETE ALL INCOMING EDGES
    #         tx = graph.cypher.begin()
    #         tx.append("MATCH (u { id_str: {id_str} })<-[r:FOLLOWS]-(u2) DELETE r", {'id_str': id_str})
    #         tx.commit()
    #
    #     elif friends_or_followers == 'friends':
    #         # DELETE ALL OUTGOING EDGES
    #         tx = graph.cypher.begin()
    #         tx.append("MATCH (u { id_str: {id_str} })-[r:FOLLOWS]->(u2) DELETE r", {'id_str': id_str})
    #         tx.commit()
    #
    #     # bunlari hizlica ekledim. konusmamiz lazim.
    #     campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
    #                              {'campaign_id': campaign_id}).one
    #     print campaign_node
    #     if not campaign_node:
    #         campaign_node = graph.cypher.execute("CREATE (c:Campaign {campaign_id: {campaign_id}}) RETURN c",
    #                                  {'campaign_id': campaign_id}).one
    #
    #     for user in user_objects:
    #
    #         user_node = init_user_to_graph_aux(campaign_node, user)
    #
    #         user_info_harvester_node = graph.cypher.execute("MATCH (t:USER_INFO_HARVESTER_TASK {id: 1}) RETURN t").one
    #         user_harvester_rel = Relationship(user_info_harvester_node, "USER_INFO_HARVESTER_TASK_STATE", user_node)
    #         user_harvester_rel.properties['state'] = 0
    #         user_harvester_rel.properties['updated_at'] = int(time.time())
    #         try:
    #             graph.create_unique(user_harvester_rel)
    #         except (GraphError), e:
    #             print("User Harvester Relation - PROBABLY A UNIQUEPATHNOTUNIQUE error")
    #
    #         if friends_or_followers == 'followers':
    #             rel = Relationship(user_node, 'FOLLOWS', root_user_node)
    #             graph.create_unique(rel)
    #         elif friends_or_followers == 'friends':
    #             rel = Relationship(root_user_node, 'FOLLOWS', user_node)
    #             graph.create_unique(rel)

if __name__ == "__main__":
    app_object.start()