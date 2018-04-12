package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class GateToken implements GameToken {
  private static List<String> tokens = Arrays.asList(
    "━",
    "="
  );
  private String icon;

  public GateToken(String icon) {
      this.icon=icon;
  }
  public void addGameElement(GameEngine game, Location location) {
      Gate gate = new Gate(location);
      Wall wall = new Wall(location);
      gate.setIcon(icon);
      gate.setGame(game);
      wall.setIcon(icon);
      wall.setGame(game);
      game.addElement(gate);
      game.addElement(wall);
  }
  public static boolean contains(String token) {
    return tokens.contains(token);
  }
}
