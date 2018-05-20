package game

import (
	"fmt"
	"io"
	"strings"
	"time"

	termbox "github.com/nsf/termbox-go"
)

var palette = map[ColourAtt]termbox.Attribute{
	BLACK:  termbox.ColorBlack,
	RED:    termbox.ColorRed,
	GREEN:  termbox.ColorGreen,
	YELLOW: termbox.ColorYellow,
	BLUE:   termbox.ColorBlue,
	PURPLE: termbox.ColorMagenta,
	CYAN:   termbox.ColorCyan,
	WHITE:  termbox.ColorWhite,
	BLINK:  termbox.AttrBold | termbox.ColorBlack,
}

type colourTerminal struct {
	game          Game
	rows, columns int
}

func (t *colourTerminal) New(game Game) Display {
	display := &colourTerminal{game: game}
	return display
}

func (t *colourTerminal) Init(stream io.Writer) {
	if t.game != nil {
		if !termbox.IsInit {
			termbox.Init()
		}
		termbox.SetOutputMode(termbox.OutputNormal)
		t.columns, t.rows = t.game.Dimensions()
	}
}

// Dimensions of the display as provided by the game
func (t *colourTerminal) Dimensions() (int, int) {
	return t.columns, t.rows
}

func (t *colourTerminal) Refresh(output string, colourMap []Colour) {
	runes := []rune(strings.Replace(output, "\n", "", -1))
	for y := 0; y <= t.rows; y++ {
		for x := 0; x < t.columns; x++ {
			index := (y * t.columns) + x
			colour := colourMap[index]
			termbox.SetCell(x, y, runes[index], palette[colour[0]], palette[colour[1]])
		}
	}
	termbox.Flush()
}

func (t *colourTerminal) Flash() {
	fmt.Printf(revOn)
	time.Sleep(time.Second / 10)
	fmt.Printf(revOff)
}

func (t *colourTerminal) Close() {
	if t.game != nil && termbox.IsInit {
		termbox.Close()
	}
	fmt.Println()
}
