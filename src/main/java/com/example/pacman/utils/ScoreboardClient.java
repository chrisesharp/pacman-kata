package com.example.pacman.utils;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Score;
import org.openapitools.client.api.ScoreboardApi;
import org.openapitools.client.auth.ApiKeyAuth;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ScoreboardClient {
  private static final Logger log = Logger.getLogger(ScoreboardClient.class);
  private ScoreboardApi scoreboard;
  private String player;
  private String scoreboardURL;
  
  public ScoreboardClient(String player, String scoreboardURL) {
    this.player = player;
    this.scoreboardURL = scoreboardURL;
    
    ApiClient client = new ApiClient();
    this.scoreboard = new ScoreboardApi();
    this.setApiClient(client);
  }
  
  public void setApiClient(ApiClient client) {
    client.setBasePath(scoreboardURL);
    ApiKeyAuth authScheme = (ApiKeyAuth) client.getAuthentication("scoreboardService_auth_Bearer");
    authScheme.setApiKeyPrefix("Bearer");
    authScheme.setApiKey("JWT_goes_here");
    scoreboard.setApiClient(client);
  }
  
  public void setPlayer(String player) {
    this.player = player;
  }
  
  public void addScore(int score) {
    Score body = new Score();
    body.setPlayer(this.player);
    body.setScore(score);
    try {
        scoreboard.addScore(body);
    } catch (ApiException e) {
        log.error("Failed to addScore for " + scoreboardURL, e);
    }
  }
  
  public List<String> scores() {
    List<String> textScores = new ArrayList<>();
    try {
      List<Score> scores =  scoreboard.scores();
      for (Score score: scores) {
        textScores.add(score.getPlayer() + ":" + score.getScore());
      }
    } catch (ApiException e) {
        log.error(e);
    }
    return textScores;
  }
}