package com.example.pacman;

public class Pill extends GameElement {
  private GameEngine game;
  private final int SCORE = 10;
  public Pill(Location location) {
      super(location);
  }

  @Override
  public void setGame(GameEngine game) {
    this.game = game;
  }

  @Override
  public void triggerEffect(GameElement element) {
    game.triggerEffect(this);
  }

  @Override
  public int score() {
      return SCORE;
  }
}
