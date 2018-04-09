'use strict';

module.exports =
class Playfield {
  constructor (width, height) {
    this.data = new Array(height);
    for (var y=0;y<height;y++) {
      this.data[y] = new Array(width);
      for (var x=0;x<width;x++) {
        this.data[y][x]={colour:()=>{return 0;},
                         icon:()=>{return " "}};
      }
    }
    this.columns = width;
    this.rows = height;
  }
  
  reset() {
    for (var y=0;y<this.rows;y++) {
      for (var x=0;x<this.columns;x++) {
        this.data[y][x]={colour:()=>{return 0;},
                         icon:()=>{return " "}};
      }
    }
  }
  
  width () { return this.columns }
  
  height() { return this.rows }
  
  setLocation({x,y},element) {
    if (!element.icon) {
      let char = element;
      element = {colour:()=>{return 0},
                 icon:()=>{return char}
                }
    }
    this.data[y][x] = element;
  }
  
  getLocation({x,y}) {
    return this.data[y][x];
  }
}
