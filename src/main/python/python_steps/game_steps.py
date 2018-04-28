from behave import given, when, then
from game import Game
from ghost import Ghost
from display import Display
import subprocess
import io


#
# Givens
#
@given(u'the screen column width is {cols:d}')
def step_impl(context, cols):
    context.columns = cols


@given(u'the player has {lives:d} lives')
def step_impl(context, lives):
    context.lives = lives


@given(u'the player score is {score:d}')
def step_impl(context, score):
    context.score = score


@given(u'the command arg "{arg}"')
def step_impl(context, arg):
    context.command.append(arg)


@given(u'the game state is')
def step_impl(context):
    game = getattr(context, "game", None)
    if not game:
        context.game = Game(context.text)
    else:
        context.game.input_map = context.text


@given(u'the game field of {width:d} x {height:d}')
def step_impl(context, width, height):
    game = getattr(context, "game", None)
    if not game:
        context.game = Game()
    context.game.set_field(width, height)


@given(u'a pacman at {x:d} , {y:d} facing "{direction}"')
def step_impl(context, x, y, direction):
    context.game.set_pacman((x, y), direction)


@given(u'a ghost at {x:d} , {y:d}')
def step_impl(context, x, y):
    context.game.add_ghost(Ghost((x, y), "M"))


@given(u'walls at the following places')
def step_impl(context):
    for row in context.table:
        icon = row[0]
        x = int(row[1])
        y = int(row[2])
        context.game.add_wall((x, y), icon)


@given(u'the score is {score:d}')
def step_impl(context, score):
    context.game.score = score


@given(u'the lives are {lives:d}')
def step_impl(context, lives):
    context.game.lives = lives


@given(u'a display')
def step_impl(context):
    buffer = io.BufferedWriter(io.BytesIO())
    context.stream = io.TextIOWrapper(buffer)
    context.display = Display(context.stream)


@given(u'a colour display')
def step_impl(context):
    raise NotImplementedError(u'STEP: Given a colour display')


@given(u'the ANSI "{sequence}" sequence is "{hex}"')
def step_impl(context, sequence, hex):
    ansi_map = getattr(context, "ansi_map", None)
    if not ansi_map:
        context.ansi_map = {}
    context.ansi_map[sequence] = hex


@given(u'a game with {levels:d} levels')
def step_impl(context, levels):
    context.game.input_map = context.text


@given(u'this is level {level:d}')
def step_impl(context, level):
    context.game.set_level(level)


@given(u'the max level  is {max:d}')
def step_impl(context, max):
    context.game.set_max_level(max)


@given(u'the game uses animation')
def step_impl(context):
    context.game.use_animation()


@given(u'this is the last level')
def step_impl(context):
    context.game.set_level(1)
    context.game.set_max_level(1)


@given(u'initialize the display')
def step_impl(context):
    context.display.init(context.game)

#
# Whens
#


@when(u'I run the command with the args')
def step_impl(context):
    received = subprocess.check_output(context.command,
                                       stderr=None,
                                       universal_newlines=True)
    received = received.replace(Display.RST, "")
    received = received.replace(Display.CLR, "")
    received = received.replace(Display.REVON, "")
    received = received.replace(Display.REVOFF, "")
    received = received.replace(Display.REV, "")
    received = received.replace(Display.BLINK, "")
    received = received.lstrip("\n")
    context.output = received.rstrip("\n")

@when(u'we render the status line')
def step_impl(context):
    composite = Game.render_status(context.lives,
                                   context.score,
                                   context.columns)
    context.output = composite["video"]

@when(u'we render the game field')
def step_impl(context):
    composite = context.game.field.render()
    context.output = composite["video"]

@when(u'we parse the state')
def step_impl(context):
    context.output = context.game.parse()


@when(u'we render the game')
def step_impl(context):
    context.game.render()


@when(u'we play {turns:d} turns')
def step_impl(context, turns):
    for x in range(turns):
        context.game.tick()


