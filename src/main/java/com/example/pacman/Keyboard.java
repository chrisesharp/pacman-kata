package com.example.pacman;
import static com.example.pacman.Location.Direction;
import static com.example.pacman.Location.Direction.*;

import java.io.IOException;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class Keyboard implements InputController {
  private Terminal terminal;
  private NonBlockingReader keyboard;
  private Moveable element;

  public Keyboard() {
    try {
      terminal = TerminalBuilder.terminal();
      terminal.enterRawMode();
    } catch (IOException e) {
      System.out.println("err...we got " + e.getMessage());
    }
    keyboard = terminal.reader();
  }

  private int read() {
    int key;
    try {
        key = keyboard.read(125);
        if (key != -1 || key != -2) {
          return  key;
        }
    } catch (Exception e) {
      System.out.println("err...we got " + e.getMessage());
    }
    return -1;
  }

  public void control(Moveable element) {
    this.element = element;
  }

  public void tick() {
    int key = read();
    if (key != -1 ) {
      Direction dir = mapKeyDirection((char)key);
      if (dir != null) {
        element.move(dir);
      }
    }
  }

  public static Direction mapKeyDirection(char key) {
    switch (key) {
        case 'j':
            return LEFT;
        case 'l':
            return RIGHT;
        case 'i':
            return UP;
        case 'm':
            return DOWN;
        default:
            return null;
    }
  }
}
