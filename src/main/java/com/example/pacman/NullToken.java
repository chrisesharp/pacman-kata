package com.example.pacman;

public class NullToken implements GameToken {
  public void addGameElement(GameEngine game, Location location) {
    
  }
  
  public boolean valid() {
    return false;
  }
}
