package game

import (
	"pacman/dir"
)

type gameStruct struct {
	game      Game
	icon      rune
	colour    Colour
	location  dir.Location
	direction dir.Direction
	points    int
	start     dir.Location
}

// NewElement gameStruct instance
func NewElement(game Game, icon rune, loc dir.Location, dir dir.Direction, points int) Element {
	return &gameStruct{game: game,
		icon:      icon,
		colour:    DefaultColour,
		location:  loc,
		direction: dir,
		points:    points,
		start:     loc}
}

// AddToGame adds a new type of this element to the game
func (el *gameStruct) AddToGame(game Game) {
	el.game = game
}

// IsForceField checks if a wall is a force field
func (el *gameStruct) IsForceField() bool {
	return IsForceField(el.icon)
}

// IsGate checks if a wall is a gate
func (el *gameStruct) IsGate() bool {
	return IsGate(el.icon)
}

// Location of this element
func (el *gameStruct) Location() dir.Location {
	return el.location
}

// SetLocation of this element
func (el *gameStruct) SetLocation(loc dir.Location) {
	loc.Wrap(el.game.Dimensions())
	el.location = loc
}

// Direction of this element
func (el *gameStruct) Direction() dir.Direction {
	return el.direction
}

// SetDirection of this element
func (el *gameStruct) SetDirection(dir dir.Direction) {
	el.direction = dir
}

// Icon for this element
func (el *gameStruct) Icon() rune {
	return el.icon
}

// Colour for this element
func (el *gameStruct) Colour() Colour {
	return el.colour
}

// SetIcon for this element
func (el *gameStruct) SetIcon(icon rune) {
	el.icon = icon
}

// SetColour for this element
func (el *gameStruct) SetColour(colour Colour) {
	el.colour = colour
}

// Tick activates this elements turn
func (el *gameStruct) Tick() {
	// Base elements have no behaviour
}

// Score in points for this elements
func (el *gameStruct) Score() int {
	return el.points
}

// Restart the element in its initial placing
func (el *gameStruct) Restart() {
	el.SetLocation(el.start)
}

// TriggerEffect for this element colliding with the other
func (el *gameStruct) TriggerEffect(element Element) {
	// Base elements have no effect on other elements
}

// GetGame for accessing Game context
func (el *gameStruct) GetGame() Game {
	return el.game
}

// CollectElements from lists or lists into a flattened list
func CollectElements(lists ...[]Element) []Element {
	var elements []Element
	for _, slice := range lists {
		for _, piece := range slice {
			if piece != nil {
				elements = append(elements, piece)
			}
		}
	}
	return elements
}
