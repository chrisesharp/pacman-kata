from time import sleep


class Display(object):
    ESC = "\u001B["
    CLR = "\u001B[H" + "\u001B[2J" + "\u001B[1m"
    RST = "\u001B[0m"
    REVON = "\u001B[?5h"
    REVOFF = "\u001B[?5l"
    BLINK = "\u001B[5m"
    REV = "\u001B[7m"

    def __init__(self, stream):
        self.outstream = stream
        self.rows = None
        self.cols = None
        self.output = ""

    def init(self, game):
        self.game = game
        if self.game.field:
            self.rows = self.game.field.height()
            self.cols = self.game.field.width()

    def width(self):
        return self.cols

    def height(self):
        return self.rows

    def refresh(self, video_output, colour_out=None):
        self.output = ""
        self.write_stream(Display.CLR)
        self.write_stream(video_output)
        self.write_stream(Display.RST)
        self.write_stream("\n")

    def write_stream(self, data):
        self.output += data
        self.outstream.buffer.write(data.encode("utf-8"))
        self.outstream.flush()

    def flash(self):
        self.write_stream(Display.REVON)
        sleep(0.1)
        self.write_stream(Display.REVOFF)
