t = new Date();

Random.srand(t.getTime());
var conn = new Mongo();
var db = conn.getDB("perf_test");
db.dropDatabase();

var tweet_num=1e5;
var profile_num=1e4;
var get_tweets_for_profiles_query_profile_number=1000;
var index=true;

var max_id = 1000000000;

// https://groups.google.com/d/msg/mongodb-user/o0AmMt9i3Zc/Iciuf5pofY0J
function randomString(min_len, max_len) {
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var randomstring = '';
    var string_length = min_len+Math.floor(Math.random()*(max_len-min_len));
    for (var i=0; i<string_length; i++) {
        var rnum = Math.floor(Math.random() * chars.length);
        randomstring += chars.substring(rnum,rnum+1);
    }
    return randomstring;
}

if( index ){
    db.tweets.ensureIndex( { 'user_id_str': 1 } );
    db.profiles.ensureIndex( { 'id_str': 1 } );
}

// generate profiles
var profiles = [];
for( var profile_ind=0; profile_ind<profile_num; profile_ind++){
    var tid = Random.randInt(max_id);
    profile = {
        "id": tid,
        "id_str": String(tid),
        "description": randomString(10,50),
        "followers_count": Random.randInt(10000),
        "friends_count": Random.randInt(10000),
        "statuses_count": Random.randInt(10000),
        "favourites_count": Random.randInt(10000),
        "listed_count": Random.randInt(10000),
        "geo_enabled": Boolean( Random.randInt(2) ),
        "created_at": Date()
    };
    profiles.push(profile);
}

var ratio_profile_null = 0.1;
print('Profile field in tweets collections is empty ', ratio_profile_null, 'of the time');

// generate tweets
var tweets = [];
for( var tweet_ind=0; tweet_ind<tweet_num; tweet_ind++){
    var tid = Random.randInt(max_id);
    var hashtags = [];
    var hashtag_num = Random.randInt(10);
    for( var hashtag_ind=0; hashtag_ind<hashtag_num; hashtag_ind++){
	var indices = [];
	var index_num = 1+Random.randInt(3);
	for( var index_ind=0; index_ind<index_num; index_ind++){
	    indices.push( Random.randInt(140) );
	}
	hashtags.push( { "text": randomString(5,15), "indices": indices } );
    }

    // for( var hashtag_ind=0; hashtag_ind<hashtag_num; hashtag_ind++){
    // 	print( "hashtag ind " + hashtag_ind );
    // 	print(hashtags[hashtag_ind].text);
    // 	print(hashtags[hashtag_ind].indices);
    // }

    var author_id;
    if (Random.rand() > ratio_profile_null){
        author_id = profiles[Random.randInt(profile_num)].id_str;
    }else{
        author_id = String(Random.randInt(max_id));
    }
    var tweet = {
        "id_str": String(tid),
        "text": randomString(5,140),
        "user_id_str": author_id,
        "retweet_count": Random.randInt(10000),
        "favorite_count": Random.randInt(10000),
        "favorited": Boolean( Random.randInt(2) ),
        "retweeted": Boolean( Random.randInt(2) ),
        "truncated": Boolean( Random.randInt(2) ),
        "entities": { "hashtags": hashtags },
        "created_at": Date(),
    }
    tweets.push(tweet);
}

print( "\n\nRun two collection test for tweet_num " + tweet_num + " profile_num " + profile_num );

// store profiles
var tic, toc, execution_secs;
tic = new Date();
for( var i=0; i<profile_num; i++){
    db.profiles.save( profiles[i] );
};
toc = new Date();
execution_secs = (toc - tic) / 1000.0;
print( "Profiles save time " + execution_secs );


// store tweets
var tic, toc, execution_secs;
tic = new Date();
for( var i=0; i<tweet_num; i++){
    db.tweets.save( tweets[i] );
};
toc = new Date();
execution_secs = (toc - tic);
print( "Tweets save time " + execution_secs );

// query number of tweets made by randomly selected get_tweets_for_profiles_query_profile_number number of user
// USE IDS
var query_profile_ids = [];
for( var i=0; i<get_tweets_for_profiles_query_profile_number; i++){
    query_profile_ids.push( profiles[Random.randInt(profile_num)].id_str );
}
var tic, toc, execution_secs;
tic = new Date();
var res = db.tweets.find({ "user_id_str": { $in : query_profile_ids } }).count();
toc = new Date();
execution_secs = (toc - tic);
print( "!## IDs: Count number of tweets of " + get_tweets_for_profiles_query_profile_number + ' profiles. Result: ' + res + ' Time: '  + execution_secs );


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
print( "### INDEXES: Count number of tweets of " + get_tweets_for_profiles_query_profile_number + ' profiles. Result: ' + res + ' Time: '  + execution_secs );


print("done");
