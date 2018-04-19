package com.example.pacman;

public class NullToken implements GameToken {
  public void addGameElement(GameEngine game, Location location) {
    // NullToken's should do nothing if added to the game
  }
  
  public boolean valid() {
    return false;
  }
}
