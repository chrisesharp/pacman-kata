"use strict";

const Colour = require("./colour.js");
const GameElement = require("./game-elements.js");

const gateIcons = [
    {icon:"=",colour:Colour.WHITE},
    {icon:"━",colour:Colour.WHITE}
  ];
  
const forcefieldIcons = [
    {icon:"#",colour:Colour.BLACK},
    {icon:"┃",colour:Colour.BLACK}
  ];

const icons = [
    {icon:"+",colour:Colour.WHITE},
    {icon:"|",colour:Colour.WHITE},
    {icon:"-",colour:Colour.WHITE},
    {icon:"═",colour:Colour.WHITE},
    {icon:"╗",colour:Colour.WHITE},
    {icon:"╔",colour:Colour.WHITE},
    {icon:"║",colour:Colour.WHITE}, 
    {icon:"╚",colour:Colour.WHITE},
    {icon:"╝",colour:Colour.WHITE},
  ].concat(gateIcons)
   .concat(forcefieldIcons);
   
module.exports =
class Wall extends GameElement {
  constructor (options) {
    super(options);
    if (options) {
      this.gate = this.setGate(options.icon);
      this.forcefield = this.setForcefield(options.icon);
    }
  } 
  
  static isWall(token) {
    return (icons.filter((element) => element.icon === token).length>0);
  }
  
  getElement(coords, icon) {
    if (Wall.isWall(icon)) {
      return new Wall({coords, icon});
    }
  }
  
  addToGame(game) {
    game.addWall(this);
  }

  setGate(token) {
    return (gateIcons.filter((element) => element.icon === token).length>0);
  }

  setForcefield(token) {
    return (forcefieldIcons.filter((element) => element.icon === token).length>0);
  }
  
  colour() {
    return this.getColour(this.image);
  }
  
  isGate() {
    return this.gate;
  }
  
  isForceField() {
    return this.forcefield;
  }

}