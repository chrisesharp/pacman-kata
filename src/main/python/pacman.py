from direction import next_location, wrap
from direction import Direction
from game_element import GameElement
from colour import Colour


class Pacman(GameElement):
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

    def is_pacman(icon):
        if isinstance(icon, Pacman) or icon in Pacman.icons:
            return True
        return False

    def get_element(coords, icon):
        if (Pacman.is_pacman(icon)):
            return Pacman(coords, icon)

    def add_to_game(self, game):
        self.game = game
        self.width = game.field.width()
        self.height = game.field.height()
        self.use_animation(game.animation)
        game.add_pacman(self)

    def __init__(self, coordinates, icon=""):
        super(Pacman, self).__init__(coordinates, icon)
        self.start = coordinates
        self.facing = None
        if icon in Pacman.icons:
            if Pacman.icons[icon] == "DEAD":
                self.alive = False
            else:
                self.facing = Pacman.icons[icon]
                self.alive = True
        self.icon = icon
        self.frame = 0
        self.colour = Colour.YELLOW

    def set_direction(self, facing):
        if isinstance(facing, str):
            for name, item in Direction.__members__.items():
                if name == facing:
                    self.facing = item
        else:
            self.facing = facing
        self.icon = Pacman.frame[self.facing][self.frame]

    def tick(self):
        if self.clear(self.next_move(self.facing)):
            self.coordinates = (self.next_move(self.facing))
            self.animate()
        self.check_collisions()

    def animate(self):
        if self.animated:
            self.frame = (self.frame + 1) % 2
        self.icon = Pacman.frame[self.facing][self.frame]

    def check_collisions(self):
        if self.game.is_pill(self.coordinates):
            self.game.eat_pill(self.coordinates)
        elif self.game.is_ghost(self.coordinates):
            self.game.kill_ghost(self.coordinates)

    def clear(self, coordinates):
        if not self.game.is_wall(coordinates):
            return True
        if self.game.is_field(coordinates):
            return True
        return False

    def move(self, direction):
        if self.clear(self.next_move(direction)):
            self.set_direction(direction)

    def next_move(self, facing):
        return wrap(next_location(self.coordinates, facing),
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
        self.set_direction(self.facing)

    def use_animation(self, animated):
        self.animated = animated
