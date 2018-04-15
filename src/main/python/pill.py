class Pill(object):
    icons = {".": 10,
             "o": 50}

    def is_pill(icon):
        if isinstance(icon, Pill) or icon in Pill.icons:
            return True
        return False

    def get_element(coords, icon):
        if (Pill.is_pill(icon)):
            return Pill(coords, icon)

    def __init__(self, coordinates, icon):
        self.coordinates = coordinates
        self.icon = icon
        self.points = Pill.icons[icon]

    def __str__(self):
        return self.icon

    def score(self):
        return self.points

    def add_to_game(self, game):
        game.add_pill(self)
