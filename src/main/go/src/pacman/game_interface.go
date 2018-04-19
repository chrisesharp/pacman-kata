package main

import "os"

// Game state interface
type Game interface {
	New() Game
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
	GetPacman() GameElement
	SetPacman(GameElement)
	AddGhost(ghost GameElement)
	GetGhosts() []GameElement
	GetPills() []GameElement
	AddPill(pill GameElement)
	RemovePill(pill GameElement)
	GetWalls() []GameElement
	AddWall(GameElement)
	GetGate() GameElement
	GetOutput() (string, []Colour)
	Tick()
	Play(debug bool)
	KeyPress(key string)
	UseAnimation()
	Animated() bool
	Quit()
}

// GameElement interface
type GameElement interface {
	AddToGame(game Game)
	IsForceField() bool
	IsGate() bool
	Location() Location
	SetLocation(loc Location)
	Direction() Direction
	SetDirection(dir Direction)
	Icon() rune
	Colour() Colour
	SetIcon(icon rune)
	SetColour(colour Colour)
	Tick()
	Score() int
	TriggerEffect(el GameElement)
	Restart()
}
