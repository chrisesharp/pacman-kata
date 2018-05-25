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
	behaviour  behaviour
}

var ghostColours = []ColourAtt{RED, CYAN, GREEN, PURPLE}
var ghostColour = 0

type behaviour interface {
	tick()
	noChoiceDirection() dir.Direction
	shouldMove() bool
	triggerEffect(Element)
}

type panicBehaviour struct {
	ghost     *ghostStruct
	turnsLeft int
}

func (p *panicBehaviour) tick() {
	if p.turnsLeft == 0 {
		p.ghost.behaviour = &calmBehaviour{p.ghost}
		return
	}
	pacman := p.ghost.game.GetPacman()
	if pacman != nil {
		p.ghost.SetDirection(p.ghost.Location().Avoid(pacman.Location()))
	}
	p.ghost.SetColour(panicColour)
	p.turnsLeft--
}

func (p *panicBehaviour) noChoiceDirection() dir.Direction {
	return p.ghost.Direction()
}

func (p *panicBehaviour) shouldMove() bool {
	return p.turnsLeft%2 == 0
}

func (p *panicBehaviour) triggerEffect(pacman Element) {
	p.ghost.game.SetScore(p.ghost.game.Score() + ghostPoints)
	p.ghost.behaviour = &calmBehaviour{p.ghost}
	p.ghost.gatePassed = false
	p.ghost.Restart()
}

type calmBehaviour struct {
	ghost *ghostStruct
}

func (c *calmBehaviour) tick() {
	c.ghost.SetColour(c.ghost.origColour)
}

func (c *calmBehaviour) shouldMove() bool {
	return true
}

func (c *calmBehaviour) noChoiceDirection() dir.Direction {
	return c.ghost.Direction().Opposite()
}

func (c *calmBehaviour) triggerEffect(pacman Element) {
	pacman.TriggerEffect(c.ghost)
}

// NewGhost creates a clean populated ghostStruct
func NewGhost(game Game, icon rune, loc dir.Location) Ghost {
	var colour Colour
	if game != nil {
		colour = Colour{ghostColours[ghostColour], BLACK}
		ghostColour = (ghostColour + 1) % len(ghostColours)
	}
	var startingBehaviour behaviour
	element := NewElement(game, icon, loc, dir.LEFT, ghostPoints)
	ghost := &ghostStruct{Element: element,
		origColour: colour,
		gatePassed: false,
		game:       game}
	if GhostPanic(icon) {
		startingBehaviour = &panicBehaviour{
			turnsLeft: panicLevel,
			ghost:     ghost,
		}
		element.SetColour(panicColour)
	} else {
		startingBehaviour = &calmBehaviour{
			ghost: ghost,
		}
		element.SetColour(colour)
	}
	ghost.behaviour = startingBehaviour
	return ghost
}

// Tick activates this elements turn
func (g *ghostStruct) Tick() {
	g.behaviour.tick()
	g.chooseDirection()
	g.checkCollisions()
	g.SetIcon(GhostIcon(g.IsPanicked()))
}

func (g *ghostStruct) chooseDirection() {
	ahead := g.Direction()
	choices := []dir.Direction{ahead, ahead.Left(), ahead.Right()}
	options := g.findOptions(choices)
	if options != nil {
		g.SetDirection(randomChoice(options))
	} else {
		g.SetDirection(g.behaviour.noChoiceDirection())
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

func (g *ghostStruct) move() {
	if g.behaviour.shouldMove() && g.isClear(g.Location().Next(g.Direction())) {
		g.SetLocation(g.Location().Next(g.Direction()))
	}
}

func (g *ghostStruct) checkCollisions() {
	gate := g.game.GetGate()
	if gate != nil && g.Location() == gate.Location() {
		g.gatePassed = true
	}
	pacman := g.game.GetPacman()
	if pacman != nil && g.Location() == pacman.Location() {
		g.TriggerEffect(pacman)
	}
}

// Panic the ghost
func (g *ghostStruct) Panic() {
	g.behaviour = &panicBehaviour{
		turnsLeft: panicLevel,
		ghost:     g,
	}
	g.SetIcon(GhostIcon(true))
	g.SetColour(panicColour)
}

// IsPanicked returns true is the ghost panic level is non-zero
func (g *ghostStruct) IsPanicked() bool {
	_, panicking := g.behaviour.(*panicBehaviour)
	return panicking
}

// TriggerEffect of colliding with this element
func (g *ghostStruct) TriggerEffect(pacman Element) {
	g.behaviour.triggerEffect(pacman)
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
