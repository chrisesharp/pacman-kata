package game

import "pacman/dir"

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

// Wall interface extends Element
type Wall interface {
	Element
}

type wallStruct struct {
	Element
}

// NewWall returns a new Wall struct
func NewWall(game Game, icon rune, loc dir.Location) Wall {
	colour := Colour{WHITE, BLACK}
	wall := NewElement(game, icon, loc, 0, 0)
	if IsForceField(icon) {
		colour = Colour{BLACK, BLACK}
	}
	wall.SetColour(colour)
	return &wallStruct{wall}
}

// GetWall returns a new Wall if the icon is a wall
func GetWall(icon rune, location dir.Location) Element {
	if IsWall(icon) {
		return NewWall(nil, icon, location)
	}
	return nil
}

// IsWall if this rune represents some kind of wall
func IsWall(icon rune) bool {
	_, ok := wallIcon[icon]
	return ok
}

// AddToGame adds a new type of this element to the game
func (w *wallStruct) AddToGame(game Game) {
	wall := NewWall(game, w.Icon(), w.Location())
	game.AddWall(wall)
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