@when(u'we play {turns:d} turn')
def step_impl(context, turns):
    for x in range(turns):
        context.game.tick()


@when(u'the game refreshes the display with the buffer "{content}"')
def step_impl(context, content):
    raise NotImplementedError(u'STEP: When the game refreshes the display \
                              with the buffer "' + content + '"')


@when(u'the display renders the icon "{icon}" in yellow and refreshes')
def step_impl(context, icon):
    raise NotImplementedError(u'STEP: When the display renders the icon "' +
                              icon + '" in yellow and refreshes')


@when(u'the player presses "{key}"')
def step_impl(context, key):
    context.keyboard.key_pressed(key)


@when(u'initialize the display')
def step_impl(context):
    context.display.init(context.game)


@when(u'we refresh the display with the buffer "{string}"')
def step_impl(context, string):
    output = [ord(c) for c in string]
    sequence = ""
    for b in output:
        sequence += '{:02X}'.format(b)
    context.ansi_map[string] = sequence
    context.display.refresh(string)

#
# Thens
#


@then(u'I should get the following output')
def step_impl(context):
    print("===")
    print(context.output)
    print("===")
    assert context.text == context.output


@then(u'the game screen should be')
def step_impl(context):
    assert context.text == context.game.output


@then(u'the game field should be {x:d} x {y:d}')
def step_impl(context, x, y):
    gamefield = context.game.field
    assert gamefield.width() == x
    assert gamefield.height() == y


@then(u'the player should have {lives:d} lives')
def step_impl(context, lives):
    assert context.game.lives == lives


@then(u'the score should be {score:d}')
def step_impl(context, score):
    assert context.game.score == score


@then(u'pacman should be at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.pacman.coordinates[0] is x
    assert context.game.pacman.coordinates[1] is y


@then(u'pacman should be facing "{direction}"')
def step_impl(context, direction):
    assert context.game.pacman.facing.name == direction


@then(u'ghost should be at {x:d} , {y:d}')
def step_impl(context, x, y):
    is_ghost = False
    for ghost in context.game.ghosts:
        if (ghost.coordinates[0] is x) and (ghost.coordinates[1] is y):
            is_ghost = True
    assert is_ghost is True


@then(u'there should be a {points:d} point pill at {x:d} , {y:d}')
def step_impl(context, points, x, y):
    pill = context.game.get_element((x, y))
    assert context.game.is_pill((x, y)) and pill.score() == points


@then(u'there should be a wall at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.is_wall((x, y))


@then(u'there should be a gate at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.is_gate((x, y))


@then(u'there should be a force field at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.is_field((x, y))


@then(u'the game lives should be {lives:d}')
def step_impl(context, lives):
    assert context.game.get_lives() is lives


@then(u'the game score should be {score:d}')
def step_impl(context, score):
    assert context.game.get_score() is score


@then(u'the display byte stream should be')
def step_impl(context):
    bytestream = ""
    for row in context.table:
        bytestream += context.ansi_map[(row[0])]

    output = [ord(c) for c in context.display.output]
    received = ""
    for b in output:
        received += '{:02X}'.format(b)

    print("expected:" + bytestream)
    print("actual  :" + received)
    assert bytestream == received


@then(u'then pacman should go "{direction}"')
def step_impl(context, direction):
    assert context.game.pacman.facing.name == direction


@then(u'pacman should be alive')
def step_impl(context):
    assert context.game.pacman.alive is True


@then(u'pacman should be dead')
def step_impl(context):
    assert context.game.pacman.alive is False


@then(u'the game dimensions should equal the display dimensions')
def step_impl(context):
    assert context.game.field.width() is context.display.width()
    assert context.game.field.height() is context.display.height()


@then(u'ghost at {x:d} , {y:d} should be calm')
def step_impl(context, x, y):
    assert context.game.field.get((x, y)).panicked() is False


@then(u'ghost at {x:d} , {y:d} should be panicked')
def step_impl(context, x, y):
    assert context.game.field.get((x, y)).panicked() is True
