class GameElement(object):
    def __init__(self, coordinates, icon):
        self.coordinates = coordinates
        self.icon = icon
        self.colour = 0

    def get_colour(self):
        return self.colour

    def __str__(self):
        return self.icon
