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
    PacmanGame theGame = (PacmanGame) game;
    if (this.valid()) {
      Gate gate = new Gate(location);
      Wall wall = new Wall(location);
      gate.setIcon(icon);
      gate.setGame(theGame);
      wall.setIcon(icon);
      wall.setGame(theGame);
      theGame.setGate(gate);
      theGame.addWall(wall);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
