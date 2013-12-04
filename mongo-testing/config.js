t = new Date();
Random.srand(t.getTime());

var db_design_type = 'subdocument'; // or 'normalized'

var conn = new Mongo();

var backup_db_name = "000_perf_test_" + db_design_type + '_bak';

var db_name = "000_perf_test_" + db_design_type;

var backup_db = conn.getDB(backup_db_name);
var db = conn.getDB(db_name);

var tweet_num=1e5;
var profile_num=1e4;
var get_tweets_for_profiles_query_profile_number=1000;
var index=true;

var max_id = 1000000000;

