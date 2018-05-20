import swagger_client
from swagger_client.rest import ApiException


class Scoreboard(object):

    def __init__(self, scoreboard_url, player):
        config = swagger_client.Configuration()
        config.host = scoreboard_url
        api_client = swagger_client.ApiClient(config)
        self.api_instance = swagger_client.ScoreboardApi(api_client)
        self.player = player

    def post_score(self, score):
        try:
            body = swagger_client.Score(self.player, score)
            return self.api_instance.add_score(body)
        except ApiException as e:
            print("Exception when calling ScoreboardApi->add_score: %s\n" % e)

    def scores(self):
        try:
            api_response = self.api_instance.scores()
            return api_response
        except ApiException as e:
            print("Exception when calling ScoreboardApi->add_score: %s\n" % e)
