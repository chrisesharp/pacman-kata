package main

// GetElement prototype of the relevant type
func GetElement(icon rune, location Location) GameElement {
	funcs := []func(icon rune, loc Location) GameElement{
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
