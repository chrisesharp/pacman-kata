from direction import avoid, nextLocation, wrap, left, right, opposite
from direction import Direction
from random import randint


class Ghost(object):
    icons = {
            "M": False,
            "W": True
            }
    iconByState = {
            False: "M",
            True: "W"
            }

    def isGhost(icon):
        if isinstance(icon, Ghost):
            return True
        elif icon in Ghost.icons:
            return True
        else:
            return False

    def __init__(self, game, coordinates, icon=""):
        self.coordinates = coordinates
        self.start = coordinates
        self.passedGate = False
        if icon in Ghost.icons:
            self.icon = icon
        if Ghost.icons[icon] is False:
            self.panicLevel = 0
        else:
            self.panicLevel = 50
        self.direction = Direction.UP
        self.game = game
        self.width = game.field.width()
        self.height = game.field.height()

    def panicked(self):
        return (self.panicLevel > 0)

    def panic(self):
            self.panicLevel = 50
            for icon, panic in Ghost.icons.items():
                if panic is True:
                    self.icon = icon

    def triggerEffect(self, pill):
        if pill.score() == 50:
            self.panic()

    def tick(self):
        pacmanLoc = self.coordinates
        pacman = self.game.getPacman()
        if (pacman):
            pacmanLoc = pacman.coordinates

        if (self.panicked() is True):
            self.direction = avoid(self.coordinates, pacmanLoc)
            self.panicLevel -= 1

        front = self.direction
        directions = [front, left(front), right(front)]
        choices = []
        for option in directions:
            if self.clear(self.nextMove(option)):
                choices.append(option)
        if len(choices) > 0:
            self.direction = choices[randint(0, len(choices)-1)]
        elif self.panicked() is False:
            self.direction = opposite(front)

        nextLocation = self.nextMove(self.direction)
        if ((self.panicLevel % 2) == 0) and self.clear(nextLocation):
            self.coordinates = (nextLocation)

        if (self.game.isPacman(self.coordinates)):
            if (self.panicked() is True):
                self.game.killGhost(self)
            else:
                self.game.killPacman()
        if (self.game.isGate(self.coordinates)):
            self.passedGate = True
        self.icon = Ghost.iconByState[self.panicLevel > 0]

    def nextMove(self, direction):
        return wrap(nextLocation(self.coordinates, direction),
                    self.width,
                    self.height)

    def clear(self, coordinates):
        if not self.game.isWall(coordinates):
            return True
        if self.game.isGate(coordinates) and not self.passedGate:
            return True
        return False

    def kill(self):
        self.coordinates = self.start
        self.panicLevel = 0
        self.passedGate = False

    def score(self):
        return 200

    def __str__(self):
        return self.icon
