package com.example.pacman;
import java.util.List;

public abstract interface GameEngine {
  public void init(InputController controller, Display display);
  public void play();
  public void parse();
  public DisplayStream render();
  public void tick();
  public boolean animatedIcons();
  public void animateIcons();
  public List<GameElement> getElements();
  public GameElement getGameElement(Location loc);
  public GameElement getGameElementByType(Class<? extends GameElement> type);
  public int getLives();
  public void loseLife();
  public int getScore();
  public void addScore(int score);
  public void setLevel(int level);
  public void setMaxLevels(int numberOfLevels);
  public boolean gameOver();
  public void triggerEffect(GameElement element);
}
