class Wall(object):
    gates = {"=": 0}
    field = {"#": 0}
    walls = {"|": 1,
             "-": 1,
             "+": 1}
    icons = dict(walls)
    icons.update(gates)
    icons.update(field)

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

    def __init__(self, coordinates, icon):
        self.coordinates = coordinates
        self.icon = icon

    def __str__(self):
        return self.icon

    def add_to_game(self, game):
        game.add_wall(self.coordinates, self.icon)
