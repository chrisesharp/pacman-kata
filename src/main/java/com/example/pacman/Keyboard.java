package com.example.pacman;
import static com.example.pacman.Location.Direction;
import static com.example.pacman.Location.Direction.*;

import java.io.IOException;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.apache.log4j.Logger;
import java.util.Map;
import java.util.HashMap;

public class Keyboard implements InputController {
  private NonBlockingReader keybrd;
  private Moveable element;
  private static final Logger log = Logger.getLogger(Keyboard.class);
  private static final int TIME_INTERVAL_MS = 100;
  private static final Map<Character, Direction> keyMap = new HashMap<>();
  static {
    keyMap.put('j', LEFT);
    keyMap.put('i', UP);
    keyMap.put('l', RIGHT);
    keyMap.put('m', DOWN);
  }

  public Keyboard() {
    try {
      Terminal terminal = TerminalBuilder.terminal();
      terminal.enterRawMode();
      keybrd = terminal.reader();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private int read() {
    try {
        return keybrd.read(TIME_INTERVAL_MS);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return -1;
  }

  public void control(Moveable element) {
    this.element = element;
  }

  public void tick() {
    int key = read();
    if (key >= 0) {
        element.move(Keyboard.mapKeyDirection((char)key));
    }
  }

  public static Direction mapKeyDirection(char key) {
    return keyMap.get(Character.valueOf(key));
  }
}
