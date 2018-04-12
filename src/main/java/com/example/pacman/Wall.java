package com.example.pacman;

public class Wall extends GameElement {
    public Wall(Location location) {
        super(location);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wall) {
            final Wall gate = (Wall) obj;
            return (this.location().equals(gate.location()));
        } else {
          return false;
        }
    }
    
    @Override
    public int hashCode() {
      return super.hashCode() * 13;
    }
}
