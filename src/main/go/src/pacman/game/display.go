package game

import (
	"fmt"
	"io"
	"time"
)

// Display interface for abstracting out terminal handling
type Display interface {
	Init(stream io.Writer)
	Dimensions() (int, int)
	Refresh(output string, colour []Colour)
	Flash()
	Close()
}

// ANSI escape sequences
const esc = "\u001B["
const clr string = esc + "H" + esc + "2J" + esc + "1m"
const rst string = esc + "0m"
const revOn string = esc + "?5h"
const revOff string = esc + "?5l"
const blink string = esc + "5m"
const rev string = esc + "7m"

type terminal struct {
	game          Game
	rows, columns int
	output        io.Writer
}

// New up a terminal
func (t *terminal) New(game Game) Display {
	display := &terminal{game: game}
	return display
}

// Init a terminal and determine display size
func (t *terminal) Init(stream io.Writer) {
	t.output = stream
	if t.game != nil {
		t.columns, t.rows = t.game.Dimensions()
	}
}

// Dimensions of the display as provided by the game
func (t *terminal) Dimensions() (int, int) {
	return t.columns, t.rows
}

// Refresh the terminal with the supplied output characters and colourmap
func (t *terminal) Refresh(output string, colour []Colour) {
	fmt.Fprintf(t.output, clr)
	fmt.Fprintf(t.output, output)
	fmt.Fprintf(t.output, rst)
	fmt.Fprintln(t.output)
}

// Flash the display
func (t *terminal) Flash() {
	fmt.Printf(revOn)
	time.Sleep(time.Second / 10)
	fmt.Printf(revOff)
}

// Close down the display
func (t *terminal) Close() {
	fmt.Fprintf(t.output, rst)
	fmt.Fprintln(t.output)
}
