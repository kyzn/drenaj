var db_design_type = 'subdocument';

var reset_db = false;

load('init_db.js');

var tic, toc, execution_secs;
tmp = new Date();
interval_unit = 5*24*3600;
start_date = new Date(tmp.getTime()/1000 - 2*interval_unit);
end_date = new Date(tmp.getTime()/1000 - interval_unit);

tic = new Date();
users = db.tweets.find({'tweet.user.created_at': {'$gte': start_date, '$lt': end_date}});
toc = new Date();

execution_secs = (toc - tic) ;

print( "!### XXXXXXXXXXXXXXXX Time: "  + execution_secs );

print("done");
// // query number of tweets made by randomly selected get_tweets_for_profiles_query_profile_number number of user
// var query_profile_inds = [];
// for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
//     query_profile_inds.push( String(Random.randInt(profile_num)) );
// }
// tic = new Date();
// var res = db.tweets.find({ "profile.id_str":{ $in: query_profile_inds } }).count()
//
// toc = new Date();

