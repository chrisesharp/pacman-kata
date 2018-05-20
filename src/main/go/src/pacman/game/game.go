package game

import (
	"fmt"
	"io/ioutil"
	"os"
	"pacman/dir"
	"pacman/scoreboard"
	"strconv"
	"strings"
	"time"
	"unicode"
)

var theGame Game

type gameState struct {
	debug        bool
	input        string
	output       string
	colour       []Colour
	levelMaps    LevelMap
	field        Playfield
	lives, score int
	pacman       Element
	ghosts       []Element
	pills        []Element
	walls        []Element
	gameOver     bool
	level        int
	maxLevel     int
	usingPills   bool
	animated     bool
	display      Display
	controller   Controller
	outputStream *os.File
	frequency    int
	player       string
	scoreboard   scoreboard.Client
}

var keyDirectionMap = map[string]dir.Direction{
	"i": dir.UP,
	"m": dir.DOWN,
	"j": dir.LEFT,
	"l": dir.RIGHT,
}

const frequency = 850

// New clears state
func (game *gameState) New(debug bool) Game {
	gameFrequency := frequency
	if debug {
		gameFrequency = 0
	}
	return &gameState{debug: debug,
		input:     "",
		field:     nil,
		lives:     3,
		score:     0,
		pacman:    nil,
		gameOver:  false,
		level:     1,
		frequency: gameFrequency}
}

// SetDisplay for rendering output
func (game *gameState) SetDisplay(thisDisplay Display) {
	game.display = thisDisplay
}

// SetController for controlling the game
func (game *gameState) SetController(controller Controller) {
	game.controller = controller
}

// SetInput for parsing
func (game *gameState) SetInput(input string) {
	game.input = strings.Trim(input, "\n")
}

// SetLevelMaps for multiple levels
func (game *gameState) SetLevelMaps(maps *levelStruct) {
	game.levelMaps = maps
	game.levelMaps.Unpack()
	game.maxLevel = game.levelMaps.Max()
}

// SetPlayer name for Scoreboard
func (game *gameState) SetPlayer(player string) {
	game.player = player
}

// Parse an input into game state
func (game *gameState) Parse() {
	game.setGameInput()
	runes, columns := game.getMapRunes()
	game.parseStatus(string(runes[:columns]))
	rowData := strings.Split(string(runes[columns+1:]), "\n")
	rows := len(rowData)
	game.field = new(playField).New(rows, columns)
	game.parseTokens(rowData)
}

func (game *gameState) setGameInput() {
	if game.levelMaps != nil {
		game.SetInput(game.levelMaps.Get(game.level))
	} else if strings.Contains(game.input, "SEPARATOR") {
		gameLevel := &levelStruct{levelMaps: game.input}
		game.SetLevelMaps(gameLevel)
		game.SetInput(game.levelMaps.Get(game.level))
	}
}

func (game *gameState) getMapRunes() ([]rune, int) {
	columns := strings.Index(game.input, "\n")
	runes := []rune(game.input)
	return runes, columns
}

func (game *gameState) parseStatus(status string) {
	f := func(c rune) bool {
		return !unicode.IsNumber(c) && !unicode.IsPunct(c)
	}
	statusFields := strings.FieldsFunc(status, f)
	lives, err := strconv.Atoi(statusFields[0])
	if err == nil {
		game.lives = lives
	}
	score, err := strconv.Atoi(statusFields[1])
	if err == nil {
		game.score = score
	}
}

func (game *gameState) parseTokens(rowData []string) {
	columns, rows := game.field.Dimensions()
	for y := 0; y < rows; y++ {
		runes := []rune(rowData[y])
		for x := 0; x < columns; x++ {
			GetElement(runes[x], dir.Location{X: x, Y: y}).AddToGame(game)
		}
	}
}

// SetPlayfield for this game state
func (game *gameState) SetPlayfield(field Playfield) {
	game.field = field
}

// Render the game state as a string
func (game *gameState) Render() {
	columns, _ := game.field.Dimensions()
	game.output, game.colour = renderStatus(game.lives, game.score, columns)
	buf, colbuf := game.field.RenderField()
	game.output += buf
	game.colour = append(game.colour, colbuf...)
}

func renderStatus(theLives int, theScore int, columns int) (string, []Colour) {
	var colourBuf []Colour
	lives := strconv.Itoa(theLives)
	score := strconv.Itoa(theScore)
	padding := columns - len(lives) - len(score)
	buffer := fmt.Sprintf("%s", lives)
	buffer += strings.Repeat(" ", padding)
	buffer += fmt.Sprintf("%s\n", score)
	for i := 0; i < len(buffer)-1; i++ {
		colourBuf = append(colourBuf, DefaultColour)
	}
	return buffer, colourBuf
}

// Tick the game turn over
func (game *gameState) Tick() {
	for _, el := range CollectElements(game.ghosts, []Element{game.pacman}) {
		el.Tick()
	}

	if game.isLevelClear() {
		game.nextLevel()
	}
	game.updateField()
}

func (game *gameState) updateField() {
	game.field.ClearField()
	for _, el := range CollectElements(game.pills, game.walls, game.ghosts, []Element{game.pacman}) {
		loc := el.Location()
		game.field.Set(loc, el.Icon(), el.Colour())
	}
	if game.gameOver {
		game.printGameOver()
	}
}

// Dimensions of the game field
func (game *gameState) Dimensions() (int, int) {
	if game.field != nil {
		return game.field.Dimensions()
	}
	return 0, 0
}

// Lives of the game player
func (game *gameState) Lives() int {
	return game.lives
}

// Score of the  game player
func (game *gameState) Score() int {
	return game.score
}

// GetPacman object from game state
func (game *gameState) GetPacman() Element {
	return game.pacman
}

