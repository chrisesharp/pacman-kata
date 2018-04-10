from direction import Direction
import tty
import sys
import termios
import threading


class Keyboard(object):
    def __init__(self, game):
        self.game = game
        self.orig_settings = termios.tcgetattr(sys.stdin)
        tty.setcbreak(sys.stdin)
        threading.Thread(group=None, target=self.listen).start()

    def keyPressed(self, key):
        if (key == 'j'):
            self.game.move(Direction.LEFT)
        if (key == 'i'):
            self.game.move(Direction.UP)
        if (key == 'l'):
            self.game.move(Direction.RIGHT)
        if (key == 'm'):
            self.game.move(Direction.DOWN)

    def listen(self):
        key = None
        while ((self.game.gameOver is False) and (key != chr(27))):
            key = sys.stdin.read(1)[0]
            if (key == chr(106)):
                self.keyPressed("j")
            if (key == chr(105)):
                self.keyPressed("i")
            if (key == chr(108)):
                self.keyPressed("l")
            if (key == chr(109)):
                self.keyPressed("m")
        self.game.gameOver = True
        self.close()

    def close(self):
        termios.tcsetattr(sys.stdin, termios.TCSADRAIN, self.orig_settings)
