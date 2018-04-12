package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class WallToken implements GameToken {
  private static List<String> tokens = Arrays.asList(
    "+",
    "|",
    "-",
    "╔",
    "╗",
    "╚",
    "╝",
    "═",
    "║",
    "╬",
    "╩",
    "╦",
    "╣",
    "╠"
  );
  private String icon;

  public WallToken(String icon) {
      this.icon=icon;
  }
  public void addGameElement(GameEngine game, Location location) {
      Wall wall = new Wall(location);
      wall.setGame(game);
      wall.setIcon(icon);
      game.addElement(wall);
  }

  public static boolean contains(String token) {
    return tokens.contains(token);
  }
}
