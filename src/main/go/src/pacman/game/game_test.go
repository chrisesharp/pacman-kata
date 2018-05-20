package game

import (
	"bytes"
	"flag"
	"fmt"
	"io"
	"os"
	. "pacman/dir"
	"strconv"
	"strings"
	"testing"
	"time"

	"github.com/DATA-DOG/godog"
	"github.com/DATA-DOG/godog/colors"
	"github.com/DATA-DOG/godog/gherkin"
)

var ANSIcodes = map[string]string{}
var gameLevel *levelStruct
var testDisplay Display
var game Game
var outputStream *bytes.Buffer
var commandArgs []string
var serviceResponse string
var columns int
var score int
var lives int

var opt = godog.Options{Output: colors.Colored(os.Stdout)}

func init() {
	godog.BindFlags("godog.", flag.CommandLine, &opt)
	opt.Paths = []string{"features/"}
	opt.Randomize = time.Now().UTC().UnixNano() // randomize scenario order
}

func TestMain(m *testing.M) {
	flag.Parse()
	if opt.Tags == "" {
		opt.Tags = os.Getenv("BDD")
	}
	status := godog.RunWithOptions("godogs", func(s *godog.Suite) {
		FeatureContext(s)
	}, opt)

	if st := m.Run(); st > status {
		status = st
	}
	os.Exit(status)
}

/** Givens ******************************************************/
// Given
func theCommandArg(arg string) error {
	commandArgs = append(commandArgs, arg)
	return nil
}

// Given
func theScreenColumnWidthIs(cols int) error {
	columns = cols
	return nil
}

// Given
func thePlayerScoreIs(thisScore int) error {
	score = thisScore
	return nil
}

// Given
func thePlayerHasLives(theLives int) error {
	lives = theLives
	return nil
}

// Given
func theGameFieldOfX(columns, rows int) error {
	field := new(playField).New(rows, columns)
	theGame.SetPlayfield(field)
	return nil
}

//Given
func aPacmanAtFacing(x, y int, facing string) error {
	var dir Direction
	switch facing {
	case "LEFT":
		dir = LEFT
	case "RIGHT":
		dir = RIGHT
	case "UP":
		dir = UP
	case "DOWN":
		dir = DOWN
	}
	pacman := NewPacman(theGame, pacmanDirs[dir][0], Location{X: x, Y: y})

	theGame.SetPacman(pacman)
	return nil
}

// Given
func aGhostAt(x, y int) error {
	ghost := NewGhost(theGame, normalGhost, Location{X: x, Y: y})
	theGame.AddGhost(ghost)
	return nil
}

//Given
func wallsAtTheFollowingPlaces(wallSpec *gherkin.DataTable) error {
	for _, row := range wallSpec.Rows {
		icon := []rune(row.Cells[0].Value)
		x, _ := strconv.Atoi(row.Cells[1].Value)
		y, _ := strconv.Atoi(row.Cells[2].Value)
		wall := NewWall(theGame, icon[0], Location{X: x, Y: y})
		wall.AddToGame(theGame)
	}
	return nil
}

// Given
func theGameStateIs(input *gherkin.DocString) error {
	theGame.SetInput(input.Content)
	return nil
}

// Given
func theScoreIs(score int) error {
	theGame.SetScore(score)
	return nil
}

// Given
func theLivesAre(lives int) error {
	theGame.SetLives(lives)
	return nil
}

// Given
func aColourDisplay() error {
	return godog.ErrPending
}

// Given
func theANSISequenceIs(sequence, hex string) error {
	ANSIcodes[sequence] = hex
	return nil
}

// Given
func aDisplay() error {
	testDisplay = new(terminal).New(theGame)
	theGame.SetDisplay(testDisplay)
	return nil
}

// Given
func aGameWithLevels(levels int, levelMaps *gherkin.DocString) error {
	theGame.SetInput(levelMaps.Content)
	return nil
}

// Given
func thisIsLevel(level int) error {
	theGame.SetLevel(level)
	return nil
}

// Given
func theMaxLevelIs(maxLevel int) error {
	theGame.SetMaxLevel(maxLevel)
	return nil
}

// Given
func theGameUsesAnimation() error {
	theGame.UseAnimation()
	return nil
}

// Given
func thisIsTheLastLevel() error {
	theGame.SetMaxLevel(1)
	return nil
}

// Given
func theUserIs(player string) error {
	theGame.SetPlayer(player)
	return nil
}

/** Whens ******************************************************/

