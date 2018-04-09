package com.example.pacman;
import java.io.PrintStream;
import java.io.OutputStream;

public class MonoDisplay implements Display {
  private PrintStream display;
  private int width, height;

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
    display.print(ANSI_CLEARSCREEN);
    display.print(output.getVideoStream());
    display.println(ANSI_RESET);
    display.flush();
  }

  public void flash() {
    display.print(ANSI_REVERSE_ON);
    try {
      Thread.sleep(150);
    } catch (Exception e) {
    }

    display.print(ANSI_REVERSE_OFF);
  }
  
  public int width() {
    return width;
  }
  
  public int height() {
    return height;
  }
}
