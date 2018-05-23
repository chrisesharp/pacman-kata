"use strict";

var client = require("leaderboard_api");

module.exports =

class Scoreboard {
  constructor(scoreboardURL, player) {
    var apiClient = new client.ApiClient();
    apiClient.basePath = scoreboardURL; 
    this.api = new client.ScoreboardApi(apiClient);
    this.player = player;
  }

  postScore(score, callback) {
    var body = new client.Score(this.player, score);
    this.api.addScore(body, callback);
  }
  
  scores(callback) {
    return this.api.scores(callback);
  }
  
};