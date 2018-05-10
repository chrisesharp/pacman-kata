package com.example.pacman;
import java.util.Map;
import java.util.HashMap;

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
    
    private static final Map<Direction,Location> deltasMap = new HashMap<Direction, Location>();
    static {
      deltasMap.put(Direction.LEFT, new Location(-1,0));
      deltasMap.put(Direction.RIGHT, new Location(1,0));
      deltasMap.put(Direction.UP, new Location(0,-1));
      deltasMap.put(Direction.DOWN, new Location(0,1));
      deltasMap.put(null, new Location(0,0));
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
        Location delta = deltasMap.get(direction);
        return (new Location(x+delta.x(), y+delta.y()));
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
