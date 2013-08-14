# A Basic Crawler that retrieves all followers of a user and sends the data to the Service Layer
# 
# 12-Aug-2013	12:33 AM	ATC		Developed using Python 2.7

# ATC = Ali Taylan Cemgil,
# Department of Computer Engineering, Bogazici University
# e-mail :  taylan.cemgil@boun.edu.tr 


from twython import Twython, TwythonError
import time
import requests
import json
from tornado.escape import json_decode
from tornado.escape import json_encode


consumer_key = "hQUgcQ8GffQb9lcteoLy9g"
consumer_secret = "ADyiEvLtupE6J8JRo9J8onJ9oHgOaq7KFjKpa6QoDk"
access_token_key = "461494325-1L0bBBGL4okKhwYKChNM2cVbG1tp5hh5w9xIvblP"
access_token_secret = "W5W9PHcWBYTBrVJWbLpOOEZAZXGeGU3edBCUzwXR4"


#root = 50354388; # koray
#root = 461494325; # Taylan
root = 505670972; # Cem Say
#root = 483121138; # meltem
#root = 230412751; # Cengiz
#root = 636874348; # Pinar Selek
# root = 382081201; # Tolga Tuzun
#root = 745174243; # Sarp Maden

#fof = "followers"
fof = "friends"

twitter = Twython(consumer_key, consumer_secret, access_token_key, access_token_secret)

cur = -1L
IDS = list()
#SS = list()

remain = 0
success = True
wait = 60;

print "Retrieving %s of user %d\n" % (fof, root)

while 1:
	# Check if we still have some bandwidth available
	while remain<=0:
		v = twitter.get('application/rate_limit_status', {"resources": fof})
		remain = v["resources"][fof]["/" + fof + "/ids"]["remaining"]
		if remain>0:
			break
		print "Waiting... Twitter API rate limit reached\n"
		time.sleep(wait)
		
	try:
		S = twitter.get(fof + '/ids', {'user_id': root, 'cursor': cur})
		
		# We count the number of remaining requests to the Twitter API
		remain = remain - 1;
		
		IDS = IDS + S["ids"]
#		SS = SS.append(S)
	
		print "Total number of %s ID's retrieved so far: %d" % (fof, len(IDS))
	
		cur = S["next_cursor"]
		if cur==0:
			break
	except TwythonError as e:
		print e
		success = False
		print "Error while fetching data from Twitter API"
		break


if success:		
	post_response = requests.post(url='http://localhost:9999/' +fof + '/ids/store', data={"user_id": root, "ids": json_encode(IDS)})
	print "Number of ids saved %s" % post_response.content
else:
	print "Terminated!"
