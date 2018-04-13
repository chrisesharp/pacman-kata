package com.example.pacman;
import java.io.PrintStream;
import java.io.OutputStream;

public class MonoDisplay implements Display {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_CLEARSCREEN = "\u001B[H\u001B[2J\u001B[1m";
  public static final String ANSI_REVERSE_ON = "\u001B[?5h";
  public static final String ANSI_REVERSE_OFF = "\u001B[?5l";
  private PrintStream display;
  private int width;
  private int height;

  public MonoDisplay(OutputStream stream) {
    setOutputStream(stream);
  }

  public void setOutputStream(OutputStream stream) {
    display = new PrintStream(stream);
  }
  
  public void init(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void refresh(DisplayStream output) {
    displayWrite(ANSI_CLEARSCREEN);
    displayWrite(output.getVideoStream());
    displayWrite(ANSI_RESET);
    displayWrite("\n");
    displayFlush();
  }

  public void flash() {
    displayWrite(ANSI_REVERSE_ON);
    try {
      Thread.sleep(150);
    } catch (Exception e) {
      // Doesn't matter if we wake up
    }

    displayWrite(ANSI_REVERSE_OFF);
  }
  
  public int width() {
    return width;
  }
  
  public int height() {
    return height;
  }
  
  public void displayWrite(String output) {
    display.print(output);
  }
  
  public void displayFlush() {
    display.flush();
  }
}
