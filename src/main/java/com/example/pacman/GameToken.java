package com.example.pacman;

public abstract interface GameToken {
  public void addGameElement(GameEngine game, Location location);
  public static boolean contains(String token) {
    return false;
  }
}
