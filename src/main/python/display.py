class Display(object):
    ESC = "\u001B["
    CLR = "\u001B[H" + "\u001B[2J" + "\u001B[1m"
    RST = "\u001B[0m"
    REVON = "\u001B[?5h"
    REVOFF = "\u001B[?5l"
    BLINK = "\u001B[5m"
    REV = "\u001B[7m"

    def __init__(self, game):
        self.game = game
        self.rows = None
        self.cols = None
        self.output = None

    def init(self, stream):
        self.output = stream
        if self.game.field:
            self.rows = self.game.field.height()
            self.cols = self.game.field.width()

    def width(self):
        return self.cols

    def height(self):
        return self.rows

    def refresh(self, output):
        self.output = Display.CLR
        self.output += output
        self.output += Display.RST
        self.output += "\n"
        print(self.output)
