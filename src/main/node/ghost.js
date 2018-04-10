"use strict";

const d = require("./direction.js");
const nextLocation = d.nextLocation;
const turnLeft = d.turnLeft;
const turnRight = d.turnRight;
const opposite = d.opposite;
const avoid = d.avoid;
const RIGHT = d.Direction.RIGHT;
const LEFT = d.Direction.LEFT;
const UP = d.Direction.UP;
const DOWN = d.Direction.DOWN;
const Colour = require("./colour.js");
const GameElement = require("./game-elements.js");

const icons = [
    {icon:"M",panicked:false,colour:null},
    {icon:"W",panicked:true,colour:Colour.BLUE}
  ];

const ghostColours = [Colour.RED,
                      Colour.GREEN,
                      Colour.PURPLE,
                      Colour.CYAN];
var ghostColour = 0;

function setGhostColour() {
  return ghostColours[ghostColour++];
}

module.exports =

class Ghost extends GameElement {
  constructor (options) {
    super(options);
    if (options) {
      this.start = options.coords;
      this.points = 200;
      this.direction = UP;
      this.game = null;
      this.gatePassed = false;
      this.uniqueColour = setGhostColour();
      
      this.panicLevel = (icons.find( (ghost) => {
        return ghost.icon === options.icon;
      }).panicked) ? 50 : 0;
    }
  } 
  
  static isGhost(token) {
    return (icons.filter((element) => element.icon === token).length>0);
  }
  
  getElement(coords, icon) {
    if (Ghost.isGhost(icon)) {
      return new Ghost({coords, icon});
    }
  }
  
  addToGame(game) {
    game.addGhost(this);
  }
  
  colour() {
    return this.getColour(this.panicLevel>0);
  }
  
  getColour(panicked) {
    let colour= icons.find((a) => { return a.panicked === panicked;}).colour;
    if (colour) {
      return colour;
    } else {
      return this.uniqueColour;
    }
  }
  
  tick() {
    let pacman=this.game.getPacman();
    let pacLoc= (pacman) ? pacman.getLocation() : this.getLocation();
    if (this.isPanicked()) {
      this.direction = avoid(this.getLocation(),pacLoc);
      this.panicLevel--;      
    } 
    let choices = [this.direction, turnLeft(this.direction),turnRight(this.direction)];
    let options = [];
    choices.forEach( (direction) => {
      let next = nextLocation(this.getLocation(), direction);
      if (this.isClear(next)) {
        options.push(direction);
      } 
    });
    if (options.length > 0) {
      let i = Math.floor((Math.random() * options.length));
      this.direction = options[i];
      if ((this.panicLevel % 2) === 0) {
        let next = nextLocation(this.getLocation(),this.direction);
        this.setLocation(next);
      }
    } else {
      if (!this.isPanicked()) {
        this.direction = opposite(this.direction);
        let next = nextLocation(this.getLocation(),this.direction);
        if (this.isClear(next))  {
          this.setLocation(next);
        }
      }
    }

    if (this.isOnPacman()) {
      this.collision(this.game.playfield.getLocation(this.getLocation()));
    }
    if (this.game.isGate(this.getLocation())) {
      this.gatePassed = true;
    }
    this.image = (icons.find( (ghost) => {
      return ghost.panicked === (this.panicLevel > 0);
    }).icon);
  }
  
  isClear(loc) {
    let element = this.game.playfield.getLocation(loc);
    if (!(this.game.isWall(loc))) {
      return true;
    } else if (element.isGate() && !this.gatePassed) {
      return true;
    } 
    return false;
  }
  
  restart() {
    this.setLocation(this.start);
    this.panicLevel=0;
    this.gatePassed = false;
  }
  
  collision(pacman) {
    if (this.isPanicked()) {
      this.game.addScore(this.score());
      this.restart();
    } else {
      pacman.kill();
    }
  }
  
  isOnPacman() {
    if (this.game.getPacman()) {
      let pacLoc = this.game.getPacman().getLocation();
      let ourLoc = this.getLocation();
      return (pacLoc.x === ourLoc.x && pacLoc.y === ourLoc.y);
    } else {
      return false;
    }
  }
  
  score() {
    return this.points;
  }
  
  panic() {
    this.panicLevel = 50;
    this.image = (icons.find( (ghost) => {
      return ghost.panicked === (this.panicLevel > 0);
    }).icon);
  }
  
  isPanicked() {
    return (this.panicLevel > 0);
  }
  
  setGame(game) {
    this.game = game;
  }
}