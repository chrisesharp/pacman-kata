package game

import "pacman/dir"

// GetElement prototype of the relevant type
func GetElement(icon rune, location dir.Location) Element {
	funcs := []func(icon rune, loc dir.Location) Element{
		GetPacman,
		GetGhost,
		GetPill,
		GetWall,
	}
	for _, fn := range funcs {
		element := fn(icon, location)
		if element != nil {
			return element
		}
	}
	return NewElement(nil, icon, location, 0, 0)
}
