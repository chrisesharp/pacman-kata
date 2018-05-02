package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class PillToken extends NullToken {
  private static List<String> tokens = Arrays.asList(
    "."
  );
  private String icon;

  public PillToken(String icon) {
    super();
    this.icon=icon;
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    PacmanGame theGame = (PacmanGame) game;
    if (this.valid()) {
      Pill pill = new Pill(location);
      pill.setIcon(icon);
      pill.setGame(theGame);
      theGame.addPill(pill);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
