const { setWorldConstructor } = require("cucumber");
const Game = require("../game.js");
const Playfield = require("../playField.js");
const g = require("../game-elements.js");
const Pacman = require("../pacman.js");
const Ghost = require("../ghost.js");
const Wall = require("../walls.js");
const Direction = require("../direction.js");
const Keyboard = require("../keyboard.js");
const Display = require("../display.js");
const ColourDisplay = require("../colourdisplay.js");
const ArrayList = require("arraylist");
const { REV, ESC, CLR, RST, REVON, REVOFF, BLINK } = require("../ansi-term.js");


function CustomWorld() {
  this.game = new Game();
  this.keyboard = new Keyboard(this.game);
  this.ansiMap = [];
  this.command = "node";
  this.commandArgs = new ArrayList;
  this.commandArgs.add("index.js");
  this.commandOutput = "";
  this.serviceResponse = null;
}

CustomWorld.prototype.addCommandArg = function(arg) {
  this.commandArgs.add(arg.toString());
}

CustomWorld.prototype.setColumns = function(cols) {
  this.columns = cols;
}

CustomWorld.prototype.runCommand = function() {
  const
    { spawnSync } = require( "child_process" ),
    cmd = spawnSync( this.command.toString(), this.commandArgs.toArray(), {
      detached: false,
      stdio: ["inherit", "pipe", process.stderr]
    });
  let received = `${cmd.stdout.toString()}`;
  received = received.replace(RST, "");
  received = received.replace(CLR, "");
  received = received.replace(REVON, "");
  received = received.replace(REVOFF, "");
  received = received.replace(REV, "");
  received = received.replace(BLINK, "");
  received = received.replace(/\n$/, "");
  this.commandOutput = received;
}

CustomWorld.prototype.setGame = function(docString) {
  this.game.setInput(docString);
}

CustomWorld.prototype.setPlayfield = function(x,y) {
  let playfield = new Playfield(x,y);
  this.game.setPlayfield(playfield);
}

CustomWorld.prototype.setPlayer = function(player) {
  this.game.setPlayer(player);
}

CustomWorld.prototype.setPacman = function(x,y,facing) {
  let direction = Direction.facing(facing);
  this.pacman = new Pacman({coords:{x,y}});
  this.pacman.setDirection(direction);
  this.pacman.alive = true;
  this.pacman.setPlayfield(this.game.getPlayfield());
  this.pacman.setGame(this.game);
  this.game.setPacman(this.pacman);
}

CustomWorld.prototype.getPacman = function() {
  return this.game.getPacman();
}

CustomWorld.prototype.isGhostAtLocation = function(loc) {
  let element = this.game.getPlayfield().getLocation(loc);
  return element instanceof Ghost; 
}

CustomWorld.prototype.isGhostPanicked = function(loc) {
  let element = this.game.getPlayfield().getLocation(loc);
  return element.isPanicked(); 
}

CustomWorld.prototype.addGhost = function(x, y) {
  this.game.addGhost(new Ghost({coords:{x, y}, icon:"M"}));
}
CustomWorld.prototype.addWalls = function(dataTable) {
  dataTable.rows().forEach((entry) => {
    let icon = entry[0];
    let x = entry[1];
    let y = entry[2];
    this.game.addWall(new Wall({coords:{x, y},icon:icon}));
  });
}

CustomWorld.prototype.setScore = function(score) {
  this.game.setScore(score);
}

CustomWorld.prototype.setPlayerScore = function(score) {
  this.score = score;
}

CustomWorld.prototype.callbackPromiseHandler= function(resolve, reject) {
  return function(error, data, response) {
      if (!error) {
        resolve(data[0].player + ":" + data[0].score);
      } else {
        reject(error);
      }
    }
}

CustomWorld.prototype.postScore = function() {
  var game = this.game;
  return new Promise((resolve, reject) => {
    var callback = this.callbackPromiseHandler(resolve, reject);
    game.postScore(callback);
  });
}

CustomWorld.prototype.getScores = function() {
  var game = this.game;
  return new Promise((resolve, reject) => {
    var callback = this.callbackPromiseHandler(resolve, reject);
    game.getScores(callback);
  });  
}

CustomWorld.prototype.setServiceResponse = function(data) {
  this.serviceResponse = data;
}

CustomWorld.prototype.setLives = function(lives) {
  this.game.setLives(lives);
}

CustomWorld.prototype.setPlayerLives = function(lives) {
  this.lives = lives;
}

CustomWorld.prototype.setLevel = function(level) {
  this.game.setLevel(level);
}

CustomWorld.prototype.setLastLevel = function(level) {
  this.game.setLastLevel(level);
}

CustomWorld.prototype.makeLastLevel = function() {
  this.game.setLevel(1);
  this.game.setLastLevel(1);
}

CustomWorld.prototype.useAnimation = function() {
  this.game.useAnimation();
}

CustomWorld.prototype.setDisplay = function() {
  let display = new Display(this.game);
  this.game.setDisplay(display);
}

CustomWorld.prototype.setColourDisplay = function() {
  let display = new ColourDisplay(this.game);
  this.game.setDisplay(display);
}

CustomWorld.prototype.initDisplay = function() {
  let outStream = [];
  this.game.getDisplay().init(outStream);
}

CustomWorld.prototype.addSequence = function(sequence, hex) {
  this.ansiMap[sequence]=hex;
}

CustomWorld.prototype.parse = function() {
  this.game.parse();
}

CustomWorld.prototype.tick = function() {
  this.game.tick();
}

CustomWorld.prototype.render = function() {
  this.game.render();
}

CustomWorld.prototype.renderStatus = function() {
  this.commandOutput = this.game.renderStatus(this.lives,
                                              this.score,
                                              this.columns).chars;
}

CustomWorld.prototype.renderGameField = function() {
  this.commandOutput = this.game.renderField().chars;
}

CustomWorld.prototype.facing = function(facing) {
  return Direction.facing(facing);
}

CustomWorld.prototype.keyPressed = function(key) {
  this.keyboard.keyPressed({name:key});
}


CustomWorld.prototype.gameDimensionsMatchDisplay = function() {
  let playfield = this.game.getPlayfield();
  let display = this.game.getDisplay();
  return (playfield.width()===display.width() && playfield.height()===display.height());
}

CustomWorld.prototype.refreshDisplay = function(string, colour) {
  var hex="";
  for (var i=0; i< string.length; i++) {
    hex += string.codePointAt(i).toString(16).toUpperCase();
  }
  this.ansiMap[string]=hex;
  let colourBuffer = new ArrayList;
  colourBuffer.add(colour);
  this.game.getDisplay().refresh(string, colourBuffer);
}

CustomWorld.prototype.displayOut = function() {
  return this.game.getDisplay().output;
}

CustomWorld.prototype.convertToHex = function(output) {
  let received = "";
  output = output.join("").toString();
  for (var i=0;i<output.length;i++) {
    var hex= output.codePointAt(i).toString(16).toUpperCase();
    received += "0".repeat(2 - hex.length) + hex;
  } 
  return received;
}

setWorldConstructor(CustomWorld);
