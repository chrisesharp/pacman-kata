package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;
import java.util.EnumMap;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class ColourDisplay extends MonoDisplay {
  private static final EnumMap<Colour,String> COLOURMAP = new EnumMap<>(Colour.class);
  static {
    COLOURMAP.put(BLINK, "\u001B[5m");
    COLOURMAP.put(REVERSE, "\u001B[7m");
    COLOURMAP.put(BLACK, "\u001B[30m");
    COLOURMAP.put(RED, "\u001B[31m");
    COLOURMAP.put(GREEN, "\u001B[32m");
    COLOURMAP.put(YELLOW, "\u001B[33m");
    COLOURMAP.put(BLUE, "\u001B[34m");
    COLOURMAP.put(PURPLE, "\u001B[35m");
    COLOURMAP.put(CYAN, "\u001B[36m");
    COLOURMAP.put(WHITE, "\u001B[37m");
    COLOURMAP.put(BLACK_BG, "\u001B[40m");
    COLOURMAP.put(RED_BG, "\u001B[41m");
    COLOURMAP.put(GREEN_BG, "\u001B[42m");
    COLOURMAP.put(YELLOW_BG, "\u001B[43m");
    COLOURMAP.put(BLUE_BG, "\u001B[44m");
    COLOURMAP.put(PURPLE_BG, "\u001B[45m");
    COLOURMAP.put(CYAN_BG, "\u001B[46m");
    COLOURMAP.put(WHITE_BG, "\u001B[47m");
    COLOURMAP.put(DEFAULT, "\u001B[37m\u001B[40m");
  }

  public ColourDisplay(OutputStream stream) {
    super(stream);
  }

  @Override
  public void refresh(DisplayStream output) {
    String outputStream = output.getVideoStream();
    Colour[] colourStream = output.getColourStream();
    
    displayWrite(ANSI_CLEARSCREEN);
    displayWrite(COLOURMAP.get(DEFAULT));
    
    final AtomicInteger idx = new AtomicInteger(0);
    outputStream.codePoints().forEach( cp -> {
      StringBuilder codepoint = new StringBuilder().appendCodePoint(cp);
      int mapIndex = idx.intValue();
      if (colourStream.length > 0) {
        displayWrite(COLOURMAP.get(colourStream[mapIndex]));
        displayWrite(codepoint.toString());
        displayWrite(ANSI_RESET + COLOURMAP.get(DEFAULT));
      } else {
        displayWrite(codepoint.toString());
      } 
      idx.incrementAndGet();
    });

    displayWrite(ANSI_RESET);
    displayWrite("\n");
  }

}
