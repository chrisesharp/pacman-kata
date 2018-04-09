package com.example.pacman;

public abstract interface InputController {
  public void control(Moveable element);
  public void tick();
}
