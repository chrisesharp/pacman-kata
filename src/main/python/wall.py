from game_element import GameElement
from colour import Colour


class Wall(GameElement):
    gates = {"=": Colour.WHITE,
             "━": Colour.WHITE}
    field = {"#": Colour.BLACK,
             "┃": Colour.BLACK}
    walls = {"|": Colour.WHITE,
             "-": Colour.WHITE,
             "+": Colour.WHITE,
             "║": Colour.WHITE,
             "═": Colour.WHITE,
             "╚": Colour.WHITE,
             "╝": Colour.WHITE,
             "╔": Colour.WHITE,
             "╗": Colour.WHITE}
    icons = dict(walls)
    icons.update(gates)
    icons.update(field)

    def __init__(self, coordinates, icon):
        super(Wall, self).__init__(coordinates, icon)
        self.colour = Wall.icons[icon]

    def is_wall(icon):
        if isinstance(icon, Wall) or icon in Wall.icons:
            return True
        return False

    def is_gate(icon):
        if isinstance(icon, Wall) and str(icon) in Wall.gates:
            return True
        return False

    def is_field(icon):
        if isinstance(icon, Wall) and str(icon) in Wall.field:
            return True
        return False

    def get_element(coords, icon):
        if (Wall.is_wall(icon)):
            return Wall(coords, icon)

    def add_to_game(self, game):
        game.add_wall(self.coordinates, self.icon)
