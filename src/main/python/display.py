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
        self.output = ""
        self.outstream = None

    def init(self, stream):
        self.outstream = stream
        if self.game.field:
            self.rows = self.game.field.height()
            self.cols = self.game.field.width()

    def width(self):
        return self.cols

    def height(self):
        return self.rows

    def refresh(self, output):
        self.write_stream(Display.CLR)
        self.write_stream(output)
        self.write_stream(Display.RST)
        self.write_stream("\n")
        print(self.output)

    def write_stream(self, data):
        self.output += data
