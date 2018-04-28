from direction import avoid, next_location, wrap, left, right, opposite
from direction import Direction
from random import randint
from game_element import GameElement
from colour import Colour


class Ghost(GameElement):
    icons = {
            "M": False,
            "W": True
            }
    icon_state = {
            False: {"icon": "M", "colour": None},
            True: {"icon": "W", "colour": Colour.BLUE}
            }

    colours = [Colour.RED,
               Colour.GREEN,
               Colour.PURPLE,
               Colour.CYAN]

    colour = 0

    def is_ghost(icon):
        if isinstance(icon, Ghost) or icon in Ghost.icons:
            return True
        return False

    def get_element(coords, icon):
        if (Ghost.is_ghost(icon)):
            return Ghost(coords, icon)

    def __init__(self, coordinates, icon=""):
        super(Ghost, self).__init__(coordinates, icon)
        self.start = coordinates
        self.passed_gate = False
        self.orig_colour = Ghost.colours[Ghost.colour]
        Ghost.colour = (Ghost.colour + 1) % len(Ghost.colours)

        if icon in Ghost.icons:
            self.icon = icon
        self.panic_level = 0 if Ghost.icons[icon] is False else 50
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
        self.manage_panic()
        self.choose_direction()
        if (self.game.is_pacman(self.coordinates)):
            if (self.panicked() is True):
                self.game.kill_ghost(self)
            else:
                self.game.kill_pacman()
        if (self.game.is_gate(self.coordinates)):
            self.passed_gate = True
        image = Ghost.icon_state[self.panic_level > 0]
        self.icon = image["icon"]
        this_colour = image["colour"]
        self.colour = self.orig_colour if this_colour is None else this_colour

    def manage_panic(self):
        if (self.panicked() is True):
            pacman_loc = self.coordinates
            pacman = self.game.get_pacman()
            if (pacman):
                pacman_loc = pacman.coordinates
            self.direction = avoid(self.coordinates, pacman_loc)
            self.panic_level -= 1

    def choose_direction(self):
        front = self.direction
        choices = [front, left(front), right(front)]
        options = self.find_options(choices)
        if len(options) > 0:
            self.random_choice(options)
        else:
            self.no_option()
        self.move()

    def find_options(self, choices):
        options = []
        for option in choices:
            if self.clear(self.next_step(option)):
                options.append(option)
        return options

    def random_choice(self, options):
        self.direction = options[randint(0, len(options)-1)]

    def no_option(self):
        if self.panicked() is False:
            self.direction = opposite(self.direction)

    def move(self):
        next_location = self.next_step(self.direction)
        if ((self.panic_level % 2) == 0) and self.clear(next_location):
            self.coordinates = (next_location)

    def next_step(self, direction):
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
