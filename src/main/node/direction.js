"use strict";

const Direction = Object.freeze({"LEFT":0,"UP":1,"RIGHT":2,"DOWN":3});
const positions = Object.keys(Direction).length;

function facing(facing) {
  switch (facing) {
    case "LEFT":
      return Direction.LEFT;
    case "RIGHT":
      return Direction.RIGHT;
    case "UP":
      return Direction.UP;
    case "DOWN":
      return Direction.DOWN;
    default:
      return -1;
  }
}

function nextLocation(loc, direction) {
  switch (direction) {
    case Direction.LEFT:
      return {x: loc.x-1, y:loc.y};
    case Direction.RIGHT:
      return {x: loc.x+1, y:loc.y};
    case Direction.UP:
      return {x: loc.x, y:loc.y-1};
    case Direction.DOWN:
      return {x: loc.x, y:loc.y+1};
  }
  return null;
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
