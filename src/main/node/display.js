'use strict'
const { Writable } = require('stream'); 
const { ESC, CLR, RST, REVON, REVOFF, BLINK } = require('./ansi-term.js');
module.exports =

class Display {
  constructor(game) {
    this.game = game;
    this.output = null;
    this.cols = null;
    this.rows = null;
  }
  
  init(stream) {
    this.output = stream;
    if (this.game) {
      this.cols = this.game.getPlayfield().width();
      this.rows = this.game.getPlayfield().height();
    }
  }

  width() {
    return this.cols;
  }
  
  height() {
    return this.rows;
  }
  
  refresh(output) {
    this.write(CLR);
    this.write(output);
    this.write(RST);
    this.write("\n");
  }
  
  flash() {
    this.write(REVON);
    this.cycleBurn(100);
    this.write(REVOFF);
  }
  
  cycleBurn(ms){
    var start = new Date().getTime();
    var end = start;
    while(end < start + ms) {
      end = new Date().getTime();
    }
  }
  
  write(string) {
    if (this.output instanceof Writable) {
      this.output.write(string);
    } else {
      this.output.push(string);
    }
  }
}