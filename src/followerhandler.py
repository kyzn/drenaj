from config import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web

from tornado.escape import json_decode,json_encode

class FollowerHandler(tornado.web.RequestHandler):
	def get(self):
		self.write("not implemented yet")

	def post(self):
		user_id = self.get_argument('user_id', None)
		json_IDS = self.get_argument('ids', None)
		IDS = json.loads(json_IDS)

		self.write(json_encode(len(IDS)))
		for i in IDS:
			print i
