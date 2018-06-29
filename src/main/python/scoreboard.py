import openapi_client
from openapi_client.rest import ApiException


class Scoreboard(object):

    def __init__(self, scoreboard_url, player):
        config = openapi_client.Configuration()
        config.host = scoreboard_url
        api_client = openapi_client.ApiClient(config)
        self.api_instance = openapi_client.ScoreboardApi(api_client)
        self.player = player

    def post_score(self, score):
        try:
            body = openapi_client.Score(self.player, score)
            return self.api_instance.add_score(body)
        except ApiException as e:
            print("Exception when calling ScoreboardApi->add_score: %s\n" % e)

    def scores(self):
        try:
            api_response = self.api_instance.scores()
            return api_response
        except ApiException as e:
            print("Exception when calling ScoreboardApi->add_score: %s\n" % e)
