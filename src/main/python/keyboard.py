from direction import Direction


class Keyboard(object):
    def __init__(self, game):
        self.game = game

    def keyPressed(self, key):
        if (key == 'j'):
            self.game.move(Direction.LEFT)
        if (key == 'i'):
            self.game.move(Direction.UP)
        if (key == 'l'):
            self.game.move(Direction.RIGHT)
        if (key == 'm'):
            self.game.move(Direction.DOWN)

    def tick(self):
        pass
