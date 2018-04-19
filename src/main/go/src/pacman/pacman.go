package main

const deadIcon rune = '*'

var pacmanDirs = map[Direction][]rune{
	LEFT:  {'>', '}'},
	RIGHT: {'<', '{'},
	UP:    {'V', 'v'},
	DOWN:  {'Λ', '^'},
}

// Pacman interface extending GameElement
type Pacman interface {
	GameElement
	Go(dir Direction)
	Alive() bool
}

type pacmanStruct struct {
	GameElement
	moving bool
	alive  bool
	frame  int
	game   Game
}

// NewPacman creates a clean populated pacmanStruct
func NewPacman(game Game, icon rune, loc Location) Pacman {
	colour := Colour{YELLOW, BLACK}
	alive := true
	if icon == deadIcon {
		alive = false
	}
	element := NewElement(game, icon, loc, facing(icon), 0)
	element.SetColour(colour)
	return &pacmanStruct{GameElement: element,
		moving: true,
		alive:  alive,
		frame:  0,
		game:   game}
}

// Tick activates this elements turn
func (p *pacmanStruct) Tick() {
	if p.moving {
		nextLoc := p.Location().Next(p.Direction())
		if p.isClear(nextLoc) {
			p.SetLocation(nextLoc)
			p.Animate()
		}
		p.checkCollisions()
	}
}

func (p *pacmanStruct) checkCollisions() {
	for _, element := range CollectElements(p.game.GetPills(), p.game.GetGhosts()) {
		if element.Location() == p.Location() {
			element.TriggerEffect(p)
		}
	}
}

// TriggerEffect of colliding with this element
func (p *pacmanStruct) TriggerEffect(el GameElement) {
	p.alive = false
	p.moving = false
	p.game.SetLives(p.game.Lives() - 1)
	p.SetIcon(deadIcon)
}

// Alive pacman
func (p *pacmanStruct) Alive() bool {
	return p.alive
}

func (p *pacmanStruct) isClear(nextLoc Location) bool {
	clear := true
	for _, wall := range p.game.GetWalls() {
		if (wall.Location() == nextLoc) && (!wall.IsForceField()) {
			clear = false
		}
	}
	return clear
}

// Icon override for pacmanStruct
func (p *pacmanStruct) Icon() rune {
	var icon rune
	if p.alive {
		icon = p.pacmanIcon(p.Direction())
	} else {
		icon = deadIcon
	}
	return icon
}

// GetPacman returns a new Pacman if the icon is a pacman
func GetPacman(icon rune, location Location) GameElement {
	if IsPacman(icon) {
		return NewPacman(nil, icon, location)
	}
	return nil
}

// IsPacman returns true if the icon represents pacman
func IsPacman(thisIcon rune) bool {
	if thisIcon == deadIcon {
		return true
	}
	for _, icons := range pacmanDirs {
		for _, icon := range icons {
			if icon == thisIcon {
				return true
			}
		}
	}
	return false
}

func facing(thisIcon rune) Direction {
	for direction, icons := range pacmanDirs {
		for _, icon := range icons {
			if icon == thisIcon {
				return direction
			}
		}
	}
	return -1
}

func (p *pacmanStruct) pacmanIcon(dir Direction) rune {
	return pacmanDirs[dir][p.frame]
}

// Animate advances the current frame
func (p *pacmanStruct) Animate() {
	if p.game.Animated() {
		p.frame = (p.frame + 1) % 2
	}
}

// Restart the element in its initial placing
func (p *pacmanStruct) Restart() {
	p.alive = true
	p.moving = false
	p.GameElement.Restart()
}

// Go makes pacman start moving in a given direction
func (p *pacmanStruct) Go(dir Direction) {
	nextLoc := p.Location().Next(dir)
	if p.isClear(nextLoc) {
		p.moving = true
		p.SetDirection(dir)
	}
}

// AddToGame adds a new type of this element to the game
func (p *pacmanStruct) AddToGame(game Game) {
	pacman := NewPacman(game, p.Icon(), p.Location())
	game.SetPacman(pacman)
}
