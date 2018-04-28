from direction import Direction
import tty
import sys
import termios
import threading


class Keyboard(object):
    keymap = {"j": Direction.LEFT,
              "i": Direction.UP,
              "l": Direction.RIGHT,
              "m": Direction.DOWN}

    def __init__(self, game):
        self.game = game
        self.orig_settings = termios.tcgetattr(sys.stdin)

    def init(self):
        tty.setcbreak(sys.stdin)
        threading.Thread(group=None, target=self.listen).start()

    def key_pressed(self, key):
        self.game.move(self.keymap[key])

    def listen(self):
        key = None
        while (self.game.game_over is False and key != chr(27)):
            key = sys.stdin.read(1)
            if key in self.keymap:
                self.key_pressed(key)
        self.game.game_over = True
        self.close()

    def close(self):
        termios.tcsetattr(sys.stdin, termios.TCSADRAIN, self.orig_settings)
