package main

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

func (t *keyboard) New(game Game) Controller {
	return &keyboard{game: game}
}

func (t *keyboard) Listen() {
	if t.game != nil {
		go t.listen()
	}
}

func (t *keyboard) Close() {
	if t.game != nil {
		termbox.Close()
	}
	fmt.Println()
}

func (t *keyboard) listen() {
	if !termbox.IsInit {
		termbox.Init()
	}
	defer termbox.Close()
	termbox.SetInputMode(termbox.InputEsc)
	defer termbox.Close()
	for {
		switch ev := termbox.PollEvent(); ev.Type {
		case termbox.EventKey:
			switch ev.Key {
			case termbox.KeyEsc:
				termbox.Close()
				t.game.Quit()
			case termbox.KeyArrowUp:
				t.game.KeyPress("i")
			case termbox.KeyArrowDown:
				t.game.KeyPress("m")
			case termbox.KeyArrowLeft:
				t.game.KeyPress("j")
			case termbox.KeyArrowRight:
				t.game.KeyPress("l")
			}
		case termbox.EventError:
			termbox.Close()
			panic(ev.Err)
		}
	}
}
