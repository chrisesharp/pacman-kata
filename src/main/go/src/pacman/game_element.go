package main

type gameStruct struct {
	game      Game
	icon      rune
	colour    Colour
	location  Location
	direction Direction
	points    int
	start     Location
}

// NewElement gameStruct instance
func NewElement(game Game, icon rune, loc Location, dir Direction, points int) GameElement {
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
func (el *gameStruct) Location() Location {
	return el.location
}

// SetLocation of this element
func (el *gameStruct) SetLocation(loc Location) {
	el.location = loc
}

// Direction of this element
func (el *gameStruct) Direction() Direction {
	return el.direction
}

// SetDirection of this element
func (el *gameStruct) SetDirection(dir Direction) {
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
func (el *gameStruct) Tick() {}

// Score in points for this elements
func (el *gameStruct) Score() int {
	return el.points
}

// Restart the element in its initial placing
func (el *gameStruct) Restart() {
	el.SetLocation(el.start)
}

// TriggerEffect for this element colliding with the other
func (el *gameStruct) TriggerEffect(element GameElement) {}

// GetGame for accessing Game context
func (el *gameStruct) GetGame() Game {
	return el.game
}

// CollectElements from lists or lists into a flattened list
func CollectElements(lists ...[]GameElement) []GameElement {
	var elements []GameElement
	for _, slice := range lists {
		for _, piece := range slice {
			if piece != nil {
				elements = append(elements, piece)
			}
		}
	}
	return elements
}
