package com.example.pacman;

import static com.example.pacman.Colours.Colour;
import static com.example.pacman.Colours.Colour.DEFAULT;

public abstract class GameElement {
    private Location startingLocation, location;
    private String icon;
    private GameEngine game;

    public GameElement(Location location) {
      this.location = location;
      startingLocation = location;
    }

    public void setLocation(Location location){
      this.location= location;
    }

    public Location location() {
      return location;
    }

    public void triggerEffect(GameElement element) {
      // Base elements have no effect by default
    }
    public void kill() {
      // Base elements are not killable by default
    }
    public boolean isDead() {
      return true;
    }

    public boolean equals(Object obj) {
      if (obj == null) {
          return false;
      } else {
          final GameElement element = (GameElement) obj;
          return (this.location().equals(element.location()));
      }
    }

    public String render() {
      return icon;
    }
    
    public Colour getColour() {
      return DEFAULT;
    }

    public void tick() {
      // Base elements are inert and therefore have no default behaviour
    }

    public void restart() {
      setLocation(startingLocation);
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }

    public String icon() {
      return icon;
    }

    public void setGame(GameEngine game) {
        this.game = game;
    }

    public GameEngine getGame() {
      return game;
    }

    public int score() {
      return 0;
    }
}
