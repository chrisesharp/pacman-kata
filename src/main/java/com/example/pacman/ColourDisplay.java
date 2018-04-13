package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class ColourDisplay extends MonoDisplay {
  private static final Map<Colour,String> COLOURMAP =
  Collections.unmodifiableMap(new HashMap<Colour, String>() {
    private static final long serialVersionUID = 42L;
    {
        put(BLINK, "\u001B[5m");
        put(REVERSE, "\u001B[7m");
        put(BLACK, "\u001B[30m");
        put(RED, "\u001B[31m");
        put(GREEN, "\u001B[32m");
        put(YELLOW, "\u001B[33m");
        put(BLUE, "\u001B[34m");
        put(PURPLE, "\u001B[35m");
        put(CYAN, "\u001B[36m");
        put(WHITE, "\u001B[37m");
        put(BLACK_BG, "\u001B[40m");
        put(RED_BG, "\u001B[41m");
        put(GREEN_BG, "\u001B[42m");
        put(YELLOW_BG, "\u001B[43m");
        put(BLUE_BG, "\u001B[44m");
        put(PURPLE_BG, "\u001B[45m");
        put(CYAN_BG, "\u001B[46m");
        put(WHITE_BG, "\u001B[47m");
        put(DEFAULT, "\u001B[40m\u001B[37m");
    }
  });

  public ColourDisplay(OutputStream stream) {
    super(stream);
  }

  @Override
  public void refresh(DisplayStream output) {
    String outputStream = output.getVideoStream();
    Colour[] colourStream = output.getColourStream();
    
    displayWrite(ANSI_CLEARSCREEN);
    displayWrite(COLOURMAP.get(DEFAULT));
    
    final AtomicInteger y = new AtomicInteger(0);
    for (String line: outputStream.split("\n")) {
        final AtomicInteger x = new AtomicInteger(0);
        line.codePoints().forEach(i -> {
          StringBuilder codepoint = new StringBuilder().appendCodePoint(i);
          Colour colour = colourStream[y.intValue()*this.width() + x.intValue()];
          displayWrite(COLOURMAP.get(colour));
          displayWrite(codepoint.toString());
          displayWrite(ANSI_RESET + COLOURMAP.get(DEFAULT));    
          x.incrementAndGet();
        });
        displayWrite("\n");
        y.incrementAndGet();
    }
    displayWrite(ANSI_RESET);
    displayWrite("\n");
  }

}
