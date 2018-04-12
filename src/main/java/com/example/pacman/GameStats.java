package com.example.pacman;
import com.example.pacman.Colours.Colour;

public class GameStats {
  private int lives = 3;
  private int score = 0;
  private int columns = 0;

  public void update(String statusLine) {
    columns = statusLine.length();
    String[] data = statusLine.split(" ");
    try {
      setLives(Integer.parseInt(data[0]));
    } catch (NumberFormatException e) {
      // We must have a ?
    }
    try {
      setScore(Integer.parseInt(data[data.length - 1]));
    } catch (NumberFormatException e) {
      // We must have a ?
    }
  }
  
  public void setColumns(int columns) {
    this.columns = columns;
  }

  public int score() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int lives() {
    return lives;
  }

  public void loseLife() {
    lives--;
  }

  public void addScore(int score) {
    this.score += score;
  }

  public void setLives(int lives) {
    this.lives = lives;
  }

  public String toString() {
      String output = "";
      String LIVES = ""+lives;
      String SCORE = ""+score;
      output += LIVES;
      int padding= columns - LIVES.length() - SCORE.length();
      for (int i=0; i < padding; i++) {
          output += " ";
      }
      output += SCORE + "\n";
      return output;
  }
  
  public Colour[] getColourStream() {
    Colour[] colourStream = new Colour[columns];
    for (int i=0; i<columns; i++) {
      colourStream[i] = Colour.WHITE;
    }
    return colourStream;
  }
}
