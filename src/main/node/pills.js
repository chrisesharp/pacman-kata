"use strict";

const GameElement = require("./game-elements.js");
const Colour = require("./colour.js");
    
const icons = [
    {icon:".", points:10,colour:Colour.WHITE},
    {icon:"o", points:50,colour:Colour.BLINK},
    {icon:"◉", points:50,colour:Colour.BLINK}
  ];

module.exports =
class Pill extends GameElement {
  constructor (options) {
    super(options);
    if (options) {
      this.points = icons.find((pill) => {return pill.icon === options.icon;}).points;
    }
  } 
  
  static isPill(token) {
    return (icons.filter((element) => element.icon === token).length>0);
  }
  
  getElement(coords, icon) {
    if (Pill.isPill(icon)) {
      return new Pill({coords, icon});
    }
  }
  
  addToGame(game) {
    game.addPill(this);
  }
  
  colour() {
    return this.getColour(this.image);
  }
  
  score() {
    return this.points;
  }
  
}