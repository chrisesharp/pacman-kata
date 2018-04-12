package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

public class ForceField extends GameElement {
    private static final Colour colour = BLACK;

    public ForceField(Location location) {
        super(location);
    }
    
    @Override
    public Colour getColour() {
      return colour;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (!super.equals(obj)) {
        return false;
      } else {
        ForceField other = (ForceField) obj;
        return (this.getColour() == other.getColour());
      }
    }
    
    @Override
    public int hashCode() {
      return (11 * (11 * this.location().hashCode()));
    }
}
