"use strict";

const nextLocation = require("./direction.js").nextLocation;
const RIGHT = require("./direction.js").Direction.RIGHT;
const LEFT = require("./direction.js").Direction.LEFT;
const UP = require("./direction.js").Direction.UP;
const DOWN = require("./direction.js").Direction.DOWN;
const Colour = require("./colour.js");
const GameElement = require("./game-elements.js");
const Pill = require("./pills.js");
const Wall = require("./walls.js");

const icons = [
  {icon:["*","*"],direction:-1,alive:false,colour:Colour.YELLOW},
  {icon:[">","}"],direction:LEFT,alive:true,colour:Colour.YELLOW},
  {icon:["V","v"],direction:UP,alive:true,colour:Colour.YELLOW},
  {icon:["<","{"],direction:RIGHT,alive:true,colour:Colour.YELLOW},
  {icon:["Λ","^"],direction:DOWN,alive:true,colour:Colour.YELLOW}
];

module.exports =
class Pacman  extends GameElement {
  constructor (options) {
    super(options);
    if (options) {
      this.start = options.coords;
      this.game = null;
      this.playfield = null;
      this.facing = this.getDirectionByIcon(options.icon);
      this.alive = this.getLivenessByIcon(options.icon);
      this.frame = 0;    
    }
  } 
  
  static isPacman(token) {
    return (icons.filter((element) => element.icon.indexOf(token)>=0).length>0);
  }
  
  getElement(coords, icon) {
    if (Pacman.isPacman(icon)) {
      return new Pacman({coords, icon});
    }
  }
  
  addToGame(game) {
    game.addPacman(this);
  }
  
  setGame(game) {
    this.game = game;
  }
  
  setPlayfield(playfield) {
    this.playfield = playfield;
  }
  
  setDirection(facing) {
    this.facing = facing;
    let tokens = icons.find((a) => {return a.direction === facing;});
    this.image = tokens[this.frame];
  }
  
  getDirectionByIcon(icon) {
    let token = icons.find((a) => { return a.icon.indexOf(icon)>=0;});
    if (token) {
      return token.direction;
    }
  }
  
  getLivenessByIcon(icon) {
    let token = icons.find((a) => { return a.icon[0] === icon;});
    if (token) {
      return token.alive;
    }
  }
  
  colour() {
    return this.getColour(this.alive);
  }
  
  getColour(alive) {
    return icons.find((a) => { return a.alive === alive;}).colour;
  }
  
  getIconByLiveness(alive) {
    let token= icons.find((a) => { return a.alive === alive;});
    if (token) {
      return token.icon[this.frame];
    }
  }
  
  setLocation(loc) {
    loc = this.wrapLocation(loc);
    this.x = loc.x;
    this.y = loc.y;
  }
  
  icon() {
    if (this.alive) {
      return icons.find((a) => { return a.direction === this.direction();}).icon[this.frame];
    }
    return this.getIconByLiveness(this.alive);
  }
  
  animate() {
    this.frame = (this.frame+1) % 2;  
  }
  
  useAnimation() {
    this.animated = true;
  }
  
  direction () {
    return this.facing;
  }
  
  direct (direction) {
    let next = nextLocation(this.getLocation(),direction);
    if (this.isClear(next)) {
      this.setDirection(direction);
    }
  }
  
  tick() {
    let next = nextLocation(this.getLocation(),this.direction());
    if (this.isClear(next))  {
      this.setLocation(next);
      if (this.animated) {
        this.animate();
      }
    }
    if (this.isOnPill()) {
      this.game.eatPill(this.playfield.getLocation(this.getLocation()));
    }
    if (this.game.isGhost(this.getLocation())) {
      this.playfield.getLocation(this.getLocation()).collision(this);
    }
  }
  
  kill() {
    this.alive = false;
    this.image = this.getIconByLiveness(false);
    this.game.loseLife();
  }
  
  restart() {
    this.setLocation(this.start);
    this.alive=true;
  }
  
  wrapLocation(loc) {
    let width = this.playfield.width();
    let height = this.playfield.height();
    return {x:(loc.x + width) % width,y:(loc.y + height) % height};
  }
  
  isClear(loc) {
    loc = this.wrapLocation(loc);
    let element = this.playfield.getLocation(loc);
    if (!(this.game.isWall(loc))) {
      return true;
    } else if (element.isForceField()) {
      return true;
    } else {
      return false;
    }
  }
  
  isOnPill() {
    return (this.playfield.getLocation(this.getLocation()) instanceof Pill);
  }
  
  isAlive() {
    return this.alive;
  }
}

