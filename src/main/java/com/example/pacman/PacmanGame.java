package com.example.pacman;

public abstract interface PacmanGame extends GameEngine {
  public void setPacman(GameElement pacman);
  public void setGate(GameElement gate);
  public void addForceField(GameElement field);
  public void addGhost(GameElement ghost);
  public void addPill(GameElement pill);
  public void addPowerPill(GameElement pill);
  public void addWall(GameElement wall);
}
