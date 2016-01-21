import drenaj.client.frontend.client_api as client_api

__author__ = 'onur'

# from client.frontend.oauthHandler import OAuthHandler
# from client.frontend.oauthHandler import SigninHandler
# from client.frontend.oauthHandler import SignupHandler
# from client.frontend.oauthHandler import SignoutHandler

routes_config = [
    (r"/(friends|followers)/(crawl|view)", client_api.ClientFriendFollowerHandler),

    (r"/tasks/(timeline|friendfollower|userinfo)", client_api.TaskHandler),

    (r"/user/(crawl|view)", client_api.visSingleProfileHandler),

    (r"/campaigns/(list|new|create_thread|kill_thread)", client_api.visCampaignsHandler),
    (r"/campaigns/view/watched_users", client_api.CampaignsWatchedUsersHandler),

    # (r'/sign_in', SigninHandler),
    # (r'/sign_up', SignupHandler),
    # (r'/sign_out', SignoutHandler),
    # (r'/start_oauth', OAuthHandler),
    # (r"/oauth/callback", OAuthHandler),
]