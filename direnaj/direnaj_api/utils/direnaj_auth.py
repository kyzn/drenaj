import hashlib
import base64

#http://wiki.python.org/moin/PythonDecoratorLibrary#Class_method_decorator_using_instance
from functools import wraps
#import direnajmongomanager

from tornado.gen import Return

def direnaj_simple_auth(f):
    '''
    Class method decorator specific to the instance.
 
    It uses a descriptor to delay the definition of the
    method wrapper.
    
    @direnaj_simple_auth decorator. Looks up username and password from
    GET/POST arguments. Returns 401 Unauthorized if user does not exist in database

    '''
    class descript(object):
         def __init__(self, f):
             self.f = f
 
         def __get__(self, instance, klass):
             if instance is None:
                 # Class method was requested
                 return self.make_unbound(klass)
             return self.make_bound(instance)
 
         def make_unbound(self, klass):
             @wraps(self.f)
             def wrapper(*args, **kwargs):
                 '''This documentation will vanish :)'''
                 raise TypeError(
                     'unbound method {}() must be called with {} instance '
                     'as first argument (got nothing instead)'.format(
                         self.f.__name__,
                         klass.__name__)
                 )
             return wrapper
 
         def make_bound(self, instance):
             @wraps(self.f)
             def wrapper(*args, **kwargs):
                 '''This documentation will disappear :)'''
                 #print "Called the decorated method {} of {}".format(self.f.__name__, instance)

                 passwd_sha1 = hashlib.sha1()
                 passwd_sha1.update(instance.get_argument('auth_password'))
                 username = instance.get_argument('auth_user_id')

                 # check authentication
                 try:
                     # direnajmongomanager.check_auth(username, base64.urlsafe_b64encode( passwd_sha1.digest()))
                     pass
                 except Return, r:
                     db_user = r.value
                     if (len(db_user) == 1):
                         kwargs["drnjID"] = db_user[0]["direnajID"]
                         return self.f(instance, *args, **kwargs)
                     else:
                         from tornado.web import HTTPError
                         raise HTTPError(401)

             # This instance does not need the descriptor anymore,
             # let it find the wrapper directly next time:
             setattr(instance, self.f.__name__, wrapper)
             return wrapper
 
    return descript(f)
