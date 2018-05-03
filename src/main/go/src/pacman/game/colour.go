package game

// DefaultColour combination
var DefaultColour = Colour{WHITE, BLACK}

// Colour attributes for foreground and background
type Colour [2]ColourAtt

// ColourAtt colour acctribute
type ColourAtt uint16

// Colours
const (
	BLACK ColourAtt = iota
	RED
	GREEN
	YELLOW
	BLUE
	PURPLE
	CYAN
	WHITE
	BLINK
)
