"use strict";

const Pacman = require("./pacman.js");
const Ghost = require("./ghost.js");
const Wall = require("./walls.js");
const Pill = require("./pills.js");

module.exports = 

class Tokenizer {
  static getElement(coords, icon) {
    const types = [ new Pacman, 
                    new Ghost, 
                    new Pill, 
                    new Wall
                  ]
    let result;
    types.forEach( (obj) => {
      let element = obj.getElement(coords, icon);
      if (element) { result = element; }
    });
    return result;
  }
}