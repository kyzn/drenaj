__author__ = 'onur'

from celery import Celery

import direnaj_api.config.server_celeryconfig as celeryconfig

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

from direnaj_api.utils.direnajneo4jmanager import create_batch_from_watchlist
from direnaj_api.app import Application

#@periodic_task(run_every=crontab(minute='*/1'))

@app_object.task(name='check_watchlist_and_dispatch_tasks')
def check_watchlist_and_dispatch_tasks():

    # we can give an optional parameter for specific settings for this use.
    app = Application()

    # TODO: This might break!!! (and pycharm doesn't seem to recognize WARNs)
    create_batch_from_watchlist(app_object)
    #app.db.create_batch_from_watchlist(app_object, batch_size)
    #create_batch_from_watchlist(app_object, batch_size)

from py2neo import Graph, GraphError, Relationship
from direnaj_api.utils.direnajneo4jmanager import init_user_to_graph_aux
import time, bson
graph = Graph()
@app_object.task(name='store_friendsfollowers_in_neo4j_offline')
def store_friendsfollowers_in_neo4j_offline(id_str, campaign_id, user_objects_str, friends_or_followers):
    user_objects = bson.json_util.loads(user_objects_str)
    root_user_node = graph.cypher.execute("MATCH (u:User) WHERE u.id_str = {id_str} RETURN u", {'id_str': id_str}).one

    if root_user_node:

        # deleting this edge because we want the next job to be explicitly ordered by some other means.
        graph.cypher.execute("MATCH (u:User)<-[r:FRIENDFOLLOWER_TASK_STATE]-(t:FRIENDFOLLOWER_HARVESTER_TASK) "\
                             "WHERE u.id_str = {id_str} SET r.state = 0, r.updated_at = {current_unix_time}"\
                             "RETURN r", {'id_str': id_str, 'current_unix_time': int(time.time())})

        if friends_or_followers == 'followers':
            # DELETE ALL INCOMING EDGES
            tx = graph.cypher.begin()
            tx.append("MATCH (u { id_str: {id_str} })<-[r:FOLLOWS]-(u2) DELETE r", {'id_str': id_str})
            tx.commit()

        elif friends_or_followers == 'friends':
            # DELETE ALL OUTGOING EDGES
            tx = graph.cypher.begin()
            tx.append("MATCH (u { id_str: {id_str} })-[r:FOLLOWS]->(u2) DELETE r", {'id_str': id_str})
            tx.commit()

        # bunlari hizlica ekledim. konusmamiz lazim.
        campaign_node = graph.cypher.execute("MATCH (c:Campaign) WHERE c.campaign_id = {campaign_id} RETURN c",
                                 {'campaign_id': campaign_id}).one
        print campaign_node
        if not campaign_node:
            campaign_node = graph.cypher.execute("CREATE (c:Campaign {campaign_id: {campaign_id}}) RETURN c",
                                     {'campaign_id': campaign_id}).one

        for user in user_objects:

            user_node = init_user_to_graph_aux(campaign_node, user)

            user_info_harvester_node = graph.cypher.execute("MATCH (t:USER_INFO_HARVESTER_TASK {id: 1}) RETURN t").one
            user_harvester_rel = Relationship(user_info_harvester_node, "USER_INFO_HARVESTER_TASK_STATE", user_node)
            user_harvester_rel.properties['state'] = 0
            user_harvester_rel.properties['updated_at'] = int(time.time())
            try:
                graph.create_unique(user_harvester_rel)
            except (GraphError, ClientError), e:
                print("User Harvester Relation - PROBABLY A UNIQUEPATHNOTUNIQUE error")

            if friends_or_followers == 'followers':
                rel = Relationship(user_node, 'FOLLOWS', root_user_node)
                graph.create_unique(rel)
            elif friends_or_followers == 'friends':
                rel = Relationship(root_user_node, 'FOLLOWS', user_node)
                graph.create_unique(rel)

if __name__ == "__main__":
    app_object.start()