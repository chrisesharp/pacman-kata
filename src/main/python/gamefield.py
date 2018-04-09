class GameField:
    def __init__(self, width, height):
        self.columns = width
        self.rows = height
        self.field = {}
        for y in range(self.rows):
            for x in range(self.columns):
                self.add((x, y), " ")

    def add(self, coordinates, element):
        self.field[coordinates[0], coordinates[1]] = element

    def get(self, coordinates):
        return self.field[coordinates[0], coordinates[1]]

    def width(self):
        return self.columns

    def height(self):
        return self.rows

    def render(self):
        screen = ""
        for y in range(self.rows):
            for x in range(self.columns):
                screen += str(self.field[(x, y)])
            if y < self.rows-1:
                screen += "\n"
        return screen
