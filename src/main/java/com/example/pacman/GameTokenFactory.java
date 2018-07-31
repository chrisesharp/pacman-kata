package com.example.pacman;

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
      int y = 0;
      for (String line: screen.split("\n")) {
        int x = 0;
        for (int codepoint : line.codePoints().toArray()) {
          String tokenStr = new String(new int[] { codepoint }, 0, 1);
          GameToken token = getTokenizer(tokenStr);
          token.addGameElement(game,new Location(x,y));
          x++;
        }
        y++;
      }
  }
}
