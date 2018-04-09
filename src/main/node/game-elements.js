"use strict";

module.exports =

class GameElement {
  constructor (options) {
    if (options) {
      this.x = options.coords.x;
      this.y = options.coords.y;
      this.image = options.icon;
    }
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