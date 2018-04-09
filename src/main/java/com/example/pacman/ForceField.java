package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

public class ForceField extends GameElement {
    private final Colour COLOUR = BLACK;

    public ForceField(Location location) {
        super(location);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            final ForceField field = (ForceField) obj;
            return (this.location().equals(field.location()));
        }
    }
    
    public Colour getColour() {
      return COLOUR;
    }
}
