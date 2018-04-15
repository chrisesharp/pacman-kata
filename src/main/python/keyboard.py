from direction import Direction
import tty
import sys
import termios
import threading


class Keyboard(object):
    def __init__(self, game):
        self.game = game
        self.orig_settings = termios.tcgetattr(sys.stdin)

    def init(self):
        tty.setcbreak(sys.stdin)
        threading.Thread(group=None, target=self.listen).start()

    def key_pressed(self, key):
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
        while ((self.game.game_over is False) and (key != chr(27))):
            key = sys.stdin.read(1)[0]
            if (key == chr(106)):
                self.key_pressed("j")
            if (key == chr(105)):
                self.key_pressed("i")
            if (key == chr(108)):
                self.key_pressed("l")
            if (key == chr(109)):
                self.key_pressed("m")
        self.game.game_over = True
        self.close()

    def close(self):
        termios.tcsetattr(sys.stdin, termios.TCSANOW, self.orig_settings)
