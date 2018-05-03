package com.example.pacman;

public class Pill extends GameElement {
  private static final int SCORE = 10;
  public Pill(Location location) {
      super(location);
  }

  @Override
  public void triggerEffect(GameElement element) {
    super.getGame().triggerEffect(this);
  }

  @Override
  public int score() {
      return SCORE;
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
