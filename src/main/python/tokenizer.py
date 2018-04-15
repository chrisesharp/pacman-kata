from pacman import Pacman
from ghost import Ghost
from pill import Pill
from wall import Wall


def get_element(coords, icon):
    funcs = [Pacman.get_element,
             Ghost.get_element,
             Pill.get_element,
             Wall.get_element]
    for f in funcs:
        element = f(coords, icon)
        if (element):
            return element
    return None
