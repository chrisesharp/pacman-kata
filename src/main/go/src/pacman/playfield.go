package main

import "pacman/dir"

// Playfield interface
type Playfield interface {
	New(rows, columns int) Playfield
	ClearField()
	RenderField() (string, []Colour)
	Get(loc dir.Location) (rune, Colour)
	Set(loc dir.Location, icon rune, colour Colour)
	Dimensions() (int, int)
}

type playField struct {
	icon          [][]rune
	colour        [][]Colour
	rows, columns int
	defaultColour Colour
	defaultRune   rune
}

func (f *playField) New(rows, columns int) Playfield {
	f = &playField{rows: rows, columns: columns, icon: nil, colour: nil}
	f.defaultColour = DefaultColour
	f.defaultRune = ' '
	f.ClearField()
	return f
}

// ClearField to blank spaces
func (f *playField) ClearField() {
	charmap := make([][]rune, f.rows)
	colourmap := make([][]Colour, f.rows)
	for y := range charmap {
		charmap[y] = make([]rune, f.columns)
		colourmap[y] = make([]Colour, f.columns)
		for x := range charmap[y] {
			charmap[y][x] = f.defaultRune
			colourmap[y][x] = f.defaultColour
		}
	}
	f.icon = charmap
	f.colour = colourmap
}

// RenderField as video and colour streams
func (f *playField) RenderField() (string, []Colour) {
	var buffer string
	var colourBuf []Colour
	columns, rows := f.Dimensions()
	lastline := rows - 1
	for y := 0; y < rows; y++ {
		for x := 0; x < columns; x++ {
			r, c := f.Get(dir.Location{X: x, Y: y})
			buffer += string(r)
			colourBuf = append(colourBuf, c)
		}
		buffer += addNewLineIfNotFinished(y, lastline)
	}
	return buffer, colourBuf
}

func addNewLineIfNotFinished(current, last int) string {
	if current < last {
		return string('\n')
	}
	return ""
}

// Set the location of the field to this string value
func (f *playField) Set(loc dir.Location, icon rune, colour Colour) {
	x := loc.X
	y := loc.Y
	f.icon[y][x] = icon
	f.colour[y][x] = colour
}

// Get the icon for this location
func (f *playField) Get(loc dir.Location) (rune, Colour) {
	return f.icon[loc.Y][loc.X], f.colour[loc.Y][loc.X]
}

// Dimensions of the playfield
func (f *playField) Dimensions() (int, int) {
	return f.columns, f.rows
}
