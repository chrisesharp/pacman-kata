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
  constructor ({x,y},icon) {
    super ({x,y}, icon);
    this.gate = this.setGate(icon);
    this.forcefield = this.setForcefield(icon);
  } 
  
  static isWall(token) {
    return (icons.filter((element) => element.icon === token).length>0);
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
  
  getColour(icon) {
    let token= icons.find((a) => { return a.icon.indexOf(icon)>=0;});
    if (token) {
      return token.colour;
    }
  }
}