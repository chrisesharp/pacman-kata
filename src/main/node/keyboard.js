"use strict";

const RIGHT = require("./direction.js").Direction.RIGHT;
const LEFT = require("./direction.js").Direction.LEFT;
const UP = require("./direction.js").Direction.UP;
const DOWN = require("./direction.js").Direction.DOWN;

const keyMap = {
  "j":    LEFT,
  "left": LEFT,
  "i":    UP,
  "up":   UP,
  "l":    RIGHT,
  "right":RIGHT,
  "m":    DOWN,
  "down": DOWN
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

