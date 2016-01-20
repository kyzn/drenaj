var db_design_type = 'normalized';

var reset_db = false;

load('init_db.js');

// query number of tweets made by randomly selected get_tweets_for_profiles_query_profile_number number of user
// USE IDS
//
//
print(db);

var tic, toc, execution_secs;



tic = new Date();

profiles = db.profiles.aggregate([{'$group': {'_id': 'result', 'ids': {'$push': '$id_str'}}}]).result[0].ids;

lap1 = new Date();

print(profiles.length);

var query_profile_ids = [];
for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
    var tmp_index = Random.randInt(profile_num);
    // print(tmp_index);
    var tmp_profile = profiles[tmp_index];
    query_profile_ids.push( tmp_profile.id_str );
}
lap2 = new Date();
var res = db.tweets.find({ "user_id_str": { $in : query_profile_ids } }).count();
toc = new Date();
execution_secs = (toc - lap2) +  (lap1-tic);
print( "### IDs: Count number of tweets of " + get_tweets_for_profiles_query_profile_number + ' profiles. Result: ' + res + ' Time: '  + execution_secs );


// query number of tweets made by randomly selected get_tweets_for_profiles_query_profile_number number of user
// USE INDEXES
var query_profile_inds = [];
for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
    query_profile_inds.push( Random.randInt(profile_num) );
}
var tic, toc, execution_secs;
tic = new Date();
var res=0;
for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
    var profile = db.profiles.findOne( {'id_str': query_profile_inds[i]} );
    if (profile) {
        res += db.tweets.find({ "user_id_str": profile.id_str }).count();
    }
}
toc = new Date();
execution_secs = (toc - tic) ;
print( "!## INDEXES: Count number of tweets of " + get_tweets_for_profiles_query_profile_number + ' profiles. Result: ' + res + ' Time: '  + execution_secs );

print("done");
