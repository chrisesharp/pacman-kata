package com.example.pacman;
import static com.example.pacman.Location.Direction;
import static com.example.pacman.Location.Direction.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class PacmanToken extends NullToken {
  private String icon;

  private static BiMap<Direction, String> ourTokens =
                    new ImmutableBiMap.Builder<Direction, String>()
                        .put(LEFT,">")
                        .put(RIGHT,"<")
                        .put(UP,"V")
                        .put(DOWN,"Λ")
                        .build();

  private static BiMap<Direction, String> ourAltTokens =
                    new ImmutableBiMap.Builder<Direction, String>()
                        .put(LEFT,"}")
                        .put(RIGHT,"{")
                        .put(UP,"v")
                        .put(DOWN,"^")
                        .build();

  private static String deadICON = "*";

  public PacmanToken(String icon) {
    super();
    this.icon=icon;
  }

  private Direction parseDirection() {
    return ourTokens.inverse().get(this.icon);
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    if (this.valid()) {
      Pacman pacman = new Pacman(location);
      pacman.setGame(game);
      pacman.setDirection(parseDirection());
      pacman.setIcon(icon);
      game.addElement(pacman);
    }
  }

  public static String getToken(Direction direction) {
    return ourTokens.get(direction);
  }

  public static String getAltToken(Direction direction) {
    return ourAltTokens.get(direction);
  }

  public static String getDeadToken() {
    return deadICON;
  }
  
  @Override
  public boolean valid() {
    if (this.icon.equals(deadICON)) {
      return true;
    }
    return ourTokens.containsValue(this.icon);
  }
}
