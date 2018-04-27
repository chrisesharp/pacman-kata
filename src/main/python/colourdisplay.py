from display import Display
from colour import Colour

DEFAULT = Display.ESC + str(Colour.WHITE) + "m"
DEFAULT += Display.ESC + str(Colour.BLACK_BG) + "m"


class ColourDisplay(Display):
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

    def refresh(self, video_output, colour_out):
        self.write_stream(Display.CLR)
        self.write_stream(DEFAULT)
        for i in range(len(video_output)):
            if colour_out[i] > 0:
                self.write_stream(Display.ESC + str(colour_out[i]) + "m")
            self.write_stream(video_output[i])
        self.write_stream(Display.RST)
        self.write_stream(DEFAULT)
        self.write_stream(Display.RST)
        self.write_stream("\n")
        print(self.output)

    def write_stream(self, data):
        self.output += data
