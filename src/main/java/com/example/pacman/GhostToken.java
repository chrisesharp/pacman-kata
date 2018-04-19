package com.example.pacman;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class GhostToken implements GameToken {
  private static BiMap<Boolean, String> ourTokens =
                    new ImmutableBiMap.Builder<Boolean, String>()
                        .put(false,"M")
                        .put(true,"W")
                        .build();
  private String icon;

  public GhostToken(String icon) {
      this.icon=icon;
  }

  public void addGameElement(GameEngine game, Location location) {
      Ghost g = new Ghost(location);
      g.setIcon(icon);
      g.setGame(game);
      if (ourTokens.inverse().get(icon)) {
        g.panic();
      }
      game.addElement(g);
  }

  public static String getToken(int panic) {
    return ourTokens.get(panic > 0);
  }
  public boolean valid() {
    return ourTokens.containsValue(this.icon);
  }
}
