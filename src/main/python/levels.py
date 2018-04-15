class LevelMaps(object):
    def __init__(self, input_maps):
        self.levels = input_maps.split("SEPARATOR\n")
        try:
            self.max = int(self.levels[0])
        except ValueError:
            self.max = 1

    def max_level(self):
        return self.max

    def get_level(self, level):
        return self.levels[level].rstrip()
