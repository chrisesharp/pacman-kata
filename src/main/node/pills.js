'use strict';

const GameElement = require('./game-elements.js');
const Colour = require('./colour.js');
    
const icons = [
    {icon:".", points:10,colour:Colour.WHITE},
    {icon:"o", points:50,colour:Colour.BLINK},
    {icon:"◉", points:50,colour:Colour.BLINK}
  ];

module.exports =
class Pill extends GameElement {
  constructor ({x,y},icon) {
    super ({x,y}, icon);
    this.points = icons.find((pill)=> {return pill.icon == icon}).points;
  } 
  
  static isPill(token) {
    return (icons.filter(element => element.icon == token).length>0);
  }
  
  colour() {
    return this.getColour(this.image);
  }
  
  score() {
    return this.points;
  }
  
  getColour(icon) {
    let token= icons.find((a) => { return a.icon.indexOf(icon)>=0;});
    if (token) {
      return token.colour;
    }
  }
}