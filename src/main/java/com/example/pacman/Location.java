package com.example.pacman;
public class Location {
    enum Direction {
        UP, RIGHT, DOWN, LEFT;
        private static Direction[] vals = values();
        public Direction turnRight()
        {
            return vals[(this.ordinal()+1) % vals.length];
        }
        public Direction turnLeft()
        {
            return vals[(this.ordinal()+3) % vals.length];
        }
        public Direction turnBack()
        {
            return vals[(this.ordinal()+2) % vals.length];
        }
    }
    private int x;
    private int y;
     
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(Location loc) {
        this.x = loc.x;
        this.y = loc.y;
    }
    
    public int x() {
      return this.x;
    }
    
    public int y() {
      return this.y;
    }

    public Location next(Direction direction) {
        int dx=0;
        int dy=0;
        switch (direction) {
          case LEFT:
              dx=-1;
              dy=0;
              break;
          case RIGHT:
              dx=1;
              dy=0;
              break;
          case UP:
              dx=0;
              dy=-1;
              break;
          case DOWN:
              dx=0;
              dy=1;
              break;
          default:
              dx=0;
              dy=0;
              break;
        }
        return (new Location(x+dx, y+dy));
    }

    public Direction avoid(Location loc) {
        Direction heading;
        if (this.isLevelWith(loc)) {
          heading = (this.isLeftOf(loc)) ? Direction.LEFT : Direction.RIGHT;
        } else {
          heading = (this.isAbove(loc)) ? Direction.UP : Direction.DOWN;
        }
        return heading;
    }

    public Direction follow(Location loc) {
        Direction heading;
        if (this.isLevelWith(loc)) {
          heading = (this.isLeftOf(loc)) ? Direction.RIGHT : Direction.LEFT;
        } else {
          heading = (this.isAbove(loc)) ? Direction.DOWN : Direction.UP;
        }
        return heading;
    }
    
    private boolean isLeftOf(Location loc) {
      return (this.x < loc.x);
    }
    
    private boolean isAbove(Location loc) {
      return (this.y < loc.y);
    }
    
    private boolean isLevelWith(Location loc) {
      return (this.y == loc.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
          Location loc = (Location) obj;
          return (loc.x == this.x && loc.y == this.y);
        } else {
          return false;
        }
    }
    
    @Override
    public int hashCode() {
      return (13 * (13 + this.x) + this.y);
    }
}