func (game *gameState) SetPacman(pacman Element) {
	game.pacman = pacman
	game.field.Set(pacman.Location(), pacman.Icon(), pacman.Colour())
}

// AddGhost to list of ghosts in game state
func (game *gameState) AddGhost(ghost Element) {
	game.ghosts = append(game.ghosts, ghost)
	game.field.Set(ghost.Location(), ghost.Icon(), ghost.Colour())
}

// GetGhosts list from game state
func (game *gameState) GetGhosts() []Element {
	return game.ghosts
}

// GetPills list from game state
func (game *gameState) GetPills() []Element {
	return game.pills
}

// AddPill to list of pills in game state
func (game *gameState) AddPill(pill Element) {
	game.usingPills = true
	game.pills = append(game.pills, pill)
	game.field.Set(pill.Location(), pill.Icon(), pill.Colour())
}

// RemovePill by element from game state
func (game *gameState) RemovePill(thePill Element) {
	for i, pill := range game.pills {
		if pill == thePill {
			game.pills = append(game.pills[:i], game.pills[i+1:]...)
		}
	}
}

// GetWalls list from game state
func (game *gameState) GetWalls() []Element {
	return game.walls
}

// AddWall to list
func (game *gameState) AddWall(wall Element) {
	game.walls = append(game.walls, wall)
	game.field.Set(wall.Location(), wall.Icon(), wall.Colour())
}

// GetGate list from game state
func (game *gameState) GetGate() Element {
	var gate Element
	for _, thisWall := range game.walls {
		if thisWall.IsGate() {
			gate = thisWall
		}
	}
	return gate
}

// SetScore for game state
func (game *gameState) SetScore(score int) {
	game.score = score
}

// PostScore to the scoreboard
func (game *gameState) PostScore() {
	if game.scoreboard == nil {
		game.connectToScoreboard()
	}
	game.scoreboard.PostScore(game.Score())
}

// GetScores from the scoreboard
func (game *gameState) GetScores() []string {
	return game.scoreboard.Scores()
}

// SetLevel for game
func (game *gameState) SetLevel(level int) {
	game.level = level
}

// SetMaxLevel for game
func (game *gameState) SetMaxLevel(max int) {
	game.maxLevel = max
}

// SetLives for game state
func (game *gameState) SetLives(lives int) {
	game.lives = lives
	if game.lives == 0 {
		game.gameOver = true
	}
}

// GetOutput of a render as a string
func (game *gameState) GetOutput() (string, []Colour) {
	return game.output, game.colour
}

func (game *gameState) isLevelClear() bool {
	return (game.usingPills && len(game.pills) == 0)
}

// LevelCleared behaviour
func (game *gameState) nextLevel() {
	game.level++
	if game.level > game.maxLevel {
		game.gameOver = true
		game.pacman.Restart()
	} else {
		game.pills = nil
		game.ghosts = nil
		game.walls = nil
		game.pacman.Restart()
		game.Parse()
	}
}

// GameOver behaviour
func (game *gameState) printGameOver() {
	columns, rows := game.field.Dimensions()
	game.gameOver = true
	const GAME = "GAME"
	const OVER = "OVER"
	y := (rows / 2) - 2
	padding := ((columns - 2) - len(GAME)) / 2
	for i, r := range GAME {
		game.field.Set(dir.Location{Y: y, X: padding + i + 1}, r, DefaultColour)
	}
	for i, r := range OVER {
		game.field.Set(dir.Location{Y: y + 1, X: padding + i + 1}, r, DefaultColour)
	}
}

func (game *gameState) connectToScoreboard() {
	scoreboardURL := os.Getenv("SCOREBOARD_URL")
	game.scoreboard = scoreboard.NewClient(scoreboardURL, game.player)
}

// Play a game
func (game *gameState) Play(debug bool) {
	game.Parse()
	game.controller.Listen()
	game.display.Init(game.outputStream)
	pacman := game.GetPacman()
	for ok := (pacman != nil); ok; ok = (!debug && !game.gameOver) {
		game.Tick()
		game.Render()
		game.display.Refresh(game.GetOutput())
		time.Sleep(time.Second / 8)
		if !pacman.(Pacman).Alive() {
			game.display.Flash()
			pacman.Restart()
		}
	}
	time.Sleep(time.Duration(game.frequency) * time.Millisecond)
	game.PostScore()
	game.controller.Close()
	game.display.Close()
}

func (game *gameState) KeyPress(key string) {
	game.pacman.(Pacman).Go(keyDirectionMap[key])
}

func (game *gameState) UseAnimation() {
	game.animated = true
}

func (game *gameState) Animated() bool {
	return game.animated
}

func (game *gameState) Quit() {
	game.gameOver = true
}

func (game *gameState) IsDebug() bool {
	return game.debug
}

// SetOutput stream for the display
func (game *gameState) SetOutput(outstream *os.File) {
	game.outputStream = outstream
}

// NewGame creates a new gameState
func NewGame(debug bool) Game {
	return new(gameState).New(debug)
}

// Start the game with correct flags
func Start(filePtr string, colour bool, animation bool, debug bool, outstream *os.File) {
	theGame = NewGame(debug)
	player := os.Getenv("USER")
	if len(player) == 0 {
		player = "go_player"
	}
	theGame.SetPlayer(player)
	theGame.SetOutput(outstream)
	if colour {
		theGame.SetDisplay(new(colourTerminal).New(theGame))
	} else {
		theGame.SetDisplay(new(terminal).New(theGame))
	}

	theGame.SetController(new(keyboard).New(theGame))
	if animation {
		theGame.UseAnimation()
	}

	level, err := ioutil.ReadFile(filePtr)
	if err != nil {
		panic(err)
	}

	theGame.SetInput(string(level))
	theGame.Play(debug)
}
