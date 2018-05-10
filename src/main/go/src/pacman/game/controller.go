package game

import (
	"fmt"

	termbox "github.com/nsf/termbox-go"
)

// Controller interface for abstracting out terminal input
type Controller interface {
	Listen()
	Close()
}

type keyboard struct {
	game Game
}

var keyMap = map[termbox.Key]string{
	termbox.KeyArrowUp:    "i",
	termbox.KeyArrowDown:  "m",
	termbox.KeyArrowLeft:  "j",
	termbox.KeyArrowRight: "l",
}

func (t *keyboard) New(game Game) Controller {
	return &keyboard{game: game}
}

func (t *keyboard) Listen() {
	if t.game != nil && !t.game.IsDebug() {
		go t.listen()
	}
}

func (t *keyboard) Close() {
	if t.game != nil && !t.game.IsDebug() {
		termbox.Close()
	}
	fmt.Println()
}

func (t *keyboard) listen() {
	if !termbox.IsInit {
		termbox.Init()
	}
	termbox.SetInputMode(termbox.InputEsc)
	defer termbox.Close()
	for {
		switch ev := termbox.PollEvent(); ev.Type {
		case termbox.EventKey:
			if ev.Key == termbox.KeyEsc {
				t.game.Quit()
			} else {
				t.game.KeyPress(keyMap[ev.Key])
			}
		case termbox.EventError:
			termbox.Close()
			panic(ev.Err)
		}
	}
}
