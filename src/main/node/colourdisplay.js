'use strict'

const Colour = require('./colour.js');
const Display = require('./display.js');
const { ESC, CLR, RST, REVON, REVOFF, BLINK } = require('./ansi-term.js');

const DEFAULT = ESC+Colour.WHITE+'m'+ESC+Colour.BLACK_BG+'m';

module.exports =

class ColourDisplay extends Display {
  constructor(game) {
    super(game);
  }
  
  refresh(output, colour) {
    this.write(CLR);
    this.write(DEFAULT);
    for (let i=0;i<output.length;i++) {
      if (colour[i] >0) { 
        this.write(ESC+parseInt(colour[i])+'m');
      }
      this.write(output[i]);
      this.write(RST);
      this.write(DEFAULT);  
    }
    this.write(RST);
    this.write("\n");
  }
}