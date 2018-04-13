package com.example.pacman;

import java.util.concurrent.atomic.AtomicInteger;

public final class GameTokenFactory {
  private GameTokenFactory() {
    throw new IllegalStateException("Factory class");
  }

  public static GameToken getToken(String cursor) {
    String token = cursor ;
      if (PacmanToken.contains(token)) {
         return new PacmanToken(token);
      } else if (GhostToken.contains(token)) {
         return new GhostToken(token);
      } else if (PillToken.contains(token)) {
          return new PillToken(token);
      } else if (PowerPillToken.contains(token)) {
          return new PowerPillToken(token);
      } else if (WallToken.contains(token)){
          return new WallToken(token);
      } else if (GateToken.contains(token)){
          return new GateToken(token);
      } else if (ForceFieldToken.contains(token)){
          return new ForceFieldToken(token);
      } else return null;
  }

  public static void parseGameTokens(GameEngine game, String screen) {
      final AtomicInteger y = new AtomicInteger(0);
      for (String line: screen.split("\n")) {
          final AtomicInteger x = new AtomicInteger(0);
          line.codePoints().forEach( i -> {
            StringBuilder codepoint = new StringBuilder().appendCodePoint(i);
            GameToken token = getToken(codepoint.toString());
            if (token != null) {
              token.addGameElement(game,new Location(x.get(),y.get()));
            }
            x.incrementAndGet();
          });
          y.incrementAndGet();
      }
  }
}
