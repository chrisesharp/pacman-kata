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
  private boolean panicked=false;

  public GhostToken(String icon) {
      this.icon=icon;
      panicked = ourTokens.inverse().get(icon);
  }

  public void addGameElement(GameEngine game, Location location) {
      Ghost g = new Ghost(location);
      g.setIcon(icon);
      g.setGame(game);
      if (panicked) {
        g.panic();
      }
      game.addElement(g);
  }

  public static boolean contains(String token) {
    return ourTokens.containsValue(token);
  }

  public static String getToken(int panic) {
    return ourTokens.get(panic > 0);
  }
}
