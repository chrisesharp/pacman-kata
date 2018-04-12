package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class PillToken implements GameToken {
  private static List<String> tokens = Arrays.asList(
    "."
  );
  private String icon;

  public PillToken(String icon) {
      this.icon=icon;
  }

  public void addGameElement(GameEngine game, Location location) {
      Pill pill = new Pill(location);
      pill.setIcon(icon);
      pill.setGame(game);
      game.addElement(pill);
  }

  public static boolean contains(String token) {
    return tokens.contains(token);
  }
}
