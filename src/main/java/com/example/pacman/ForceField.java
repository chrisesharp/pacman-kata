package com.example.pacman;
import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.*;

public class ForceField extends GameElement {
    private final Colour COLOUR = BLACK;

    public ForceField(Location location) {
        super(location);
    }
    
    public Colour getColour() {
      return COLOUR;
    }
}
