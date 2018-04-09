from direction import next, wrap, Direction


class Pacman:
    icons = {
            "<": Direction.RIGHT,
            ">": Direction.LEFT,
            "V": Direction.UP,
            "Λ": Direction.DOWN,
            "*": "DEAD"
            }

    frame = {
            Direction.RIGHT: ["<", "{"],
            Direction.LEFT: [">", "}"],
            Direction.UP: ["V", "v"],
            Direction.DOWN: ["Λ", "^"]
            }

    def isPacman(icon):
        if type(icon) is Pacman:
            return True
        elif icon in Pacman.icons:
            return True
        else:
            return False

    def __init__(self, game, coordinates, icon=""):
        self.coordinates = coordinates
        self.start = coordinates
        self.facing = None
        if icon in Pacman.icons:
            if Pacman.icons[icon] == "DEAD":
                self.alive = False
            else:
                self.facing = Pacman.icons[icon]
                self.alive = True
        self.icon = icon
        self.game = game
        self.width = game.field.width()
        self.height = game.field.height()
        self.frame = 0

    def setDirection(self, facing):
        if type(facing) is str:
            for name, item in Direction.__members__.items():
                if name == facing:
                    self.facing = item
        else:
            self.facing = facing
        self.icon = Pacman.frame[self.facing][self.frame]

    def tick(self):
        print("tick")
        if self.clear(self.nextMove(self.facing)):
            self.coordinates = (self.nextMove(self.facing))
            if self.animated:
                self.frame = (self.frame + 1) % 2
            self.icon = Pacman.frame[self.facing][self.frame]
        if self.game.isPill(self.coordinates):
            self.game.eatPill(self.coordinates)
        if self.game.isGhost(self.coordinates):
            self.game.killGhost(self.game.getElement(self.coordinates))

    def clear(self, coordinates):
        if not self.game.isWall(coordinates):
            return True
        if self.game.isField(coordinates):
            return True
        return False

    def move(self, direction):
        if self.clear(self.nextMove(direction)):
            self.setDirection(direction)

    def nextMove(self, facing):
        return wrap(next(self.coordinates, facing),
                    self.width,
                    self.height)

    def kill(self):
        self.alive = False
        for icon, state in Pacman.icons.items():
            if state == "DEAD":
                self.icon = icon

    def restart(self):
        self.coordinates = self.start
        self.alive = True
        self.setDirection(self.facing)

    def useAnimation(self, animated):
        self.animated = animated

    def __str__(self):
        return self.icon
