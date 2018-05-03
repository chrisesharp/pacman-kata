package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;
import static com.example.pacman.Location.Direction;

public class Pacman extends GameElement implements Moveable {
    private boolean alive=true;
    private boolean iconState=true;
    private static final Colour colour = YELLOW;
    private Direction direction;
    private boolean moving=true;

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
      super.getGame().loseLife();
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
            GameElement e = super.getGame().getGameElement(location());
            if (e !=null) {
              e.triggerEffect(this);
            }
        }
    }

    @Override
    public void move(Direction direction) {
      if (direction != null && isClear(direction)) {
        setDirection(direction);
        go();
      }
    }

    @Override
    public String render() {
      String icon;
      if (alive) {
        if (super.getGame().animatedIcons()) {
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
      return colour;
    }

    public boolean isClear(Direction direction) {
      GameElement e = super.getGame().getGameElement(location().next(direction));
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
    
    @Override
    public boolean equals(Object obj) {
      if (!super.equals(obj)) {
        return false;
      } else {
        Pacman other = (Pacman) obj;
        return (this.getColour() == other.getColour());
      }
    }
    
    @Override
    public int hashCode() {
      return super.hashCode() * 27;
    }
}
