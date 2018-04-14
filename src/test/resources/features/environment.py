from behave import fixture
from game import Game
from keyboard import Keyboard


@fixture
def before_scenario(context, scenario):
    game = getattr(context, "game", None)
    if not game:
        context.game = Game(None)
    context.keyboard = Keyboard(context.game)
    context.game.setController(context.keyboard)
    context.command = ["python3", "game.py"]
    context.command_output = ""
