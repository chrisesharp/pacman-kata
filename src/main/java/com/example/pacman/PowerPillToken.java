package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class PowerPillToken extends NullToken {
  private static List<String> tokens = Arrays.asList(
    "◉",
    "o"
  );
  private String icon;

  public PowerPillToken(String icon) {
    super();
    this.icon=icon;
  }

  @Override
  public void addGameElement(GameEngine game, Location location) {
    if (this.valid()) {
      PowerPill p = new PowerPill(location);
      p.setGame(game);
      p.setIcon(this.icon);
      game.addElement(p);
    }
  }

  @Override
  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
