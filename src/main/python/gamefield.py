from game_element import GameElement


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
        colour = []
        video = ""
        for y in range(self.rows):
            for x in range(self.columns):
                element = self.field[(x, y)]
                if isinstance(element, GameElement):
                    video += str(element)
                    colour.append(element.get_colour())
                else:
                    video += element
                    colour.append(0)
            if y < self.rows-1:
                video += "\n"
                colour.append(0)
        return {"video": video, "colour": colour}