// When
func iRunTheCommandWithTheArgs() error {
	r, w, _ := os.Pipe()
	defer w.Close()

	f := flag.NewFlagSet("f", flag.ContinueOnError)
	filePtr := f.String("file", "data/pacman.txt", "level txt file")
	colour := f.Bool("colour", false, "use colour display")
	debug := f.Bool("debug", false, "debug mode plays only one frame")
	animation := f.Bool("animation", false, "use animated icons")
	if err := f.Parse(commandArgs[1:]); err == nil {
		fmt.Printf("Using file %s, colour=%v, animation=%v, debug=%v",
			*filePtr, *colour, *animation, *debug)
		go func() {
			io.Copy(outputStream, r)
		}()
		Start(*filePtr, *colour, *animation, *debug, w)
	}
	return nil
}

// When
func weRenderTheStatusLine() error {
	output, _ := renderStatus(lives, score, columns)
	outputStream.WriteString(output)
	return nil
}

// When
func weParseTheState() error {
	theGame.Parse()
	return nil
}

// When
func weRenderTheGame() error {
	theGame.Render()
	return nil
}

// When
func wePlayTurns(turns int) error {
	for i := 0; i < turns; i++ {
		theGame.Tick()
	}
	return nil
}

// When
func weRefreshTheDisplayWithTheBuffer(buffer string) error {
	hexstring := fmt.Sprintf("%X", buffer)
	ANSIcodes[buffer] = hexstring
	testDisplay.Refresh(buffer, nil)
	return nil
}

// When
func thePlayerPresses(key string) error {
	theGame.KeyPress(key)
	return nil
}

// When
func initializeTheDisplay() error {
	testDisplay.Init(io.Writer(outputStream))
	return nil
}

// When
func iPostTheScoreToTheScoreboard() error {
	theGame.PostScore()
	return nil
}

// When
func iGetTheScores() error {
	serviceResponse = theGame.GetScores()[0]
	return nil
}

/** Thens ******************************************************/

// Then
func iShouldGetTheFollowingOutput(expected *gherkin.DocString) error {
	received := strings.TrimSuffix(cleanupAnsi(outputStream.String()), "\r")
	if expected.Content == received {
		return nil
	}
	return fmt.Errorf("\nExpected:[%s]\nReceived:[%s]", expected.Content, received)
}

func cleanupAnsi(received string) string {
	const esc = "\u001B["
	const clr string = esc + "H" + esc + "2J" + esc + "1m"
	const rst string = esc + "0m"
	const revOn string = esc + "?5h"
	const revOff string = esc + "?5l"
	const blink string = esc + "5m"
	const rev string = esc + "7m"
	received = strings.Replace(received, rst, "", -1)
	received = strings.Replace(received, clr, "", -1)
	received = strings.Replace(received, revOn, "", -1)
	received = strings.Replace(received, revOff, "", -1)
	received = strings.Replace(received, rev, "", -1)
	received = strings.Replace(received, blink, "", -1)
	outstream := strings.TrimSpace(received)
	return outstream
}

// Then
func theGameFieldShouldBeX(x, y int) error {
	cols, rows := theGame.Dimensions()
	if (cols != x) || (rows != y) {
		return fmt.Errorf("expected dimensions to be %v,%v but it is %v,%v", x, y, cols, rows)
	}
	return nil
}

// Then
func thePlayerShouldHaveLives(lives int) error {
	if theGame.Lives() != lives {
		return fmt.Errorf("expected lives to be %v, but it is %v", lives, theGame.Lives())
	}
	return nil
}

// Then
func pacmanShouldBeAt(x, y int) error {
	pacman := theGame.GetPacman()
	loc := pacman.Location()
	if (loc.X != x) && (loc.Y != y) {
		return fmt.Errorf("expected pacman to be at %v,%v but it is at %v,%v", x, y, loc.X, loc.Y)
	}
	return nil
}

// Then
func pacmanShouldBeFacing(direction string) error {
	pacman := theGame.GetPacman()

	if !pacman.Direction().Equals(direction) {
		return fmt.Errorf("expected pacman to be facing %v but is  %v", direction, pacman.Direction())
	}
	return nil
}

// Then
func ghostShouldBeAt(x, y int) error {
	for _, ghost := range theGame.GetGhosts() {
		loc := ghost.Location()
		if loc.X == x && loc.Y == y {
			return nil
		}
	}
	return fmt.Errorf("expected ghost at %v,%v but didn't find one", x, y)
}

// Then
func thenPacmanShouldGo(direction string) error {
	pacman := theGame.GetPacman()

	if !pacman.Direction().Equals(direction) {
		return fmt.Errorf("expected pacman to be facing %v but is %v", direction, pacman.Direction())
	}
	return nil
}

