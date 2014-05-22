import direnaj.client.frontend.client_api as client_api

__author__ = 'onur'

# from client.frontend.oauthHandler import OAuthHandler
# from client.frontend.oauthHandler import SigninHandler
# from client.frontend.oauthHandler import SignupHandler
# from client.frontend.oauthHandler import SignoutHandler

routes_config = [
    (r"/(friends|followers)/(crawl|view)", client_api.visFollowerHandler),
    (r"/statuses/(crawl|view)", client_api.visStatusesHandler),
    (r"/user/(crawl|view)", client_api.visSingleProfileHandler),
    (r"/profiles/(crawl|view)", client_api.visUserProfilesHandler),
    (r"/campaigns/(list|new|create_thread|kill_thread)", client_api.visCampaignsHandler),

    # (r'/sign_in', SigninHandler),
    # (r'/sign_up', SignupHandler),
    # (r'/sign_out', SignoutHandler),
    # (r'/start_oauth', OAuthHandler),
    # (r"/oauth/callback", OAuthHandler),
]