package main

import (
	"bytes"
	"fmt"
	"io"
	"os"
	"strconv"
	"testing"
	"time"

	"github.com/DATA-DOG/godog"
	"github.com/DATA-DOG/godog/gherkin"
)

var ANSIcodes = map[string]string{}
var gameLevel *levelStruct
var testDisplay Display
var game Game
var outputStream *bytes.Buffer

func TestMain(m *testing.M) {
	tags := os.Getenv("BDD")
	status := godog.RunWithOptions("godogs", func(s *godog.Suite) {
		FeatureContext(s)
	}, godog.Options{
		Format:    "pretty",
		Paths:     []string{"features/"},
		Tags:      tags,
		Randomize: time.Now().UTC().UnixNano(), // randomize scenario execution order
	})

	if st := m.Run(); st > status {
		status = st
	}
	os.Exit(status)
}

/** Givens ******************************************************/
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
	pacman := NewPacman(theGame, pacmanDirs[dir][0], Location{x, y})

	theGame.SetPacman(pacman)
	return nil
}

//Given
func wallsAtTheFollowingPlaces(wallSpec *gherkin.DataTable) error {
	for _, row := range wallSpec.Rows {
		icon := []rune(row.Cells[0].Value)
		x, _ := strconv.Atoi(row.Cells[1].Value)
		y, _ := strconv.Atoi(row.Cells[2].Value)
		wall := NewWall(theGame, icon[0], Location{x, y})
		theGame.AddWall(wall)
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

/** Whens ******************************************************/

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

/** Thens ******************************************************/

// Then
func theGameFieldShouldBeX(x, y int) error {
	cols, rows := theGame.Dimensions()
	if (cols != x) || (rows != y) {
		return fmt.Errorf("expected dimensions to be %v,%v but it is %v,%v", x, y, cols, rows)
	}
	return nil
}

// Then
func thePlayerHasLives(lives int) error {
	if theGame.Lives() != lives {
		return fmt.Errorf("expected lives to be %v, but it is %v", lives, theGame.Lives())
	}
	return nil
}

// Then
func thePlayerScoreIs(score int) error {
	if theGame.Score() != score {
		return fmt.Errorf("expected score to be %v, but it is %v", score, theGame.Score())
	}
	return nil
}

// Then
func pacmanIsAt(x, y int) error {
	pacman := theGame.GetPacman()
	loc := pacman.Location()
	if (loc.x != x) && (loc.y != y) {
		return fmt.Errorf("expected pacman to be at %v,%v but it is at %v,%v", x, y, loc.x, loc.y)
	}
	return nil
}

// Then
func pacmanIsFacing(direction string) error {
	pacman := theGame.GetPacman()

	if !pacman.Direction().Equals(direction) {
		return fmt.Errorf("expected pacman to be facing %v but is  %v", direction, pacman.Direction())
	}
	return nil
}

// Then
func ghostIsAt(x, y int) error {
	for _, ghost := range theGame.GetGhosts() {
		loc := ghost.Location()
		if loc.x == x && loc.y == y {
			return nil
		}
	}
	return fmt.Errorf("expected ghost at %v,%v but didn't find one", x, y)
}

// Then
func thenPacmanGoes(direction string) error {
	pacman := theGame.GetPacman()

	if !pacman.Direction().Equals(direction) {
		return fmt.Errorf("expected pacman to be facing %v but is %v", direction, pacman.Direction())
	}
	return nil
}

// Then
func thereIsAPointPillAt(points, x, y int) error {
	for _, pill := range theGame.GetPills() {
		loc := pill.Location()
		if loc.x == x && loc.y == y {
			return nil
		}
	}
	return fmt.Errorf("expected pill at %v,%v but didn't find one", x, y)
}

// Then
func thereIsAWallAt(x, y int) error {
	for _, wall := range theGame.GetWalls() {
		loc := wall.Location()
		if loc.x == x && loc.y == y {
			return nil
		}
	}
	return fmt.Errorf("expected wall at %v,%v but didn't find one", x, y)
}

// Then
func thereIsAForceFieldAt(x, y int) error {
	for _, wall := range theGame.GetWalls() {
		loc := wall.Location()
		if loc.x == x && loc.y == y && wall.IsForceField() {
			return nil
		}
	}
	return fmt.Errorf("expected force field at %v,%v but didn't find one", x, y)
}

// Then
func thereIsAGateAt(x, y int) error {
	gate := theGame.GetGate()
	loc := gate.Location()
	if loc.x == x && loc.y == y {
		return nil
	}
	return fmt.Errorf("expected gate at %v,%v but didn't find one", x, y)
}

// Then
func theGameScreenIs(expected *gherkin.DocString) error {
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
func theGameScoreShouldBe(score int) error {
	if theGame.Score() != score {
		return fmt.Errorf("expected score to be %v, but it is %v", score, theGame.Score())
	}
	return nil
}

// Then
func pacmanIsDead() error {
	pacman := theGame.GetPacman()
	if pacman != nil {
		if pacman.(Pacman).Alive() != false {
			return fmt.Errorf("expected pacman to be dead")
		}
	}
	return nil
}

// Then
func pacmanIsAlive() error {
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
		if loc.x == x && loc.y == y {
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
		if loc.x == x && loc.y == y {
			if ghost.(Ghost).IsPanicked() {
				return nil
			}
		}
	}
	return fmt.Errorf("expected ghost at %v,%v to be calm", x, y)
}

// Feature matchers
func FeatureContext(s *godog.Suite) {
	s.Step(`^the game state is$`, theGameStateIs)
	s.Step(`^we parse the state$`, weParseTheState)
	s.Step(`^there is a gate at (\d+) , (\d+)$`, thereIsAGateAt)
	s.Step(`^pacman is at (\d+) , (\d+)$`, pacmanIsAt)
	s.Step(`^the player has (\d+) lives$`, thePlayerHasLives)
	s.Step(`^the player score is (\d+)$`, thePlayerScoreIs)
	s.Step(`^there is a (\d+) point pill at (\d+) , (\d+)$`, thereIsAPointPillAt)
	s.Step(`^pacman is facing "([^"]*)"$`, pacmanIsFacing)
	s.Step(`^ghost is at (\d+) , (\d+)$`, ghostIsAt)
	s.Step(`^there is a wall at (\d+) , (\d+)$`, thereIsAWallAt)
	s.Step(`^the game lives should be (\d+)$`, theGameLivesShouldBe)
	s.Step(`^the game score should be (\d+)$`, theGameScoreShouldBe)
	s.Step(`^there is a force field at (\d+) , (\d+)$`, thereIsAForceFieldAt)
	s.Step(`^the game field should be (\d+) x (\d+)$`, theGameFieldShouldBeX)
	s.Step(`^the score is (\d+)$`, theScoreIs)
	s.Step(`^the lives are (\d+)$`, theLivesAre)
	s.Step(`^we play (\d+) turn(.*)$`, wePlayTurns)
	s.Step(`^we render the game$`, weRenderTheGame)
	s.Step(`^the game screen is$`, theGameScreenIs)
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
	s.Step(`^then pacman goes "([^"]*)"$`, thenPacmanGoes)
	s.Step(`^this is the last level$`, thisIsTheLastLevel)
	s.Step(`^pacman is dead$`, pacmanIsDead)
	s.Step(`^initialize the display$`, initializeTheDisplay)
	s.Step(`^the game dimensions should equal the display dimensions$`, theGameDimensionsShouldEqualTheDisplayDimensions)
	s.Step(`^the game field of (\d+) x (\d+)$`, theGameFieldOfX)
	s.Step(`^a pacman at (\d+) , (\d+) facing "([^"]*)"$`, aPacmanAtFacing)
	s.Step(`^walls at the following places:$`, wallsAtTheFollowingPlaces)
	s.Step(`^pacman is alive$`, pacmanIsAlive)
	s.Step(`^ghost at (\d+) , (\d+) should be calm$`, ghostAtShouldBeCalm)
	s.Step(`^ghost at (\d+) , (\d+) should be panicked$`, ghostAtShouldBePanicked)
	s.BeforeScenario(func(interface{}) {
		outputStream = new(bytes.Buffer)
		theGame = new(gameState).New() // clean the state before every scenario
		testDisplay = new(terminal).New(nil)
		theGame.SetDisplay(testDisplay)
	})
	s.AfterScenario(func(interface{}, error) {
		return
	})
}
