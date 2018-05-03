package game

import "pacman/dir"

// Pill interface extends base Element with pill effects
type Pill interface {
	Element
}

type pillStruct struct {
	Element
}

const normalPill = 10
const powerPill = 50

var pillIcon = map[rune]int{
	'.': normalPill,
	'o': powerPill,
	'◉': powerPill,
}

// NewPill creates a new Pill game element
func NewPill(game Game, icon rune, loc dir.Location) Pill {
	colour := Colour{WHITE, BLACK}
	pill := NewElement(game, icon, loc, 0, PillScore(icon))
	if PillScore(icon) == 50 {
		colour = Colour{WHITE, BLINK}
	}
	pill.SetColour(colour)
	return &pillStruct{pill}
}

// AddToGame adds a new type of this element to the game
func (p *pillStruct) AddToGame(game Game) {
	pill := NewPill(game, p.Icon(), p.Location())
	game.AddPill(pill)
}

// TriggerEffect to model what happens when a Power pill is eaten
func (p *pillStruct) TriggerEffect(el Element) {
	theGame.SetScore(theGame.Score() + p.Score())
	if PillScore(p.Icon()) == powerPill {
		for _, ghost := range theGame.GetGhosts() {
			ghost.(Ghost).Panic()
		}
	}
	theGame.RemovePill(p)
}

// GetPill returns a new Pill if the icon is a pill
func GetPill(icon rune, location dir.Location) Element {
	if IsPill(icon) {
		return NewPill(nil, icon, location)
	}
	return nil
}

// IsPill returns true if this rune represents a pill of some kind
func IsPill(icon rune) bool {
	_, ok := pillIcon[icon]
	return ok
}

// PillScore for a given pill icon
func PillScore(icon rune) int {
	score, _ := pillIcon[icon]
	return score
}
