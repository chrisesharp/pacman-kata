package com.example.pacman;
import static com.example.pacman.Location.Direction;

public interface Moveable {
  public void move(Direction direction);
  public void setDirection(Direction direction);
  public void go();
  public void stop();
  public boolean moving();
  public boolean isClear(Direction direction);
  public Direction direction();
}
