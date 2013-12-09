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

var profiles = [];

function init_db(db, db_design_type) {

    print('Initializing database: ', db_design_type);

    if (db_design_type === 'subdocument') {
        if( index ){
            db.tweets.ensureIndex( { 'profile.id_str': 1 } );
        }

        // generate profiles
        for( var profile_ind=0; profile_ind<profile_num; profile_ind++){
            var tid = Random.randInt(max_id);
            var now = new Date();
            var rand_radius = 30*24*3600;
            var created_at_randomness = Random.randInt(2*rand_radius) - rand_radius;
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
                "created_at": new Date(now.getTime()/1000 + created_at_randomness),
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
            //  print( "hashtag ind " + hashtag_ind );
            //  print(hashtags[hashtag_ind].text);
            //  print(hashtags[hashtag_ind].indices);
            // }

            var author_id;
            if (Random.rand() > ratio_profile_null) {
                profile = profiles[Random.randInt(profile_num)];
            }else{
                profile = {};
            }

            var rand_radius_for_tweet = 24*3600;
            var created_at_randomness_for_tweet = Random.randInt(2*rand_radius_for_tweet) - rand_radius_for_tweet;

            var tweet = {
                "tweet":
                {
                    "_id": tid,
                    "id_str": String(tid),
                    "text": randomString(5,140),
                    "retweet_count": Random.randInt(10000),
                    "favorite_count": Random.randInt(10000),
                    "favorited": Boolean( Random.randInt(2) ),
                    "retweeted": Boolean( Random.randInt(2) ),
                    "truncated": Boolean( Random.randInt(2) ),
                    "entities": { "hashtags": hashtags },
                    "created_at": new Date(now.getTime()/1000 + created_at_randomness_for_tweet),
                },
                "profile": profile,
            }
            tweets.push(tweet);
        }


        print( "\n\nRun single collection test for tweet_num " + tweet_num + " profile_num " + profile_num );

        // store tweets
        var tic, toc, execution_secs;
        tic = new Date();
        for( var i=0; i<tweet_num; i++){
            db.tweets.insert( tweets[i] );
        };
        toc = new Date();
        execution_secs = (toc - tic) / 1000.0;
        print( "Tweets save time " + execution_secs );
    } else {

        if( index ){
            db.tweets.ensureIndex( { 'user_id_str': 1 } );
            db.profiles.ensureIndex( { 'id_str': 1 } );
        }

        // generate profiles
        for( var profile_ind=0; profile_ind<profile_num; profile_ind++){
            var tid = Random.randInt(max_id);
            var now = new Date();
            var rand_radius = 30*24*3600;
            var created_at_randomness = Random.randInt(2*rand_radius) - rand_radius;
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
                "created_at": new Date(now.getTime()/1000 + created_at_randomness),
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

            var rand_radius_for_tweet = 24*3600;
            var created_at_randomness_for_tweet = Random.randInt(2*rand_radius_for_tweet) - rand_radius_for_tweet;

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
                "created_at": new Date(now.getTime()/1000 + created_at_randomness),
            }
            tweets.push(tweet);
        }

        print( "\n\nRun two collection test for tweet_num " + tweet_num + " profile_num " + profile_num );

        // store profiles
        var tic, toc, execution_secs;
        tic = new Date();
        for( var i=0; i<profile_num; i++){
            db.profiles.insert( profiles[i] );
        };
        toc = new Date();
        execution_secs = (toc - tic) / 1000.0;
        print( "Profiles save time " + execution_secs );


        // store tweets
        var tic, toc, execution_secs;
        tic = new Date();
        for( var i=0; i<tweet_num; i++){
            db.tweets.insert( tweets[i] );
        };
        toc = new Date();
        execution_secs = (toc - tic);
        print( "Tweets save time " + execution_secs );

    }

    return db;

}

