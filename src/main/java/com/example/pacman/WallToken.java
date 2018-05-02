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
    PacmanGame theGame = (PacmanGame) game;
    if (this.valid()) {
      Wall wall = new Wall(location);
      wall.setGame(theGame);
      wall.setIcon(icon);
      theGame.addWall(wall);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
