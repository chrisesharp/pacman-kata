package main

import (
	"math/rand"
)

// Ghost interface extending GameElement
type Ghost interface {
	GameElement
	Panic()
	IsPanicked() bool
}

const normalGhost rune = 'M'
const panicGhost rune = 'W'

var panicColour = Colour{BLUE, BLACK}

var ghostIcon = map[rune]bool{
	normalGhost: false,
	panicGhost:  true,
}

const panicLevel int = 50
const ghostPoints int = 200

type ghostStruct struct {
	GameElement
	panic      int
	origColour Colour
	gatePassed bool
	game       Game
}

var ghostColours = []ColourAtt{RED, YELLOW, GREEN, PURPLE}
var ghostColour = 0

// NewGhost creates a clean populated ghostStruct
func NewGhost(game Game, icon rune, loc Location) Ghost {
	colour := Colour{ghostColours[ghostColour], BLACK}
	ghostColour = (ghostColour + 1) % len(ghostColours)
	var panic int
	element := NewElement(game, icon, loc, LEFT, ghostPoints)
	if GhostPanic(icon) {
		panic = panicLevel
		element.SetColour(panicColour)
	} else {
		element.SetColour(colour)
	}

	return &ghostStruct{GameElement: element,
		panic:      panic,
		origColour: colour,
		gatePassed: false,
		game:       game}
}

// Tick activates this elements turn
func (g *ghostStruct) Tick() {
	var options []Direction
	pacmanLoc := g.Location()
	options = nil
	pacman := g.game.GetPacman()
	if pacman != nil {
		pacmanLoc = pacman.Location()
	}
	if g.panic > 0 {
		g.SetColour(panicColour)
		g.SetDirection(g.Location().Avoid(pacmanLoc))
		g.panic--
	} else {
		g.SetColour(g.origColour)
	}

	ahead := g.Direction()
	left := ahead.Left()
	right := ahead.Right()
	walls := g.game.GetWalls()

	for _, nextDir := range []Direction{ahead, left, right} {
		if g.isClear(g.Location().Next(nextDir), walls) {
			options = append(options, nextDir)
		}
	}
	if options != nil {
		index := rand.Intn(len(options))
		g.SetDirection(options[index])
		if g.panic%2 == 0 {
			g.SetLocation(g.Location().Next(g.Direction()))
		}
	} else {
		if g.panic == 0 {
			g.SetDirection(g.Direction().Opposite())
			if g.isClear(g.Location().Next(g.Direction()), walls) {
				g.SetLocation(g.Location().Next(g.Direction()))
			}
		}
	}

	gate := g.game.GetGate()
	if gate != nil {
		if g.Location() == gate.Location() {
			g.gatePassed = true
		}
	}
	if pacman != nil {
		if g.Location() == pacman.Location() {
			g.TriggerEffect(pacman)
		}
	}
	g.SetIcon(GhostIcon(g.panic > 0))

}

// Panic the ghost
func (g *ghostStruct) Panic() {
	g.panic = panicLevel
	g.SetIcon(GhostIcon(g.panic > 0))
	g.SetColour(panicColour)
}

// IsPanicked returns true is the ghost panic level is non-zero
func (g *ghostStruct) IsPanicked() bool {
	return g.panic > 0
}

// TriggerEffect of colliding with this element
func (g *ghostStruct) TriggerEffect(pacman GameElement) {
	if g.panic > 0 {
		g.game.SetScore(g.game.Score() + ghostPoints)
		g.panic = 0
		g.gatePassed = false
		g.Restart()
	} else {
		pacman.TriggerEffect(g)
	}
}

func (g *ghostStruct) isClear(nextLoc Location, walls []GameElement) bool {
	clear := true
	for _, wall := range walls {
		if wall.Location() == nextLoc {
			if !wall.IsGate() {
				clear = false
			} else {
				if g.gatePassed {
					clear = false
				}
			}
		}
	}
	return clear
}

// IsGhost returns true is the icon represents one
func IsGhost(icon rune) bool {
	_, ok := ghostIcon[icon]
	return ok
}

// GhostPanic returns true is the icon represents a panicked ghost
func GhostPanic(icon rune) bool {
	panic, _ := ghostIcon[icon]
	return panic
}

// GhostIcon returns a suitable icon for a panicked ghost
func GhostIcon(panic bool) rune {
	if panic {
		return panicGhost
	}
	return normalGhost
}
