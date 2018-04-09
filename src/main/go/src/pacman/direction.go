package main

//Direction enumeration type
type Direction int

//Direction enumeration values
const (
	LEFT Direction = iota
	UP
	RIGHT
	DOWN
)

// Location tuple
type Location struct {
	x, y int
}

// Next location by direction
func (our Location) Next(dir Direction) Location {
	dx, dy := dir.Move()
	our.x += dx
	our.y += dy
	return our
}

// Avoid direction to a given location
func (our Location) Avoid(their Location) Direction {
	if our.x < their.x {
		return LEFT
	} else if our.x > their.x {
		return RIGHT
	} else if our.y < their.y {
		return UP
	}
	return DOWN

}

// Opposite direction to this
func (d Direction) Opposite() Direction {
	return (d + 2) % 4
}

// Left direction to this
func (d Direction) Left() Direction {
	return (d + 3) % 4

}

// Right direction to this
func (d Direction) Right() Direction {
	return (d + 1) % 4
}

// Equals checks with the string is semantically equivalent to the Direction
func (d Direction) Equals(direction string) bool {
	if direction == "LEFT" && d == LEFT {
		return true
	} else if direction == "RIGHT" && d == RIGHT {
		return true
	} else if direction == "UP" && d == UP {
		return true
	} else if direction == "DOWN" && d == DOWN {
		return true
	}
	return false
}

// Move returns the location deltas for a particular direction
func (d Direction) Move() (int, int) {
	dx := 0
	dy := 0
	switch d {
	case LEFT:
		dx = -1
	case RIGHT:
		dx = 1
	case UP:
		dy = -1
	case DOWN:
		dy = 1
	}
	return dx, dy
}

// Wrap a location toroidallly on a width/height field
func (our *Location) Wrap(width, height int) {
	our.x = (our.x + width) % width
	our.y = (our.y + height) % height
}
