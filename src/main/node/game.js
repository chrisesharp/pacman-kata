'use strict';

const Playfield = require('./playField.js');
const display = require('./display.js');
const ArrayList = require('arraylist');
const Pacman = require('./pacman.js');
const Ghost = require('./ghost.js');
const Wall = require('./walls.js');
const Pill = require('./pills.js');
const LevelMap = require('./levels.js');


module.exports = 

class Game {
    constructor (docString) {
      this.input = docString;
      this.lives = 3;
      this.score = 0;
      this.playfield = new Playfield(3,3);
      this.pacman = null; 
      this.walls = new ArrayList;
      this.pills = new ArrayList;
      this.ghosts = new ArrayList;
      this.usingPills = false;
      this.gameOver = false;
      this.level = 1;
      this.lastLevel = 1;
      this.levelMaps = null;
      this.animated = false;
      this.display = null;
    }
    
    setInput (docString) {
      this.input = docString;
    }
    
    setDisplay (display) {
      this.display = display;
    }
    
    getDisplay () {
      return this.display;
    }

    parse () {
      if (this.levelMaps !== null) {
        this.input = this.levelMaps.getLevel(this.level);
      } else {
        if (this.input.includes("SEPARATOR")) {
          this.levelMaps = new LevelMap(this.input);
          this.lastLevel = this.levelMaps.max();
          this.input = this.levelMaps.getLevel(this.level);
        }
      }
      this.columns = parseInt(this.input.indexOf("\n"),10);
      this.parseStatus(this.input.substring(0, this.columns));
      let screenRows = this.input.substring(this.columns).split("\n").filter(x => x);
      this.playfield = new Playfield(this.columns,screenRows.length);
      this.parseTokens(screenRows);
    }

    parseStatus (statusLine) {
      let elements = statusLine.split(" ").filter(x => x);
      let lives = parseInt(elements[0],10);
      this.lives =  isNaN(lives) ? this.lives : lives;
      let score = parseInt(elements[1],10);
      this.score =  isNaN(score) ? this.score : score;
    }
    
    parseTokens (screenRows) {
      let rows = this.playfield.height();
      let cols = this.playfield.width();
      for (var y=0;y<rows;y++) {
        for (var x=0;x<cols;x++) {
          let token = screenRows[y][x];
          if (Pacman.isPacman(token)) {
            let pacman = new Pacman({x,y},token);
            pacman.setPlayfield(this.playfield);
            pacman.setGame(this);
            if (this.animated) {
              pacman.useAnimation();
            }
            this.setPacman(pacman);
          }
          if (Wall.isWall(token)) {
            let wall = new Wall({x,y},token);
            this.addWall(wall);
          }
          if (Pill.isPill(token)) {
            let pill = new Pill({x,y},token);
            this.addPill(pill);
          }
          if (Ghost.isGhost(token)) {
            let ghost = new Ghost({x,y},token);
            ghost.setGame(this);
            this.addGhost(ghost);
          }
        }
      }
    }
    
    tick () {
      this.ghosts.forEach((ghost) => {ghost.tick()});
      if (this.pacman) {this.pacman.tick()}
      if (this.levelCleared()) {this.nextLevel()}
      this.updatePlayfield();
     }
    
    render() {
      let status = this.renderStatus();
      this.output = status.chars;
      this.colour = status.colour;
      let field = this.renderField();
      this.output += field.chars;
      this.colour = this.colour.concat(field.colour);
    }
    
    renderStatus() {
      let score = String(this.score);
      let lives = String(this.lives);
      let padding = this.playfield.width() - lives.length - score.length;
      let buffer = lives + " ".repeat(padding) + score + "\n";
      let colourbuf = Array(buffer.length).fill(0);
      return {chars:buffer,colour:colourbuf};
    }
    
    renderField() {
      let buffer = "";
      let colourbuf = [];
        for (let y=0;y<this.playfield.height();y++) {
          for (let x=0;x<this.playfield.width();x++) {
            let element = this.playfield.getLocation({x,y})
            buffer += element.icon();
            colourbuf.push(element.colour());
          }
          if (y < this.playfield.height()-1) {
            buffer += "\n";
            colourbuf.push(0);
          }
        }
      return {chars:buffer,colour:colourbuf};
    }
    
    refreshDisplay() {
      this.render();
      this.display.refresh(this.output, this.colour);
    }
    
    updatePlayfield() {
      this.playfield.reset();
      (this.walls.concat(this.pills)
                  .concat(this.ghosts)
                  .concat([this.pacman]))
                  .filter((x)=>x)
                  .forEach((e)=>{
                    this.playfield.setLocation(e.getLocation(),e)});
      if (this.gameOver) {this.printGameOver()}
    }
    
    levelCleared() {
      return (this.pills.isEmpty() && this.usingPills);
    }

    nextLevel() {
      this.level++;        
      this.pacman.restart();
      if (this.level > this.lastLevel) {
        this.gameOver = true;
      } else {
        this.pills.clear();
        this.walls.clear();
        this.ghosts.clear();
        this.parse();
      }

    }
    
    getPacman () {
      return this.pacman;
    }
    
    setPacman (pacman) {
      this.playfield.setLocation({x:pacman.x,y:pacman.y},pacman);
      this.pacman = pacman;
    }
    
    setPlayfield (playfield) {
      this.playfield = playfield
    }
    
    getPlayfield () {
      return this.playfield;
    }
    
    setScore (score) {
      this.score = parseInt(score,10);
    }
    
    addScore (score) {
      this.score += score;
    }
    
    setLives (lives) {
      this.lives = parseInt(lives,10);
    }
    
    getLives () {
      return this.lives;
    }
    
    loseLife() {
      this.lives -= 1;
      if (this.lives===0) {this.gameOver=true}
    }
    
    setLevel(level) {
      this.level = parseInt(level,10);
    }
    
    setLastLevel(last) {
      this.lastLevel = parseInt(last,10);
    }
    
    printGameOver() {
      let cols = this.playfield.width();
      let rows = this.playfield.height();
      const GAME = "GAME";
    	const OVER = "OVER";
      let y = Math.floor((rows / 2)) - 2;
    	let padding = Math.floor(((cols - 2) - GAME.length) / 2);
    	for (let i=0; i< GAME.length; i++) {
    		this.playfield.setLocation({x: padding + i + 1,y:y  },GAME[i])
        this.playfield.setLocation({x: padding + i + 1,y:y+1},OVER[i])
    	}
    }
    
    addWall (wall) {
      this.playfield.setLocation(wall.getLocation(),wall);
      this.walls.add(wall);
    }
    
    addGhost (ghost) {
      this.playfield.setLocation(ghost.getLocation(),ghost);
      this.ghosts.add(ghost);
    }
    
    addPill (pill) {
      this.playfield.setLocation(pill.getLocation(),pill);
      this.pills.add(pill);
      this.usingPills = true;
    }
    
    eatPill (pill) {
      let pillScore = pill.score();
      this.score += pillScore;
      this.pills.removeElement(pill);
      if (pillScore == 50) {
        this.ghosts.forEach((ghost)=>{ghost.panic()});
      }
    }
    
    useAnimation() {
      this.animated = true;
    }
    
    keyPressed (event) {
      this.pacman.direct(event);      
    }
    
    isGhost(loc) {
      return this.playfield.getLocation(loc) instanceof Ghost;
    }
    
    isWall(loc) {
      return this.playfield.getLocation(loc) instanceof Wall;
    }
    
    isGate(loc) {
      let element = this.playfield.getLocation(loc)
      return ( element instanceof Wall && element.isGate() );
    }
}

