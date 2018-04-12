package com.example.pacman;
import static com.example.pacman.Location.Direction;
import static com.example.pacman.Location.Direction.*;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class Ghost extends GameElement implements Moveable {
    private GameEngine game;
    private Direction direction;
    private boolean moving;
    private static Colour[] COLOURS = {
      RED,
      GREEN,
      PURPLE,
      CYAN
    };
    private static final int SCORE=200;
    private static final int PANIC_LEVEL=50;
    private static final int CALM_LEVEL=0;
    private static int colour=0;
    private Colour NORMAL_COLOUR;
    private final Colour PANIC_COLOUR = BLUE;
    private int panic=0;
    private boolean passedGate=false;
    private Random randomizer = new Random();

    public Ghost(Location location) {
        super(location);
        NORMAL_COLOUR = COLOURS[colour%COLOURS.length];
        colour++;
        setDirection(UP);
    }
    
    @Override
    public void setGame(GameEngine game) {
      this.game = game;
    }

    @Override
    public void tick() {
        managePanic();
        chooseDirection();
        move();
        checkCollisions();
    }
    
    private void managePanic() {
      if (panicked()) {
        GameElement pacman = game.getGameElementByType(Pacman.class);
        Location pacmanLocation;
        pacmanLocation = (pacman!=null) ? pacman.location() : this.location();
        setDirection(location().avoid(pacmanLocation));
        panic--;
        setIcon(GhostToken.getToken(panic));
      }
    }

    @Override
    public void triggerEffect(GameElement element) {
      if (element instanceof PowerPill) {
        panic();
      }
      if (element instanceof Pacman) {
        if (panicked()) {
          this.kill();
        } else {
          element.kill();
        }
      }
    }
    
    private void chooseDirection() {
      Direction next = chooseOption(gatherOptions());
      if (next != null) {
        setDirection(next);
      }
    }

    private void move() {
      if (panic%2==0) {

        if (isClear(direction())) {
          setLocation(location().next(direction()));
        }
      }
    }
    
    private void checkCollisions() {
      GameElement pacman = game.getGameElementByType(Pacman.class);
      if (pacman != null) {
        if (location().equals(pacman.location())) {
          triggerEffect(pacman);
        }
      }
      GameElement gate = game.getGameElementByType(Gate.class);
      if (gate != null && (location().equals(gate.location())))  {
        passedGate = true;
      }
    }

    private List<Direction> gatherOptions() {
      List<Direction> options = new ArrayList<Direction>();
      List<Direction> possibleRoutes = Arrays.asList(direction(),
                                                      direction().turnLeft(),
                                                      direction().turnRight());
      for (Direction dir: possibleRoutes) {
        if (isClear(dir)) {
          options.add(dir);
        }
      }
      return options;
    }

    private Direction chooseRandom(List<Direction> options){
      return options.get(randomizer.nextInt(options.size()));
    }

    private Direction chooseOption(List<Direction> options) {
      Direction option;
      if (options.isEmpty()) {
        option = (panicked()) ? null : direction().turnBack();
      } else {
        option = chooseRandom(options);
      }
      return option;
    }

    public void panic() {
        panic=PANIC_LEVEL;
        setIcon(GhostToken.getToken(panic));
    }

    protected boolean panicked() {
      return (panic > CALM_LEVEL);
    }

    @Override
    public String render() {
      String icon = this.icon();
      return icon;
    }
    
    @Override
    public Colour getColour() {
      return panicked() ? PANIC_COLOUR : NORMAL_COLOUR;
    }

    @Override
    public void kill() {
      panic=CALM_LEVEL;
      setIcon(GhostToken.getToken(panic));
      restart();
      game.addScore(SCORE);
    }

    @Override
    public void restart() {
      passedGate = false;
      super.restart();
    }
    
    public boolean isClear(Direction direction) {
      GameElement el = game.getGameElement(location().next(direction));
      return !(el instanceof Wall || 
               el instanceof ForceField || 
               (el instanceof Gate && passedGate)
              );
    }
    public void setDirection(Direction direction) {
      this.direction = direction;
    }
    public Direction direction() {
      return direction;
    }
    public boolean moving() {
      return moving;
    }
    public void move(Direction direction) {
      // Ghosts are not controlled externally
    }

    public void go() {
      moving = true;
    }

    public void stop() {
      moving = false;
    }
}
