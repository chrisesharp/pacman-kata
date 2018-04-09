'use strict';

module.exports =

class GameElement {
  constructor ({x,y}, icon) {
    this.x = x;
    this.y = y;
    this.image = icon;
  }
  
  icon() {
    return this.image;
  }
  
  colour() {
    return this.colour;
  }
  
  setLocation({x,y}) {
    this.x = x;
    this.y = y;
  }
  
  getLocation() {
    return {x:this.x,y:this.y};
  }
}