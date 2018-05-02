package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class ForceFieldToken extends NullToken {
  private static List<String> tokens = Arrays.asList(
    "#",
    "┃"
  );
  private String icon;

  public ForceFieldToken(String icon) {
    super();
    this.icon=icon;
  }
  
  @Override
  public void addGameElement(GameEngine game, Location location) {
    PacmanGame theGame = (PacmanGame) game;
    if (this.valid()) {
      ForceField field = new ForceField(location);
      field.setGame(theGame);
      field.setIcon(icon);
      theGame.addForceField(field);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
