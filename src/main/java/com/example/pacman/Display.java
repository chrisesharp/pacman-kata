package com.example.pacman;
import java.io.OutputStream;

public interface Display {
  public void setOutputStream(OutputStream stream);
  public void init(int width, int height);
  public void refresh(DisplayStream outputBuffer);
  public void flash();
  public int width();
  public int height();
}
