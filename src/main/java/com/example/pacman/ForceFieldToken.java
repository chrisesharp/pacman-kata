package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class ForceFieldToken implements GameToken {
  private static List<String> Tokens = Arrays.asList(
    "#",
    "┃"
  );
  private String icon;

  public ForceFieldToken(String icon) {
      this.icon=icon;
  }
  public void addGameElement(GameEngine game, Location location) {
      ForceField field = new ForceField(location);
      field.setGame(game);
      field.setIcon(icon);
      game.addElement(field);
  }
  public static boolean contains(String token) {
    return Tokens.contains(token);
  }
}
