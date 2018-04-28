from game_element import GameElement
from colour import Colour


class Pill(GameElement):
    icons = {".": {"points": 10, "colour": Colour.WHITE},
             "o": {"points": 50, "colour": Colour.BLINK}}

    def is_pill(icon):
        if isinstance(icon, Pill) or icon in Pill.icons:
            return True
        return False

    def get_element(coords, icon):
        if (Pill.is_pill(icon)):
            return Pill(coords, icon)

    def __init__(self, coordinates, icon):
        super(Pill, self).__init__(coordinates, icon)
        config = Pill.icons[icon]
        self.points = config["points"]
        self.colour = config["colour"]

    def score(self):
        return self.points

    def add_to_game(self, game):
        game.add_pill(self)
