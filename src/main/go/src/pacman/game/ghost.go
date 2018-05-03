package game

import (
	"math/rand"
	"pacman/dir"
)

// Ghost interface extending Element
type Ghost interface {
	Element
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
	Element
	panic      int
	origColour Colour
	gatePassed bool
	game       Game
}

var ghostColours = []ColourAtt{RED, CYAN, GREEN, PURPLE}
var ghostColour = 0

// NewGhost creates a clean populated ghostStruct
func NewGhost(game Game, icon rune, loc dir.Location) Ghost {
	var colour Colour
	if game != nil {
		colour = Colour{ghostColours[ghostColour], BLACK}
		ghostColour = (ghostColour + 1) % len(ghostColours)
	}
	var panic int
	element := NewElement(game, icon, loc, dir.LEFT, ghostPoints)
	if GhostPanic(icon) {
		panic = panicLevel
		element.SetColour(panicColour)
	} else {
		element.SetColour(colour)
	}

	return &ghostStruct{Element: element,
		panic:      panic,
		origColour: colour,
		gatePassed: false,
		game:       game}
}

// Tick activates this elements turn
func (g *ghostStruct) Tick() {
	g.managePanic()
	g.chooseDirection()
	g.checkCollisions()
	g.SetIcon(GhostIcon(g.IsPanicked()))
}

func (g *ghostStruct) managePanic() {
	if g.panic > 0 {
		pacman := g.game.GetPacman()
		if pacman != nil {
			g.SetDirection(g.Location().Avoid(pacman.Location()))
		}
		g.SetColour(panicColour)
		g.panic--
	} else {
		g.SetColour(g.origColour)
	}
}

func (g *ghostStruct) chooseDirection() {
	ahead := g.Direction()
	choices := []dir.Direction{ahead, ahead.Left(), ahead.Right()}
	options := g.findOptions(choices)
	if options != nil {
		g.SetDirection(randomChoice(options))
	} else {
		g.SetDirection(g.noChoice())
	}
	g.move()
}

func (g *ghostStruct) findOptions(choices []dir.Direction) []dir.Direction {
	var options []dir.Direction
	for _, nextDir := range choices {
		if g.isClear(g.Location().Next(nextDir)) {
			options = append(options, nextDir)
		}
	}
	return options
}

func randomChoice(options []dir.Direction) dir.Direction {
	index := rand.Intn(len(options))
	return options[index]
}

func (g *ghostStruct) noChoice() dir.Direction {
	direction := g.Direction()
	if g.panic == 0 {
		direction = direction.Opposite()
	}
	return direction
}

func (g *ghostStruct) move() {
	if g.panic%2 == 0 {
		if g.isClear(g.Location().Next(g.Direction())) {
			g.SetLocation(g.Location().Next(g.Direction()))
		}
	}
}

func (g *ghostStruct) checkCollisions() {
	gate := g.game.GetGate()
	if gate != nil {
		if g.Location() == gate.Location() {
			g.gatePassed = true
		}
	}
	pacman := g.game.GetPacman()
	if pacman != nil {
		if g.Location() == pacman.Location() {
			g.TriggerEffect(pacman)
		}
	}
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
func (g *ghostStruct) TriggerEffect(pacman Element) {
	if g.panic > 0 {
		g.game.SetScore(g.game.Score() + ghostPoints)
		g.panic = 0
		g.gatePassed = false
		g.Restart()
	} else {
		pacman.TriggerEffect(g)
	}
}

func (g *ghostStruct) isClear(nextLoc dir.Location) bool {
	clear := true
	walls := g.game.GetWalls()
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

// GetGhost returns a new Ghost if the icon is a ghost
func GetGhost(icon rune, location dir.Location) Element {
	if IsGhost(icon) {
		return NewGhost(nil, icon, location)
	}
	return nil
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

// AddToGame adds a new type of this element to the game
func (g *ghostStruct) AddToGame(game Game) {
	ghost := NewGhost(game, g.Icon(), g.Location())
	game.AddGhost(ghost)
}
