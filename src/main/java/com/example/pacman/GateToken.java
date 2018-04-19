package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class GateToken extends NullToken {
  private static List<String> tokens = Arrays.asList(
    "━",
    "="
  );
  private String icon;

  public GateToken(String icon) {
    super();
    this.icon=icon;
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    if (this.valid()) {
      Gate gate = new Gate(location);
      Wall wall = new Wall(location);
      gate.setIcon(icon);
      gate.setGame(game);
      wall.setIcon(icon);
      wall.setGame(game);
      game.addElement(gate);
      game.addElement(wall);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
