__author__ = 'onur'

import time
from direnaj.utils.drnj_time import py_time2drnj_time

import requests

def main():
    import argparse
    parser = argparse.ArgumentParser(description='get tweets by campaign id')
    parser.add_argument('campaign_id', default='2014avrasya1', help='campaign id')
    parser.add_argument('n_days_before_start', default=7, help='baslangic')
    parser.add_argument('n_days_before_end', default=6, help='bitis')
    parser.add_argument('outfilename', default='tweets.json', help='outputfile')
    parser.add_argument('skip', default=0, help='skip')
    parser.add_argument('limit', default=0, help='limit')
    args = parser.parse_args()

    base_url = \
        "http://direnaj-staging.cmpe.boun.edu.tr/statuses/filter?auth_password=tamtam&auth_user_id=direnaj&campaign_id={campaign_id}&since_datetime={since_datetime}&until_datetime={until_datetime}&skip={skip}&limit={limit}"

    print(args)

    n_days_before_start = float(args.n_days_before_start)
    n_days_before_end = float(args.n_days_before_end)

    now = time.time()

    request_url = base_url.format(campaign_id=args.campaign_id,
                    since_datetime=py_time2drnj_time(now-1*n_days_before_start*3600*24),
                    until_datetime=py_time2drnj_time(now-1*n_days_before_end*3600*24),
                    skip=args.skip,
                    limit=args.limit)

    print(request_url)

    response = requests.get(request_url)

    #print(response.content)
    f = open(args.outfilename, 'a+')
    f.write(response.content)
    f.write('\n')
    f.close()

if __name__ == "__main__":
    main()