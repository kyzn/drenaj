var db_design_type = 'subdocument';

var reset_db = false;

load('init_db.js');

// query number of tweets made by randomly selected get_tweets_for_profiles_query_profile_number number of user
var query_profile_inds = [];
for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
    query_profile_inds.push( String(Random.randInt(profile_num)) );
}
var tic, toc, execution_secs;
tic = new Date();
var res = db.tweets.find({ "profile.id_str":{ $in: query_profile_inds } }).count()

toc = new Date();
execution_secs = (toc - tic) ;
print( "!### Count number of tweets of " + get_tweets_for_profiles_query_profile_number + ' profiles. Result: ' + res + ' Time: '  + execution_secs );

print("done");

