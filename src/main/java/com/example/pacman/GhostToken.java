package com.example.pacman;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class GhostToken extends NullToken {
  private static BiMap<Boolean, String> ourTokens =
                    new ImmutableBiMap.Builder<Boolean, String>()
                        .put(false,"M")
                        .put(true,"W")
                        .build();
  private String icon;

  public GhostToken(String icon) {
    super();
    this.icon=icon;
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    PacmanGame theGame = (PacmanGame) game;
    if (this.valid()) {
      Ghost g = new Ghost(location);
      g.setIcon(icon);
      g.setGame(theGame);
      if (ourTokens.inverse().get(icon)) {
        g.panic();
      }
      theGame.addGhost(g);
    }
  }

  public static String getToken(int panic) {
    return ourTokens.get(panic > 0);
  }
  
  @Override
  public boolean valid() {
    return ourTokens.containsValue(this.icon);
  }
}
