from display import Display
from colour import Colour


class ColourDisplay(Display):
    DEFAULT = Display.ESC + str(Colour.WHITE) + "m"
    DEFAULT += Display.ESC + str(Colour.BLACK_BG) + "m"

    def refresh(self, video_output, colour_out):
        self.output = ""
        self.write_stream(Display.CLR)
        self.write_stream(ColourDisplay.DEFAULT)
        for i in range(len(video_output)):
            if colour_out[i] > 0:
                self.write_stream(Display.ESC + str(colour_out[i]) + "m")
                self.write_stream(video_output[i])
                self.write_stream(Display.RST)
                self.write_stream(ColourDisplay.DEFAULT)
            else:
                self.write_stream(video_output[i])
        self.write_stream(Display.RST)
        self.write_stream("\n")
