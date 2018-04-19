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
    if (this.valid()) {
      ForceField field = new ForceField(location);
      field.setGame(game);
      field.setIcon(icon);
      game.addElement(field);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
