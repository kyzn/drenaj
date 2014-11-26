#!/bin/bash

campaign_id=$1
start_day=$2
end_day=$3

skip=0
limit=100

x=2
while (( $x > 1 ))
do
  filename=tweets-$campaign_id-$start_day-$end_day-$skip-$limit.json
  python -m direnaj.client.utils.get_tweets_by_campaign $campaign_id $start_day $end_day $filename.tmp $skip $limit
  skip=$((skip+limit))
  cat $filename.tmp | sed 's/{"direnaj_service_version/\n{"direnaj_service_version/g' > $filename
  rm $filename.tmp
  x=`wc -l $filename | cut -d' ' -f1`
done;