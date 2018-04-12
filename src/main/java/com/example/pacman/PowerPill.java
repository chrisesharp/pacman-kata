package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

public class PowerPill extends GameElement {
  private static final int score = 50;
  private static final Colour colour = BLINK;
  private GameEngine game;

  public PowerPill(Location location) {
      super(location);
  }

  @Override
  public int score() {
      return score;
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
  public Colour getColour() {
    return colour;
  }
  
  @Override
  public boolean equals(Object obj) {
      if (obj instanceof PowerPill) {
          final PowerPill pill = (PowerPill) obj;
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
