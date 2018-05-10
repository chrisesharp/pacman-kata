package game

import (
	"os"
	"pacman/dir"
)

// Game state interface
type Game interface {
	New(bool) Game
	Parse()
	Render()
	SetDisplay(Display)
	SetController(Controller)
	SetInput(string)
	SetOutput(outstream *os.File)
	SetPlayfield(Playfield)
	SetLevelMaps(*levelStruct)
	Lives() int
	SetLives(lives int)
	Score() int
	SetScore(score int)
	SetLevel(max int)
	SetMaxLevel(max int)
	Dimensions() (int, int)
	GetPacman() Element
	SetPacman(Element)
	AddGhost(ghost Element)
	GetGhosts() []Element
	GetPills() []Element
	AddPill(pill Element)
	RemovePill(pill Element)
	GetWalls() []Element
	AddWall(Element)
	GetGate() Element
	GetOutput() (string, []Colour)
	Tick()
	Play(debug bool)
	KeyPress(key string)
	UseAnimation()
	Animated() bool
	Quit()
	IsDebug() bool
}

// Element interface
type Element interface {
	AddToGame(game Game)
	IsForceField() bool
	IsGate() bool
	Location() dir.Location
	SetLocation(loc dir.Location)
	Direction() dir.Direction
	SetDirection(dir dir.Direction)
	Icon() rune
	Colour() Colour
	SetIcon(icon rune)
	SetColour(colour Colour)
	Tick()
	Score() int
	TriggerEffect(el Element)
	Restart()
}
