package com.example.pacman;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.Objects;

public class Game implements GameEngine {
    private boolean gameOver=false;
    private boolean usingPills=false;
    private boolean usingPowerPills=false;
    private boolean animatedIcons=false;
    private Level level;

    private GameStats status = new GameStats();
    private GameField gameField;
    private InputController controller;
    private Display display = new MonoDisplay(System.out);

    private GameElement pacman;
    private List<GameElement> ghosts = new ArrayList<>();
    private List<GameElement> pills = new ArrayList<>();
    private List<GameElement> powerPills = new ArrayList<>();
    private List<GameElement> walls = new ArrayList<>();
    private List<GameElement> forceFields = new ArrayList<>();
    private GameElement gate;

    public Game() {
    }

    public Game(Level level ) {
        this.level = level;
    }

    public boolean animatedIcons() {
      return animatedIcons;
    }

    public void animateIcons() {
      animatedIcons=true;
    }

    public void parse() {
        int columns = level.getLevelMap().indexOf('\n');
        status.update(level.getLevelMap().substring(0, columns));
        String screenRows = level.getLevelMap().substring(columns + 1);
        int rows = screenRows.split("\n").length;
        gameField = new GameField(columns,rows);
        GameTokenFactory.parseGameTokens(this, screenRows);
        gameField.update(this);

    }

    public DisplayStream render() {
      gameField.generateDisplayStream();
      DisplayStream stream = new DisplayStream();
      stream.writeVideo(status.toString());
      stream.writeColour(status.getColourStream());
      stream.writeVideo(gameField.getVideoStream());
      stream.writeColour(gameField.getColourStream());
      return stream;
    }

    public void tick() {
      for (GameElement ghost: ghosts) {
          ghost.tick();
      }
      if (pacman != null) {
          pacman.tick();
      }
      if (controller != null) {
          controller.tick();
      }

      if (clearedLevel()) {
        nextLevel();
      }
      gameField.update(this);
    }

    private boolean clearedLevel() {
      return (usingPills && pills.isEmpty()) &&
          (usingPowerPills && powerPills.isEmpty());
    }

    public void setMaxLevels(int numberOfLevels) {
      level.setMaxLevel(numberOfLevels);
    }

    public void setLevel(int currentLevel) {
      level.setLevel(currentLevel);
    }

    private boolean lastLevel() {
      return (level.last());
    }

    private void setPacman(GameElement pacman) {
      this.pacman = pacman;
      if (controller != null) {
        controller.control((Moveable)pacman);
      }
      gameField.setLocation(pacman);
    }

    private void setGate(GameElement gate) {
      this.gate = gate;
      gameField.setLocation(gate);
    }

    private void addForceField(GameElement field) {
      forceFields.add(field);
      gameField.setLocation(field);
    }

    private void addGhost(GameElement ghost) {
      ghosts.add(ghost);
      gameField.setLocation(ghost);
    }

    private void addPill(GameElement pill) {
        usingPills=true;
        pills.add(pill);
        gameField.setLocation(pill);
    }

    private void addPowerPill(GameElement pill) {
        usingPowerPills=true;
        powerPills.add(pill);
        gameField.setLocation(pill);
    }

    private void addWall(GameElement wall) {
        walls.add(wall);
        gameField.setLocation(wall);
    }

    public int getLives(){
        return status.lives();
    }

    public void setLives(int lives){
        status.setLives(lives);
    }

    public int getScore(){
        return status.score();
    }

    public void setScore(int score){
        status.setScore(score);
    }

    public void addScore(int score) {
        status.addScore(score);
    }

    public List<GameElement> getElements() {
      return  Stream.of(pills.stream(),
                        powerPills.stream(),
                        walls.stream(),
                        forceFields.stream(),
                        ghosts.stream(),
                        Collections.singletonList(pacman).stream(),
                        Collections.singletonList(gate).stream()
                        )
                    .flatMap(s -> s)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }
    
    public GameElement getGameElement(Location loc) {
      return this.gameField.getLocation(loc);
    }
    
    public GameElement getGameElementByType(Class<? extends GameElement> type) {
      if (type == Pacman.class) {
        return pacman;
      }
      if (type == Gate.class) {
        return gate;
      }
      return null;
    }

    public void addElement(GameElement element) {
      if (element instanceof Pacman) {
        setPacman(element);
      } else if (element instanceof Ghost) {
        addGhost(element);
      } else if (element instanceof Pill) {
        addPill(element);
      } else if (element instanceof PowerPill) {
        addPowerPill(element);
      } else if (element instanceof Wall) {
        addWall(element);
      } else if (element instanceof Gate) {
        setGate(element);
      } else if (element instanceof ForceField) {
        addForceField(element);
      }
    }

    public void loseLife() {
      status.loseLife();
      if (status.lives() == 0) {
        gameOver=true;
      }
    }

    public void triggerEffect(GameElement element) {
      if (element instanceof Pill) {
        pillEffect(element);
      } else if (element instanceof PowerPill) {
        powerPillEffect(element);
      }  
    }
    private void pillEffect(GameElement pill) {
      addScore(pill.score());
      pills.remove(pill);
    }

    private void powerPillEffect(GameElement pill) {
      addScore(pill.score());
      powerPills.remove(pill);
      pacman.triggerEffect(pill);
      for (GameElement ghost: ghosts) {
          ghost.triggerEffect(pill);
      }
    }

    public boolean gameOver() {
      return gameOver;
    }

    public void play(boolean debug) {
      if (debug) {
        tick();      
        display.refresh(this.render());
      } else {
        play();
      }
    }
    
    public void play() {
      while (!gameOver) {
        tick();      
        display.refresh(this.render());
        if (pacman.isDead()) {
          display.flash();
          pacman.restart();
        }
      }
    }
    
    public void init(InputController controller, Display display) {
      this.display = display;
      this.controller = controller;
      parse();
      controller.control((Moveable)pacman);
      display.init(gameField.width(), gameField.height());
    }

    public GameField getGameField() {
      return gameField;
    }
    
    public void setGameField(GameField gameField) {
      this.gameField = gameField;
      this.status.setColumns(gameField.width());
    }

    private void nextLevel() {
      if (lastLevel()) {
        gameOver=true;
        pacman.restart();
        gameField.printGameOver();
      } else {
        pacman.restart();
        ghosts.clear();
        walls.clear();
        pills.clear();
        powerPills.clear();
        forceFields.clear();
        level.nextLevel();
        parse();
      }
    }  

    public static void main(String [] args) {
      ArgParser parser = new ArgParser(args);
      String file = (parser.hasMapFile()) ? parser.getMapFile() : "src/test/resources/data/pacman.txt";

      Level level = new Level();
      level.readFile(file);
      level.setMaxLevel(99);
      InputController keyboard = new Keyboard();
      Display display;
      if (parser.useColour()) {
        display = new ColourDisplay(System.out);
      } else {
        display = new MonoDisplay(System.out);
      }
      Game game = new Game(level);
      if (!parser.getDebug()) {
        game.animateIcons();
      }
      game.init(keyboard, display);
      game.play(parser.getDebug());
    }
}
