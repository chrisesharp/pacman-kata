"use strict";

const Dir = require("./direction.js").Direction;


const keyMap = {
  "j":    Dir.LEFT,
  "left": Dir.LEFT,
  "i":    Dir.UP,
  "up":   Dir.UP,
  "l":    Dir.RIGHT,
  "right":Dir.RIGHT,
  "m":    Dir.DOWN,
  "down": Dir.DOWN
};

module.exports = 

class Keyboard {
  constructor (game) {
    this.game = game;
  } 
  
  keyPressed (key) {
    if(key.sequence === "\u0003" || key.sequence === "\u001B") {
      process.stdin.setRawMode(false);
      process.exit();
    }
    let keypress = keyMap[key.name];
    if(keypress === "undefined") {
      return;
    }
    this.game.keyPressed(keypress);
  }
}

