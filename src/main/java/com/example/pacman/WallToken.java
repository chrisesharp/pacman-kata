package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class WallToken extends NullToken {
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
    super();
    this.icon=icon;
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    if (this.valid()) {
      Wall wall = new Wall(location);
      wall.setGame(game);
      wall.setIcon(icon);
      game.addElement(wall);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