// Then
func thereShouldBeAPointPillAt(points, x, y int) error {
	for _, pill := range theGame.GetPills() {
		loc := pill.Location()
		if loc.X == x && loc.Y == y {
			return nil
		}
	}
	return fmt.Errorf("expected pill at %v,%v but didn't find one", x, y)
}

// Then
func thereShouldBeAWallAt(x, y int) error {
	for _, wall := range theGame.GetWalls() {
		loc := wall.Location()
		if loc.X == x && loc.Y == y {
			return nil
		}
	}
	return fmt.Errorf("expected wall at %v,%v but didn't find one", x, y)
}

// Then
func thereShouldBeAForceFieldAt(x, y int) error {
	for _, wall := range theGame.GetWalls() {
		loc := wall.Location()
		if loc.X == x && loc.Y == y && wall.IsForceField() {
			return nil
		}
	}
	return fmt.Errorf("expected force field at %v,%v but didn't find one", x, y)
}

// Then
func thereShouldBeAGateAt(x, y int) error {
	gate := theGame.GetGate()
	loc := gate.Location()
	if loc.X == x && loc.Y == y {
		return nil
	}
	return fmt.Errorf("expected gate at %v,%v but didn't find one", x, y)
}

// Then
func theGameScreenShouldBe(expected *gherkin.DocString) error {
	output, _ := theGame.GetOutput()
	if output == expected.Content {
		return nil
	}
	return fmt.Errorf("expected screen to be:\n======\n%v\n but was\n%v\n======", expected.Content, output)
}

// Then
func theDisplayByteStreamShouldBe(bytestream *gherkin.DataTable) error {
	var bytes bytes.Buffer
	for _, row := range bytestream.Rows {
		for _, cell := range row.Cells {
			fmt.Fprintf(&bytes, "%s", ANSIcodes[cell.Value])
		}
	}
	expected := bytes.String()
	received := fmt.Sprintf("%X", outputStream.String())
	if expected == received {
		return nil
	}
	return fmt.Errorf("\nExpected:%s\nReceived:%X", expected, received)
}

// Then
func theGameLivesShouldBe(lives int) error {
	if theGame.Lives() == lives {
		return nil
	}
	return fmt.Errorf("expected lives to be %v but was %v", lives, theGame.Lives())
}

// Then
func theScoreShouldBe(score int) error {
	if theGame.Score() != score {
		return fmt.Errorf("expected score to be %v, but it is %v", score, theGame.Score())
	}
	return nil
}

// Then
func pacmanShouldBeDead() error {
	pacman := theGame.GetPacman()
	if pacman != nil {
		if pacman.(Pacman).Alive() != false {
			return fmt.Errorf("expected pacman to be dead")
		}
	}
	return nil
}

// Then
func pacmanShouldBeAlive() error {
	pacman := theGame.GetPacman()
	if pacman != nil {
		if pacman.(Pacman).Alive() == false {
			return fmt.Errorf("expected pacman to be alive")
		}
	}
	return nil
}

// Then
func theGameDimensionsShouldEqualTheDisplayDimensions() error {
	gX, gY := theGame.Dimensions()
	dX, dY := testDisplay.Dimensions()
	if dX != gX && dY != gY {
		return fmt.Errorf("expected display to be %v,%v, but it is %v,%v", gX, gY, dX, dY)
	}
	return nil
}

// Then
func ghostAtShouldBeCalm(x, y int) error {
	for _, ghost := range theGame.GetGhosts() {
		loc := ghost.Location()
		if loc.X == x && loc.Y == y {
			if !ghost.(Ghost).IsPanicked() {
				return nil
			}
		}
	}
	return fmt.Errorf("expected ghost at %v,%v to be calm", x, y)
}

// Then
func ghostAtShouldBePanicked(x, y int) error {
	for _, ghost := range theGame.GetGhosts() {
		loc := ghost.Location()
		if loc.X == x && loc.Y == y {
			if ghost.(Ghost).IsPanicked() {
				return nil
			}
		}
	}
	return fmt.Errorf("expected ghost at %v,%v to be calm", x, y)
}

// Then
func iShouldGetTheFollowingResponse(expected *gherkin.DocString) error {
	if serviceResponse == expected.Content {
		return nil
	}
	return fmt.Errorf("expected screen to be:\n======\n%v\n but was\n%v\n======", expected.Content, serviceResponse)
}

