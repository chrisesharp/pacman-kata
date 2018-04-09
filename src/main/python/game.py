from time import sleep
from math import floor
from gamefield import GameField
from keyboard import Keyboard
from pacman import Pacman
from ghost import Ghost
from wall import Wall
from pill import Pill
from levels import LevelMaps


class Game(object):
    def __init__(self, input=None):
        if input is not None:
            self.input = input.rstrip()
        self.field = None
        self.lives = 3
        self.score = 0
        self.pacman = None
        self.ghosts = []
        self.pills = []
        self.walls = []
        self.gameOver = False
        self.controller = None
        self.gate = None
        self.level = 1
        self.lastLevel = 1
        self.levelMaps = None
        self.animation = False

    def play(self):
        self.parse()
        while (self.gameOver is False):
            self.tick()
            self.render()
            self.refresh()
            sleep(0.1)
            if (self.pacman.alive is False):
                self.pacman.restart()

    def parse(self):
        if self.levelMaps:
            self.input = self.levelMaps.getLevel(self.level)
        else:
            if "SEPARATOR" in self.input:
                self.levelMaps = LevelMaps(self.input)
                self.lastLevel = self.levelMaps.maxLevel()
                self.input = self.levelMaps.getLevel(self.level)
        columns = self.input.index("\n")
        self.parseStatus(self.input[:columns])
        screenRows = self.input[columns+1:].split('\n')
        rows = len(screenRows)
        self.field = GameField(columns, rows)
        self.parseField(screenRows)

    def parseStatus(self, statusLine):
        elements = statusLine.split()
        try:
            self.lives = int(elements[0])
            self.score = int(elements[1])
        except ValueError:
            pass

    def parseField(self, screenRows):
        for y in range(self.field.height()):
            for x in range(self.field.width()):
                if Pacman.isPacman(screenRows[y][x]):
                    self.pacman = Pacman(self, (x, y), screenRows[y][x])
                    self.pacman.useAnimation(self.animation)
                    self.field.add((x, y), self.pacman)
                if Wall.isWall(screenRows[y][x]):
                    wall = Wall((x, y), screenRows[y][x])
                    self.walls.append(wall)
                    self.field.add((x, y), wall)
                    if Wall.isGate(screenRows[y][x]):
                        self.gate = wall
                if Pill.isPill(screenRows[y][x]):
                    pill = Pill((x, y), screenRows[y][x])
                    self.pills.append(pill)
                    self.field.add((x, y), pill)
                if Ghost.isGhost(screenRows[y][x]):
                    ghost = Ghost(self, (x, y), screenRows[y][x])
                    self.ghosts.append(ghost)
                    self.field.add((x, y), ghost)

    def setGameField(self, width, height):
        self.field = GameField(width, height)

    def setPacman(self, coordinates, direction):
        self.pacman = Pacman(self, coordinates)
        self.pacman.setDirection(direction)
        self.field.add(coordinates, self.pacman)

    def addWall(self, coordinates, icon):
        wall = Wall(coordinates, icon)
        self.walls.append(wall)
        self.field.add(coordinates, wall)

    def tick(self):
        if not self.gameOver:
            for ghost in self.ghosts:
                ghost.tick()
            if self.pacman:
                self.pacman.tick()
        if self.controller:
            self.controller.tick()
        self.updateField()

    def render(self):
        self.output = "{lives:1d}".format(lives=self.lives)
        self.output += "{score:>{width}d}\n".format(score=self.score,
                                                    width=self.field.width()
                                                    - len(self.output))
        self.output += self.field.render()

    def refresh(self):
        print("\u001B[H" + "\u001B[2J" + "\u001B[1m")
        print(self.output)

    def updateField(self):
        newField = GameField(self.field.width(), self.field.height())
        for wall in self.walls:
            newField.add(wall.coordinates, wall)
        for pill in self.pills:
            newField.add(pill.coordinates, pill)
        for ghost in self.ghosts:
            newField.add(ghost.coordinates, ghost)
        if self.pacman:
            newField.add(self.pacman.coordinates, self.pacman)
        if self.gameOver:
            self.printGameOver(newField)
        self.field = newField

    def getElement(self, coords):
        return self.field.get(coords)

    def isGhost(self, coordinates):
        return Ghost.isGhost(self.field.get(coordinates))

    def isWall(self, coordinates):
        return Wall.isWall(self.field.get(coordinates))

    def isGate(self, coordinates):
        return Wall.isGate(self.field.get(coordinates))

    def isField(self, coordinates):
        return Wall.isField(self.field.get(coordinates))

    def isPill(self, coordinates):
        return Pill.isPill(self.field.get(coordinates))

    def isPacman(self, coordinates):
        return type(self.field.get(coordinates)) is Pacman

    def eatPill(self, coordinates):
        pill = self.field.get(coordinates)
        self.pills.remove(pill)
        self.score += pill.score()
        for ghost in self.ghosts:
            ghost.triggerEffect(pill)
        if len(self.pills) == 0:
            self.nextLevel()

    def killGhost(self, ghost):
        self.score += ghost.score()
        ghost.kill()

    def killPacman(self):
        self.pacman.kill()
        self.lives -= 1
        if (self.lives == 0):
            self.gameOver = True

    def getPacman(self):
        return self.pacman

    def setController(self, controller):
        self.controller = controller

    def move(self, direction):
        self.pacman.move(direction)

    def printGameOver(self, field):
        cols = field.width()
        rows = field.height()
        GAME = "GAME"
        OVER = "OVER"
        y = floor(rows / 2) - 2
        padding = floor(((cols - 2) - len(GAME)) / 2)
        for i in range(len(GAME)):
            x = padding + i + 1
            field.add((x, y), GAME[i])
            field.add((x, y+1), OVER[i])

    def setLevel(self, level):
        self.level = level

    def setMaxLevel(self, level):
        self.lastLevel = level

    def nextLevel(self):
        if self.level < self.lastLevel:
            self.level += 1
            self.pills = []
            self.walls = []
            self.ghosts = []
            self.parse()
        else:
            self.gameOver = True
            self.pacman.restart()

    def useAnimation(self):
        self.animation = True

    def getLives(self):
        return self.lives

    def getScore(self):
        return self.score


if __name__ == "__main__":
    file = "data/pacman.txt"
    with open(file) as f:
        levelMap = f.read()

    game = Game(levelMap)
    controller = Keyboard(game)
    game.setController(controller)
    game.useAnimation()
    game.play()
