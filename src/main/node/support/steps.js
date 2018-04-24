const { Given, When, Then } = require('cucumber')
const assert = require('assert')
const { expect } = require('chai')
const g = require('../game-elements.js')

let World = require('./world.js').World;

// Givens
Given('the command arg {string}', function (string) {
  this.addCommandArg(string);
  });
Given('the screen column width is {int}', function (cols) {
  this.setColumns(cols);
});
Given('the player has {int} lives', function (lives) {
  this.setPlayerLives(lives);
});
Given('the player score is {int}', function (score) {
  this.setPlayerScore(score);
});
Given('the game state is', function (docString) {
  this.setGame(docString);
  });
Given('the game field of {int} x {int}', function (x, y) {
  this.setPlayfield(x,y)
  });
Given('a pacman at {int} , {int} facing {string}', function (x, y, facing) {
  this.setPacman(x,y,facing);
  });
Given('a ghost at {int} , {int}', function (x, y) {
  this.addGhost(x,y);
  });
Given('walls at the following places:', function (dataTable) {
  this.addWalls(dataTable);
  });
Given('the score is {int}', function (score) {
  this.setScore(score);
  });
Given('the lives are {int}', function (lives) {
  this.setLives(lives);
  });
Given('a colour display', function (callback) {
  // Write code here that turns the phrase above into concrete actions
  callback(null, 'pending');
  });
Given('a display', function (callback) {
  this.setDisplay();
  callback();
  });
Given('a game with {int} levels', function (levels, docString, callback) {
  this.setGame(docString);
  callback();
  });
Given('the ANSI {string} sequence is {string}', function (sequence, hex, callback) {
  this.addSequence(sequence,hex)
  callback();
  });
Given('the game uses animation', function (callback) {
  this.useAnimation();
  callback();
  });
Given('the max level  is {int}', function (level, callback) {
  this.setLastLevel(level);
  callback();
  });
  
Given('this is the last level', function (callback) {
  this.makeLastLevel();
  callback();
});
Given('this is level {int}', function (level, callback) {
  this.setLevel(level);
  callback();
  });

// Whens
When('I run the command with the args', function () {
   this.runCommand();
  });

When('we render the status line', function () {
    this.renderStatus();
  });

When('we render the game field', function () {
   this.renderGameField();
  });

When('we parse the state', function (callback) {
  this.parse();
  callback();
  });

When('we play {int} turn(s)', function (turn) {
  for (let i=0;i<turn;i++) {
    this.tick();
  }
  });

When('we render the game', function () {
  this.render();
  });

When('the display renders the icon {string} in yellow and refreshes', function (icon, callback) {
  // Write code here that turns the phrase above into concrete actions
  callback(null, 'pending');
  });

When('the game refreshes the display with the buffer {string}', function (buffer, callback) {
  // Write code here that turns the phrase above into concrete actions
  callback(null, 'pending');
  });

When('the player presses {string}', function (key) {
  this.keyPressed(key);
  });
  
When('initialize the display', function (callback) {
  this.initDisplay();
  callback();
  });
  
When('we refresh the display with the buffer {string}', function (string, callback) {
    this.refreshDisplay(string);
    callback();
  });

// Thens

Then('I should get the following output:', function (docString, callback) {
  let received = this.commandOutput;
  assert.equal(received, docString);
  callback();
  });

Then('the game field should be {int} x {int}', function (x, y) {
  assert.equal(x, this.game.playfield.width());
  assert.equal(y, this.game.playfield.height());
  });

Then('the player should have {int} lives', function (lives) {
  assert.equal(lives, this.game.lives);
  });

Then('the score should be {int}', function (score) {
  assert.equal(score, this.game.score);
  });

Then('pacman should be at {int} , {int}', function (x, y) {
  this.pacman = this.getPacman();
  assert.equal(x, this.pacman.x);
  assert.equal(y, this.pacman.y);
  });

Then('the game lives should be {int}', function (lives) {
  assert.equal(lives, this.game.lives)
  });

Then('the game score should be {int}', function (score) {
  assert.equal(score, this.game.score)
  });

Then('the game screen should be', function (docString) {
  assert.equal(docString, this.game.output)
  });

Then('pacman should be facing {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.facing(direction), this.pacman.direction());
  });

Then('then pacman should go {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.facing(direction), this.pacman.direction());
  });

Then('pacman should be dead', function (callback) {
  assert.equal(false, this.getPacman().isAlive());
  callback();
 });
 
Then('pacman should be alive', function (callback) {
  assert.equal(true, this.getPacman().isAlive());
  callback();
 });

Then('ghost should be at {int} , {int}', function (x, y, callback) {
  assert.equal(true, this.isGhostAtLocation({x,y}));
  callback();
  });
  
Then('ghost at {int} , {int} should be calm', function (x, y, callback) {
  assert.equal(false, this.isGhostPanicked({x,y}));
  callback();
});
  
Then('ghost at {int} , {int} should be panicked', function (x, y, callback) {
  assert.equal(true, this.isGhostPanicked({x,y}));
  callback();
});

Then('the game dimensions should equal the display dimensions', function (callback) {
  assert(this.gameDimensionsMatchDisplay())
  callback();
});

Then('the display byte stream should be', function (dataTable, callback) {
  let stream = dataTable.rawTable.map((x)=>this.ansiMap[x]).join('');
  let received = this.convertToHex(this.displayOut());
  assert.equal(stream, received);
  callback();
  });

Then('there should be a force field at {int} , {int}', function (x, y, callback) {
  let ff = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.forcefield==true);
  assert(ff)
  callback();
  });
  
Then('there should be a gate at {int} , {int}', function (x, y, callback) {
  let gate = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.gate==true);
  assert(gate)
  callback();
  });
Then('there should be a wall at {int} , {int}', function (x, y, callback) {
  let wall = this.game.walls.filter((wall)=> wall.x==x && wall.y==y);
  assert(wall);
  callback();
  });
Then('there should be a {int} point pill at {int} , {int}', function (points, x, y, callback) {
  let pill = this.game.pills.filter((pill)=> pill.x==x && pill.y==y && pill.points==points);
  assert(pill)
  callback();
  });
  

         


