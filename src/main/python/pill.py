class Pill(object):
    icons = {".": 10,
             "o": 50}

    def isPill(icon):
        if type(icon) is Pill:
            return True
        elif icon in Pill.icons:
            return True
        else:
            return False

    def __init__(self, coordinates, icon):
        self.coordinates = coordinates
        self.icon = icon
        self.points = Pill.icons[icon]

    def __str__(self):
        return self.icon

    def score(self):
        return self.points
