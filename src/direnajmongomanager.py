from config import *

from pymongo import MongoClient
from pymongo import ASCENDING

mongo_client = MongoClient(MONGO_HOST, MONGO_PORT)
