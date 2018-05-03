package main

import (
	"flag"
	"os"
	"pacman/game"
)

func main() {
	filePtr := flag.String("file", "data/pacman.txt", "level txt file")
	colour := flag.Bool("colour", false, "use colour display")
	debug := flag.Bool("debug", false, "debug mode plays only one frame")
	animation := flag.Bool("animation", true, "use animated icons")
	flag.Parse()
	game.Start(*filePtr, *colour, *animation, *debug, os.Stdout)
}
