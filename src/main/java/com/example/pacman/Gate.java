package com.example.pacman;
public class Gate extends GameElement {
    public Gate(Location location) {
        super(location);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            final Gate gate = (Gate) obj;
            return (this.location().equals(gate.location()));
        }
    }
}
