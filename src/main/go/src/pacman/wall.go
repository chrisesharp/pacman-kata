package main

var wallIcon = map[rune]int{
	'╔': 0,
	'║': 0,
	'╚': 0,
	'╗': 0,
	'╝': 0,
	'═': 0,
	'+': 0,
	'|': 0,
	'-': 0,
	'#': 1,
	'┃': 1,
	'=': 2,
	'━': 2,
}

// NewWall creates a new Pill game element
func NewWall(game Game, icon rune, loc Location) GameElement {
	colour := Colour{WHITE, BLACK}
	wall := NewElement(game, icon, loc, 0, 0)
	if IsForceField(icon) {
		colour = Colour{BLACK, BLACK}
	}
	wall.SetColour(colour)
	return wall
}

// IsWall if this rune represents some kind of wall
func IsWall(icon rune) bool {
	_, ok := wallIcon[icon]
	return ok
}

// IsForceField if this rune represents a force field
func IsForceField(icon rune) bool {
	force, _ := wallIcon[icon]
	return (force == 1)
}

// IsGate if this rune represents a force field
func IsGate(icon rune) bool {
	gate, _ := wallIcon[icon]
	return (gate == 2)
}
