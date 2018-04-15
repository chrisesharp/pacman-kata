from direction import avoid, next_location, wrap, left, right, opposite
from direction import Direction
from random import randint


class Ghost(object):
    icons = {
            "M": False,
            "W": True
            }
    icon_state = {
            False: "M",
            True: "W"
            }

    def is_ghost(icon):
        if isinstance(icon, Ghost) or icon in Ghost.icons:
            return True
        return False

    def get_element(coords, icon):
        if (Ghost.is_ghost(icon)):
            return Ghost(coords, icon)

    def __init__(self, coordinates, icon=""):
        self.coordinates = coordinates
        self.start = coordinates
        self.passed_gate = False
        if icon in Ghost.icons:
            self.icon = icon
        if Ghost.icons[icon] is False:
            self.panic_level = 0
        else:
            self.panic_level = 50
        self.direction = Direction.UP

    def add_to_game(self, game):
        self.game = game
        self.width = game.field.width()
        self.height = game.field.height()
        game.add_ghost(self)

    def panicked(self):
        return (self.panic_level > 0)

    def panic(self):
            self.panic_level = 50
            for icon, panic in Ghost.icons.items():
                if panic is True:
                    self.icon = icon

    def trigger_effect(self, pill):
        if pill.score() == 50:
            self.panic()

    def tick(self):
        pacman_loc = self.coordinates
        pacman = self.game.get_pacman()
        if (pacman):
            pacman_loc = pacman.coordinates

        if (self.panicked() is True):
            self.direction = avoid(self.coordinates, pacman_loc)
            self.panic_level -= 1

        front = self.direction
        directions = [front, left(front), right(front)]
        choices = []
        for option in directions:
            if self.clear(self.next_move(option)):
                choices.append(option)
        if len(choices) > 0:
            self.direction = choices[randint(0, len(choices)-1)]
        elif self.panicked() is False:
            self.direction = opposite(front)

        next_location = self.next_move(self.direction)
        if ((self.panic_level % 2) == 0) and self.clear(next_location):
            self.coordinates = (next_location)

        if (self.game.is_pacman(self.coordinates)):
            if (self.panicked() is True):
                self.game.kill_ghost(self)
            else:
                self.game.kill_pacman()
        if (self.game.is_gate(self.coordinates)):
            self.passed_gate = True
        self.icon = Ghost.icon_state[self.panic_level > 0]

    def next_move(self, direction):
        return wrap(next_location(self.coordinates, direction),
                    self.width,
                    self.height)

    def clear(self, coordinates):
        if not self.game.is_wall(coordinates):
            return True
        if self.game.is_gate(coordinates) and not self.passed_gate:
            return True
        return False

    def kill(self):
        self.coordinates = self.start
        self.panic_level = 0
        self.passed_gate = False

    def score(self):
        return 200

    def __str__(self):
        return self.icon
