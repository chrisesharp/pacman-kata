package dir

//Direction enumeration type
type Direction int

//Direction enumeration values
//go:generate stringer -type=Direction
const (
	LEFT Direction = iota
	UP
	RIGHT
	DOWN
)

//Deltas for coordinates by Direction
var Deltas = map[Direction]Delta{
	LEFT:  {dx: -1, dy: 0},
	UP:    {dx: 0, dy: -1},
	RIGHT: {dx: 1, dy: 0},
	DOWN:  {dx: 0, dy: 1},
}

// Location tuple
type Location struct {
	X, Y int
}

// Delta tuple
type Delta struct {
	dx, dy int
}

// Next location by direction
func (our Location) Next(dir Direction) Location {
	delta := Deltas[dir]
	our.X += delta.dx
	our.Y += delta.dy
	return our
}

// Avoid direction to a given location
func (our Location) Avoid(their Location) Direction {
	if our.X < their.X {
		return LEFT
	} else if our.X > their.X {
		return RIGHT
	} else if our.Y < their.Y {
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
	return direction == d.String()
}

// Wrap a location toroidallly on a width/height field
func (our *Location) Wrap(width, height int) {
	our.X = (our.X + width) % width
	our.Y = (our.Y + height) % height
}
