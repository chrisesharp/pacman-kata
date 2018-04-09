class LevelMaps(object):
    def __init__(self, inputMaps):
        self.levels = inputMaps.split("SEPARATOR\n")
        try:
            self.max = int(self.levels[0])
        except ValueError:
            self.max = 1

    def maxLevel(self):
        return self.max

    def getLevel(self, level):
        return self.levels[level].rstrip()
