class Wall(object):
    gates = {"=": 0}
    field = {"#": 0}
    walls = {"|": 1,
             "-": 1,
             "+": 1}
    icons = dict(walls)
    icons.update(gates)
    icons.update(field)

    def isWall(icon):
        if isinstance(icon, Wall):
            return True
        elif icon in Wall.icons:
            return True
        else:
            return False

    def isGate(icon):
        if isinstance(icon, Wall) and str(icon) in Wall.gates:
            return True
        if icon in Wall.gates:
            return True
        return False

    def isField(icon):
        if isinstance(icon, Wall) and str(icon) in Wall.field:
            return True
        if icon in Wall.gates:
            return True
        return False

    def __init__(self, coordinates, icon):
        self.coordinates = coordinates
        self.icon = icon

    def __str__(self):
        return self.icon
