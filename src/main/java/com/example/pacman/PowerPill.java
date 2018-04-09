package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

public class PowerPill extends GameElement {
  private final int SCORE = 50;
  private final Colour COLOUR = BLINK;
  private GameEngine game;

  public PowerPill(Location location) {
      super(location);
  }

  @Override
  public int score() {
      return SCORE;
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
    return COLOUR;
  }
}
