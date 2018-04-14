"use strict";

const Game = require("./game.js");
const Keyboard = require("./keyboard.js");
const ColourDisplay = require("./colourdisplay.js");
const Display = require("./display.js");
const fs = require("fs");
const readline = require("readline");
const MainLoop = require("mainloop.js");
const args = require("minimist")(process.argv.slice(2));
const interval_ms = 100;
var game;

function draw() {
  game.refreshDisplay();
  if (game.debug) {
    game.gameOver = true;
  } else {
    if (!(game.getPacman().isAlive())) {
      game.display.flash();
      game.pacman.restart();
    }
  }
  if (game.gameOver) {
    MainLoop.stop();
    process.exit();
  }
}

function update() {
  game.tick();
}

// This is the equivalent of 'main' in node
if (require.main === module) {
  readline.emitKeypressEvents(process.stdin);
  process.stdin.setRawMode(true);
  
  let file = (args.file) ?  args.file : "data/pacman.txt";
  let colour = (args.colour) ?  args.colour : false;
  let debug = (args.debug) ?  args.debug : false;
  let contents = fs.readFileSync(file,"utf8");
  
  game = new Game(contents);
  game.setDebug(debug);
  let keyboard = new Keyboard(game);
  var display;
  if (colour==="true") {
    display = new ColourDisplay(game);
  } else {
    display = new Display(game);
  }
  display.init(process.stdout);
  game.setDisplay(display);
  game.useAnimation();
  game.parse();
  
  process.stdin.on("keypress", (str, key) => {
    keyboard.keyPressed(key);
  });
  
  MainLoop.setSimulationTimestep(interval_ms).setUpdate(update).setDraw(draw).start();  
}