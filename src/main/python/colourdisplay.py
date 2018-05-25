from display import Display
from colour import Colour


class ColourDisplay(Display):
    DEFAULT = Display.ESC + str(Colour.WHITE) + "m"
    DEFAULT += Display.ESC + str(Colour.BLACK_BG) + "m"

    def refresh(self, video_output, colour_out=None):
        self.output = ""
        self.write_stream(Display.CLR)
        self.write_stream(ColourDisplay.DEFAULT)
        for index, video_data in enumerate(video_output):
            if colour_out and colour_out[index] > 0:
                self.write_stream(Display.ESC + str(colour_out[index]) + "m")
                self.write_stream(video_data)
                self.write_stream(Display.RST)
                self.write_stream(ColourDisplay.DEFAULT)
            else:
                self.write_stream(video_data)
        self.write_stream(Display.RST)
        self.write_stream("\n")
