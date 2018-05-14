package com.example.pacman.utils;

import io.swagger.client.*;
import io.swagger.client.model.*;
import io.swagger.client.api.DefaultApi;
import org.apache.log4j.Logger;

public class ScoreboardClient {
  private static final Logger log = Logger.getLogger(ScoreboardClient.class);
  private DefaultApi apiInstance;
  private String player;
  
  public ScoreboardClient(String player) {
    apiInstance = new DefaultApi();
    this.player = player;
  }
  
  public void addScore(int score) {
    Score body = new Score();
    body.score(score);
    body.player(player);
    try {
        apiInstance.addScore(body);
    } catch (ApiException e) {
        log.error(e);
    }
  }
}