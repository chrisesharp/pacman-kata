const Colour = require("../colour.js");
const { Given, When, Then } = require('cucumber')
const assert = require('assert')
var chai = require("chai");
var chaiAsPromised = require("chai-as-promised");

chai.use(chaiAsPromised);

var expect = chai.expect;

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

Given('a colour display', function () {
  this.setColourDisplay();
});

Given('a display', function () {
  this.setDisplay();
});

Given('a game with {int} levels', function (levels, docString) {
  this.setGame(docString);
});

Given('the ANSI {string} sequence is {string}', function (sequence, hex) {
  this.addSequence(sequence,hex);
});

Given('the game uses animation', function () {
  this.useAnimation();
});

Given('the max level  is {int}', function (level) {
  this.setLastLevel(level);
});

Given('this is the last level', function () {
  this.makeLastLevel();
});

Given('this is level {int}', function (level) {
  this.setLevel(level);
});

Given('the user is {string}', function (player) {
  this.setPlayer(player);
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

When('we parse the state', function () {
  this.parse();
});

When('we play {int} turn(s)', function (turn) {
  for (let i=0;i<turn;i++) {
    this.tick();
  }
});

When('we render the game', function () {
  this.render();
});

When('the player presses {string}', function (key) {
  this.keyPressed(key);
});
  
When('initialize the display', function () {
  this.initDisplay();
});
  
When('we refresh the display with the buffer {string}', function (string) {
  this.refreshDisplay(string);
});

When('the display renders the icon {string} in yellow and refreshes', function (icon) {
  this.refreshDisplay(icon, Colour.YELLOW);
});

When('I post the score to the scoreboard', function () {
  // This returns a Promise
  this.postScore();
});

When('I get the scores', function () {
  // Needs to be handled as a promise in the Then rather than here
});

// Thens

Then('I should get the following output:', function (docString) {
  let received = this.commandOutput;
  assert.equal(received, docString);
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
  assert.equal(lives, this.game.lives);
});

Then('the game score should be {int}', function (score) {
  assert.equal(score, this.game.score);
});

Then('the game screen should be', function (docString) {
  assert.equal(docString, this.game.output);
});

Then('pacman should be facing {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.facing(direction), this.pacman.direction());
});

Then('then pacman should go {string}', function (direction) {
  this.pacman = this.getPacman();
  assert.equal(this.facing(direction), this.pacman.direction());
});

Then('pacman should be dead', function () {
  assert.equal(false, this.getPacman().isAlive());
 });
 
Then('pacman should be alive', function () {
  assert.equal(true, this.getPacman().isAlive());
 });

Then('ghost should be at {int} , {int}', function (x, y) {
  assert.equal(true, this.isGhostAtLocation({x,y}));
});
  
Then('ghost at {int} , {int} should be calm', function (x, y) {
  assert.equal(false, this.isGhostPanicked({x,y}));
});
  
Then('ghost at {int} , {int} should be panicked', function (x, y) {
  assert.equal(true, this.isGhostPanicked({x,y}));
});

Then('the game dimensions should equal the display dimensions', function () {
  assert(this.gameDimensionsMatchDisplay())
});

Then('the display byte stream should be', function (dataTable) {
  let expected = dataTable.rawTable.map((x)=>this.ansiMap[x]).join('');
  let received = this.convertToHex(this.displayOut());
  assert.equal(received, expected);
});

Then('there should be a force field at {int} , {int}', function (x, y) {
  let ff = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.forcefield==true);
  assert(ff);
});
  
Then('there should be a gate at {int} , {int}', function (x, y) {
  let gate = this.game.walls.filter((wall)=> wall.x==x && wall.y==y && wall.gate==true);
  assert(gate);
});

Then('there should be a wall at {int} , {int}', function (x, y) {
  let wall = this.game.walls.filter((wall)=> wall.x==x && wall.y==y);
  assert(wall);
});

Then('there should be a {int} point pill at {int} , {int}', function (points, x, y) {
  let pill = this.game.pills.filter((pill)=> pill.x==x && pill.y==y && pill.points==points);
  assert(pill);
});

Then('I should get the following response:', function (docString) {
  expect(this.getScores()).to.eventually.equal(docString);
  process.on("unhandledRejection", (error) => {
    console.error("Failed to get service Response", error.message);
  });
});
         


