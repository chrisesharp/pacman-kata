"use strict";

const Direction = Object.freeze({LEFT:0,UP:1,RIGHT:2,DOWN:3});
const Deltas = Object.freeze({
  0:  {dx:-1,dy:0},
  1:  {dx:0, dy:-1},
  2:  {dx:1, dy:0},
  3:  {dx:0, dy:1}
});
const positions = Object.keys(Direction).length;


function getDelta(direction) {
  if (typeof direction == "number") {
    return Deltas[direction]
  } else if (typeof direction === "string") {
    return Deltas[Direction[direction]];
  } 
  return null;
}

function facing(facing) {
  return Direction[facing];
}

function nextLocation(loc, direction) {
  let delta = getDelta(direction);
  return {x: (loc.x + delta.dx), y: (loc.y + delta.dy) };
}

function turnLeft(facing) {
  return (facing + 3) % positions;
}

function turnRight(facing) {
  return (facing +1) % positions;
}

function opposite(facing) {
  return (facing +2) % positions;
}

function avoid(ours, theirs) {
  if (ours.x < theirs.x) {
    return Direction.LEFT;
  } else if (ours.x > theirs.x) {
    return Direction.RIGHT;
  }
  if (ours.y < theirs.y) {
    return Direction.UP;
  } else {
    return Direction.DOWN;
  }
}

module.exports = {
  Direction: Direction,
  facing: facing,
  nextLocation: nextLocation,
  turnLeft: turnLeft,
  turnRight: turnRight,
  opposite: opposite,
  avoid: avoid
}
