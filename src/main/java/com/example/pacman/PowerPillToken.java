package com.example.pacman;
import java.util.List;
import java.util.Arrays;

public class PowerPillToken implements GameToken {
  private static List<String> tokens = Arrays.asList(
    "◉",
    "o"
  );
  private String icon;

  public PowerPillToken(String icon) {
      this.icon=icon;
  }

  public void addGameElement(GameEngine game, Location location) {
      PowerPill p = new PowerPill(location);
      p.setGame(game);
      p.setIcon(this.icon);
      game.addElement(p);
  }

  public boolean valid() {
    return tokens.contains(this.icon);
  }
}
