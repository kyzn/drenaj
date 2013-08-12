from config import *

from direnajmongomanager import *

import tornado.ioloop
import tornado.web

from tornado.escape import json_decode,json_encode

class SubHandler(tornado.web.RequestHandler):
	def get(self):
		json_data = self.get_argument('param', None)
		self.write(json_data)
		self.write(json_encode("SubHandler: get"))
		print "GET: Param %s " % json_data

	def post(self):
		json_data = self.get_argument('param', None)
		self.write("Argument:")
		self.write(json_data)
		self.write(json_encode("SubHandler: post"))
		print "POST: Param %s " % json_data
