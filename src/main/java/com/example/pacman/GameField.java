package com.example.pacman;

import com.example.pacman.Colours.Colour;
//import com.example.pacman.Colours.Colour.*;
import java.util.Arrays;
import java.util.ArrayList;

public class GameField {
  private GameElement [][] gameField;
  private int width, height;
  private Colour[] colourStream;
  private String videoStream;

  public GameField(int width, int height) {
    this.width = width;
    this.height = height;
    gameField = new GameElement [width][height];
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  protected void clear() {
    for (GameElement[] row: gameField) {
      Arrays.fill(row,null);
    }
  }

  private Location setLocation(Location loc, GameElement value) {
    Location location = this.wrappedLocation(loc);
    gameField[location.x()][location.y()] = value;
    return location;
  }

  public void setLocation(GameElement element) {
    element.setLocation(this.setLocation(element.location(),element));
  }

  public GameElement getLocation(Location loc) {
    Location location = this.wrappedLocation(loc);
    return gameField[location.x()][location.y()];
  }
  
  private Location wrappedLocation(Location loc) {
    return new Location(((loc.x() + width) % width), ((loc.y() + height) % height));
  }

  public void printGameOver() {
      String GAME = "GAME";
      String OVER = "OVER";
      int y = (height/2)-2;
      int padding = ((width-2)-GAME.length())/2;
      TextElement character;

      for (int x=0; x < GAME.length(); x++) {
          character = new TextElement(null);
          character.setIcon("" + GAME.charAt(x));
          gameField[padding+1+x][y]= character;
          character = new TextElement(null);
          character.setIcon("" + OVER.charAt(x));
          gameField[padding+1+x][y+1]= character;
      }
  }
  
  public void generateDisplayStream() {
    StringBuffer buffer = new StringBuffer();
    ArrayList<Colour> colours = new ArrayList<Colour>();
    for (int y=0; y < height; y++) {
        for (int x=0; x < width; x++) {
          GameElement el = gameField[x][y];
          if (el != null) {
            buffer.append(el.render());
            colours.add(el.getColour());
          } else {
            buffer.append(" ");
            colours.add(Colour.DEFAULT);
          }    
        }
        buffer.append('\n');
    }
    this.colourStream = colours.toArray(new Colour[colours.size()]);
    this.videoStream = buffer.toString().trim();
  }

  public String getVideoStream() {
      return videoStream;
  }
  
  public Colour[] getColourStream() {
    return colourStream;
  }

  public void update(GameEngine game) {
    clear();
    for (GameElement element: game.getElements()) {
        setLocation(element);
    }
    if (game.gameOver()) {
        printGameOver();
    }
  }
}
