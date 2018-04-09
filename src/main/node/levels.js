"use strict";

module.exports =
class LevelMap {
  constructor (levels) {
    this.levels = levels.split("SEPARATOR\n");
    this.lastLevel = parseInt(levels,10);
    if (isNaN(this.lastLevel)) {
      this.lastLevel = 1;
    }
  }
  
  getLevel(level) {
    return this.levels[level];
  }
  
  max() {
    return this.lastLevel;
  }
}