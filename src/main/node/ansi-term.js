"use strict";

module.exports = Object.freeze({
   ESC    : "\u001B[",
   CLR    : "\u001B[H" + "\u001B[2J" + "\u001B[1m",
   RST    : "\u001B[0m",
   REVON  : "\u001B[?5h",
   REVOFF : "\u001B[?5l",
   BLINK  : "\u001B[5m",
   REV    : "\u001B[7m" 
});
