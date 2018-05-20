package com.example.pacman.utils;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.model.Score;
import io.swagger.client.api.ScoreboardApi;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;

public class ScoreboardClient {
  private static final Logger log = Logger.getLogger(ScoreboardClient.class);
  private ScoreboardApi scoreboard;
  private String player;
  private String scoreboardURL;
  
  public ScoreboardClient(String player, String scoreboardURL) {
    this.player = player;
    this.scoreboardURL = scoreboardURL;
    
    ApiClient client = new ApiClient();
    scoreboard = new ScoreboardApi();
    this.setApiClient(client);
  }
  
  public void setApiClient(ApiClient client) {
    client.setBasePath(scoreboardURL);
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
        log.error("For " + scoreboardURL, e);
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