from enum import IntEnum


class Direction(IntEnum):
    LEFT = 0
    UP = 1
    RIGHT = 2
    DOWN = 3


def next_location(coords, direction):
    return {
        Direction.LEFT: lambda coords: ((coords[0] - 1),
                                        coords[1]),
        Direction.RIGHT: lambda coords: ((coords[0] + 1),
                                         coords[1]),
        Direction.UP: lambda coords: (coords[0],
                                      (coords[1] - 1)),
        Direction.DOWN: lambda coords: (coords[0],
                                        (coords[1] + 1))
        }[direction](coords)


def avoid(our, their):
    if our[0] < their[0]:
        return Direction.LEFT
    elif our[0] > their[0]:
        return Direction.RIGHT
    elif our[1] < their[1]:
        return Direction.DOWN
    else:
        return Direction.UP


def left(direction):
    return ((direction + 3) % 4)


def right(direction):
    return ((direction + 1) % 4)


def opposite(direction):
    return ((direction + 2) % 4)


def wrap(coords, width, height):
    return ((coords[0] + width) % width, (coords[1] + height) % height)
