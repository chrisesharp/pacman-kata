package com.example.pacman;
import java.io.OutputStream;

public interface Display {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_CLEARSCREEN = "\u001B[H\u001B[2J\u001B[1m";
  public static final String ANSI_REVERSE_ON = "\u001B[?5h";
  public static final String ANSI_REVERSE_OFF = "\u001B[?5l";

  public void setOutputStream(OutputStream stream);
  public void init(int width, int height);
  public void refresh(DisplayStream outputBuffer);
  public void flash();
  public int width();
  public int height();
}
