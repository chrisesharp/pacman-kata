package main

// Playfield interface
type Playfield interface {
	New(rows, columns int) Playfield
	ClearField()
	Get(loc Location) (rune, Colour)
	Set(loc Location, icon rune, colour Colour)
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

// Set the location of the field to this string value
func (f *playField) Set(loc Location, icon rune, colour Colour) {
	x := loc.x
	y := loc.y
	f.icon[y][x] = icon
	f.colour[y][x] = colour
}

// Get the icon for this location
func (f *playField) Get(loc Location) (rune, Colour) {
	return f.icon[loc.y][loc.x], f.colour[loc.y][loc.x]
}

// Dimensions of the playfield
func (f *playField) Dimensions() (int, int) {
	return f.columns, f.rows
}
