package com.example.pacman;

import java.util.concurrent.atomic.AtomicInteger;

public final class GameTokenFactory {
  private GameTokenFactory() {
    throw new IllegalStateException("Factory class");
  }

  public static GameToken getTokenizer(String token) {
    GameToken[] tokenizers = {
                              new PacmanToken(token),
                              new GhostToken(token),
                              new PillToken(token),
                              new PowerPillToken(token),
                              new WallToken(token),
                              new GateToken(token),
                              new ForceFieldToken(token)
                              };
                              
    for (int i = 0; i < tokenizers.length; i++) {
      if (tokenizers[i].valid()) {
        return tokenizers[i];
      }
    }
    return new NullToken();
  }

  public static void parseGameTokens(GameEngine game, String screen) {
      final AtomicInteger y = new AtomicInteger(0);
      for (String line: screen.split("\n")) {
          final AtomicInteger x = new AtomicInteger(0);
          line.codePoints().forEach( i -> {
            StringBuilder codepoint = new StringBuilder().appendCodePoint(i);
            GameToken token = getTokenizer(codepoint.toString());
            token.addGameElement(game,new Location(x.get(),y.get()));
            x.incrementAndGet();
          });
          y.incrementAndGet();
      }
  }
}
