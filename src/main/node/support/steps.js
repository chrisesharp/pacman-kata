const { Given, When, Then } = require('cucumber')
const assert = require('assert')
const { expect } = require('chai')
const g = require('../game-elements.js')

this.World = require('./world.js').World;

// Givens
Given('the game state is', function (docString) {
  this.setGame(docString);
  });
Given('the game field of {int} x {int}', function (x, y) {
  this.setPlayfield(x,y)
  });
Given('a pacman at {int} , {int} facing {string}', function (x, y, facing) {
  this.setPacman(x,y,facing);
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
When('we parse the state', function (callback) {
  this.parse();
  callback();
  });

When('we play {int} turn(s)', function (turn) {
  for (i=0;i<turn;i++) {
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

Then('the game field should be {int} x {int}', function (x, y) {
  assert.equal(this.game.playfield.width(),x);
  assert.equal(this.game.playfield.height(),y);
  });

Then('the player has {int} lives', function (lives) {
  assert.equal(this.game.lives,lives);
  });

Then('the player score is {int}', function (score) {
  assert.equal(this.game.score,score);
  });

Then('pacman is at {int} , {int}', function (x, y) {
  this.pacman = this.getPacman();
  assert.equal(this.pacman.x,x);
  assert.equal(this.pacman.y,y);
  });

Then('the game lives should be {int}', function (lives) {
  assert.equal(this.game.lives,lives)
  });

Then('the game score should be {int}', function (score) {
  assert.equal(this.game.score,score)
  });

Then('the game screen is', function (docString) {
  assert.equal(this.game.output,docString)
  });

Then('pacman is facing {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.pacman.direction(),this.facing(direction));
  });

Then('then pacman goes {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.pacman.direction(),this.facing(direction));
  });

Then('pacman is dead', function (callback) {
  assert.equal(this.getPacman().isAlive(),false);
  callback();
 });
 
Then('pacman is alive', function (callback) {
  assert.equal(this.getPacman().isAlive(),true);
  callback();
 });

Then('ghost is at {int} , {int}', function (x, y, callback) {
  assert.equal(this.isGhostAtLocation({x,y}), true);
  callback();
  });
  
Then('ghost at {int} , {int} should be calm', function (x, y, callback) {
  assert.equal(this.isGhostPanicked({x,y}),false);
  callback();
});
  
Then('ghost at {int} , {int} should be panicked', function (x, y, callback) {
  assert.equal(this.isGhostPanicked({x,y}),true);
  callback();
});

Then('the game dimensions should equal the display dimensions', function (callback) {
  assert(this.gameDimensionsMatchDisplay())
  callback();
});

Then('the display byte stream should be', function (dataTable, callback) {
  let stream = dataTable.rawTable.map((x)=>this.ansiMap[x]).join('');
  let received = this.convertToHex(this.displayOut());
  assert.equal(received,stream);
  callback();
  });

Then('there is a force field at {int} , {int}', function (x, y, callback) {
  let ff = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.forcefield==true);
  assert(ff)
  callback();
  });
  
Then('there is a gate at {int} , {int}', function (x, y, callback) {
  let gate = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.gate==true);
  assert(gate)
  callback();
  });
Then('there is a wall at {int} , {int}', function (x, y, callback) {
  let wall = this.game.walls.filter((wall)=> wall.x==x && wall.y==y);
  assert(wall);
  callback();
  });
Then('there is a {int} point pill at {int} , {int}', function (points, x, y, callback) {
  let pill = this.game.pills.filter((pill)=> pill.x==x && pill.y==y && pill.points==points);
  assert(pill)
  callback();
  });
  

         


