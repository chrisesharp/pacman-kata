package com.example.pacman;

public class Pill extends GameElement {
  private GameEngine game;
  private static final int score = 10;
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
      return score;
  }
  
  @Override
  public boolean equals(Object obj) {
      if (obj instanceof Pill) {
          final Pill pill = (Pill) obj;
          return (this.location().equals(pill.location()));
      } else {
        return false;
      }
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() * 23;
  }
}