// Feature matchers
func FeatureContext(s *godog.Suite) {
	s.Step(`^the player score is (\d+)$`, thePlayerScoreIs)
	s.Step(`^the player has (\d+) lives$`, thePlayerHasLives)
	s.Step(`^the player should have (\d+) lives$`, thePlayerShouldHaveLives)
	s.Step(`^the score should be (\d+)$`, theScoreShouldBe)
	s.Step(`^the game state is$`, theGameStateIs)
	s.Step(`^we parse the state$`, weParseTheState)
	s.Step(`^there should be a gate at (\d+) , (\d+)$`, thereShouldBeAGateAt)
	s.Step(`^pacman should be at (\d+) , (\d+)$`, pacmanShouldBeAt)
	s.Step(`^there should be a (\d+) point pill at (\d+) , (\d+)$`, thereShouldBeAPointPillAt)
	s.Step(`^pacman should be facing "([^"]*)"$`, pacmanShouldBeFacing)
	s.Step(`^ghost should be at (\d+) , (\d+)$`, ghostShouldBeAt)
	s.Step(`^there should be a wall at (\d+) , (\d+)$`, thereShouldBeAWallAt)
	s.Step(`^the game lives should be (\d+)$`, theGameLivesShouldBe)
	s.Step(`^there should be a force field at (\d+) , (\d+)$`, thereShouldBeAForceFieldAt)
	s.Step(`^the game field should be (\d+) x (\d+)$`, theGameFieldShouldBeX)
	s.Step(`^the score is (\d+)$`, theScoreIs)
	s.Step(`^the lives are (\d+)$`, theLivesAre)
	s.Step(`^we play (\d+) turn(.*)$`, wePlayTurns)
	s.Step(`^we render the game$`, weRenderTheGame)
	s.Step(`^the game screen should be$`, theGameScreenShouldBe)
	s.Step(`^a colour display$`, aColourDisplay)
	s.Step(`^the ANSI "([^"]*)" sequence is "([^"]*)"$`, theANSISequenceIs)
	s.Step(`^the display byte stream should be$`, theDisplayByteStreamShouldBe)
	s.Step(`^we refresh the display with the buffer "([^"]*)"$`, weRefreshTheDisplayWithTheBuffer)
	s.Step(`^a display$`, aDisplay)
	s.Step(`^a game with (\d+) levels$`, aGameWithLevels)
	s.Step(`^this is level (\d+)$`, thisIsLevel)
	s.Step(`^the max level  is (\d+)$`, theMaxLevelIs)
	s.Step(`^the game uses animation$`, theGameUsesAnimation)
	s.Step(`^the player presses "([^"]*)"$`, thePlayerPresses)
	s.Step(`^then pacman should go "([^"]*)"$`, thenPacmanShouldGo)
	s.Step(`^this is the last level$`, thisIsTheLastLevel)
	s.Step(`^pacman should be dead$`, pacmanShouldBeDead)
	s.Step(`^initialize the display$`, initializeTheDisplay)
	s.Step(`^the game dimensions should equal the display dimensions$`, theGameDimensionsShouldEqualTheDisplayDimensions)
	s.Step(`^the game field of (\d+) x (\d+)$`, theGameFieldOfX)
	s.Step(`^a pacman at (\d+) , (\d+) facing "([^"]*)"$`, aPacmanAtFacing)
	s.Step(`^walls at the following places:$`, wallsAtTheFollowingPlaces)
	s.Step(`^pacman should be alive$`, pacmanShouldBeAlive)
	s.Step(`^ghost at (\d+) , (\d+) should be calm$`, ghostAtShouldBeCalm)
	s.Step(`^ghost at (\d+) , (\d+) should be panicked$`, ghostAtShouldBePanicked)
	s.Step(`^the command arg "([^"]*)"$`, theCommandArg)
	s.Step(`^I run the command with the args$`, iRunTheCommandWithTheArgs)
	s.Step(`^I should get the following output:$`, iShouldGetTheFollowingOutput)
	s.Step(`^the screen column width is (\d+)$`, theScreenColumnWidthIs)
	s.Step(`^we render the status line$`, weRenderTheStatusLine)
	s.Step(`^a ghost at (\d+) , (\d+)$`, aGhostAt)
	s.Step(`^the user is "([^"]*)"$`, theUserIs)
	s.Step(`^I post the score to the scoreboard$`, iPostTheScoreToTheScoreboard)
	s.Step(`^I get the scores$`, iGetTheScores)
	s.Step(`^I should get the following response:$`, iShouldGetTheFollowingResponse)
	s.BeforeScenario(func(interface{}) {
		commandArgs = append(commandArgs, "game.go")
		outputStream = new(bytes.Buffer)
		theGame = new(gameState).New(true) // clean the state before every scenario
		testDisplay = new(terminal).New(nil)
		theGame.SetDisplay(testDisplay)
	})
	s.AfterScenario(func(interface{}, error) {
		commandArgs = nil
		return
	})
}
