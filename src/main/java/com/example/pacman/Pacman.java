package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;
import static com.example.pacman.Location.Direction;

public class Pacman extends GameElement implements Moveable {
    private GameEngine game;
    private boolean alive=true;
    private boolean iconState=true;
    private final Colour COLOUR = YELLOW;
    private Direction direction;
    private boolean moving;

    public Pacman(Location location) {
        super(location);
    }

    @Override
    public void restart() {
      super.restart();
      alive=true;
    }

    @Override
    public void kill() {
      alive=false;
      stop();
      game.loseLife();
    }

    @Override
    public boolean isDead() {
      return !alive;
    }
    
    @Override
    public void setIcon(String icon) {
      super.setIcon(icon);
      this.alive = (!icon.equals(PacmanToken.getDeadToken()));
    }

    @Override
    public void tick() {
        if (moving()) {
            if (!isClear(direction())) {
                stop();
            } else {
                setLocation(location().next(direction()));
                iconState=!iconState;
            }
            GameElement e = game.getGameElement(location());
            if (e !=null) {
              e.triggerEffect(this);
            }
        }
    }

    @Override
    public void move(Direction direction) {
      if (isClear(direction)) {
        setDirection(direction);
        go();
      }
    }

    @Override
    public void setGame(GameEngine game) {
      this.game = game;
    }

    @Override
    public String render() {
      String icon;
      if (alive) {
        if (game.animatedIcons()) {
          icon = (iconState) ? PacmanToken.getToken(direction()) :  PacmanToken.getAltToken(direction());
        } else {
          icon = PacmanToken.getToken(direction());
        }
      } else {
        icon = PacmanToken.getDeadToken();
      }
      return icon;
    }
    
    @Override
    public Colour getColour() {
      return COLOUR;
    }

    public boolean isClear(Direction direction) {
      GameElement e = game.getGameElement(location().next(direction));
      return !(e instanceof Wall || e instanceof Gate);
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

    public void go() {
      moving = true;
    }

    public void stop() {
      moving = false;
    }
}
