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
    private Direction direction;
    private boolean moving;
    private static final Colour[] COLOURS = {
      RED,
      GREEN,
      PURPLE,
      CYAN
    };
    private static final int SCORE=200;
    private static final int PANIC_LEVEL=50;
    private static final int CALM_LEVEL=0;
    private static int colour=0;
    private Colour normalColour;
    private static final Colour PANIC_COLOUR = BLUE;
    private boolean passedGate=false;
    private Random randomizer = new Random();
    private Behaviour behaviour = new CalmBehaviour();

    public Ghost(Location location) {
        super(location);
        normalColour = COLOURS[colour%COLOURS.length];
        colour++;
        setDirection(UP);
    }

    @Override
    public void tick() {
    	behaviour.tick();
        chooseDirection();
        move();
        checkCollisions();
    }

    @Override
    public void triggerEffect(GameElement element) {
      if (element instanceof PowerPill) {
        panic();
      }
      if (element instanceof Pacman) {
        behaviour.triggerPacmanEffect((Pacman) element);
      }
    }
    
    private void chooseDirection() {
      Direction next = chooseOption(gatherOptions());
      if (next != null) {
        setDirection(next);
      }
    }

    private void move() {
      if (behaviour.shouldMove() && isClear(direction())) {
          setLocation(location().next(direction()));
      }
    }
    
    private void checkCollisions() {
      GameElement pacman = super.getGame().getGameElementByType(Pacman.class);
      if (pacman != null && location().equals(pacman.location())) {
          triggerEffect(pacman);
      }
      GameElement gate = super.getGame().getGameElementByType(Gate.class);
      if (gate != null && location().equals(gate.location()))  {
        passedGate = true;
      }
    }

    private List<Direction> gatherOptions() {
      List<Direction> options = new ArrayList<>();
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
        option = behaviour.noChoiceDirection();
      } else {
        option = chooseRandom(options);
      }
      return option;
    }

    public void panic() {
    	behaviour = new PanickedBehaviour();
        setIcon(GhostToken.getToken(PANIC_LEVEL));
    }

    @Override
    public String render() {
      return this.icon();
    }
    
    @Override
    public Colour getColour() {
      return behaviour.getColour();
    }

    @Override
    public void kill() {
      behaviour = new CalmBehaviour();
      setIcon(GhostToken.getToken(CALM_LEVEL));
      restart();
      super.getGame().addScore(SCORE);
    }

    @Override
    public void restart() {
      passedGate = false;
      super.restart();
    }
    
    public boolean isClear(Direction direction) {
      GameElement el = super.getGame().getGameElement(location().next(direction));
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
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ghost) {
            final Ghost ghost = (Ghost) obj;
            return (this.location().equals(ghost.location()));
        } else {
          return false;
        }
    }
    
    @Override
    public int hashCode() {
      return super.hashCode() * 31;
    }
    
    private interface Behaviour {
    	void tick();
    	Colour getColour();
		boolean panicked();
		Direction noChoiceDirection();
    	boolean shouldMove();
    	void triggerPacmanEffect(Pacman pacman);
    	
    }
    
    private class PanickedBehaviour implements Behaviour {

    	private int turnsLeft = PANIC_LEVEL;
    	
		@Override
		public void tick() {
			GameElement pacman = getGame().getGameElementByType(Pacman.class);
	        Location pacmanLocation;
	        pacmanLocation = (pacman!=null) ? pacman.location() : location();
	        setDirection(location().avoid(pacmanLocation));
	        turnsLeft--;
	        if (turnsLeft == 0) {
	        	behaviour = new CalmBehaviour();
	        }
	        setIcon(GhostToken.getToken(turnsLeft));
		}

		@Override
		public Direction noChoiceDirection() {
			return null;
		}

		@Override
		public boolean shouldMove() {
			return turnsLeft%2==0;
		}

		@Override
		public void triggerPacmanEffect(Pacman pacman) {
			kill();
		}

		@Override
		public boolean panicked() {
			return true;
		}

		@Override
		public Colour getColour() {
			return PANIC_COLOUR;
		}
    	
    }
    
    private class CalmBehaviour implements Behaviour {

		@Override
		public void tick() {
			// No-op
		}

		@Override
		public Direction noChoiceDirection() {
			return direction().turnBack();
		}

		@Override
		public boolean shouldMove() {
			return true;
		}

		@Override
		public void triggerPacmanEffect(Pacman pacman) {
			pacman.kill();
		}

		@Override
		public boolean panicked() {
			return false;
		}

		@Override
		public Colour getColour() {
			return normalColour;
		}
    	
    }
}
