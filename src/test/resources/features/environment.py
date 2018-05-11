from behave import fixture
from game import Game
from keyboard import Keyboard


@fixture
def before_scenario(context, scenario):
    context.game = Game(None)
    context.keyboard = Keyboard(context.game)
    context.game.set_controller(context.keyboard)
    context.command = ["python3", "game.py"]
    context.command_output = ""


@fixture
def after_scenario(context, scenario):
    context.game = None
