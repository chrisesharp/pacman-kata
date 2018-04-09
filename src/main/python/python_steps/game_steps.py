from behave import given, when, then
from game import Game
from display import Display


#
# Givens
#


@given(u'the game state is')
def step_impl(context):
    game = getattr(context, "game", None)
    if not game:
        context.game = Game(context.text)
    else:
        context.game.inputMap = context.text


@given(u'the game field of {width:d} x {height:d}')
def step_impl(context, width, height):
    game = getattr(context, "game", None)
    if not game:
        context.game = Game()
    context.game.setGameField(width, height)


@given(u'a pacman at {x:d} , {y:d} facing "{direction}"')
def step_impl(context, x, y, direction):
    context.game.setPacman((x, y), direction)


@given(u'walls at the following places')
def step_impl(context):
    for row in context.table:
        icon = row[0]
        x = int(row[1])
        y = int(row[2])
        context.game.addWall((x, y), icon)


@given(u'the score is {score:d}')
def step_impl(context, score):
    context.game.score = score


@given(u'the lives are {lives:d}')
def step_impl(context, lives):
    context.game.lives = lives


@given(u'a display')
def step_impl(context):
    context.display = Display(context.game)


@given(u'a colour display')
def step_impl(context):
    raise NotImplementedError(u'STEP: Given a colour display')


@given(u'the ANSI "{sequence}" sequence is "{hex}"')
def step_impl(context, sequence, hex):
    ansiMap = getattr(context, "ansiMap", None)
    if not ansiMap:
        context.ansiMap = {}
    context.ansiMap[sequence] = hex


@given(u'a game with {levels:d} levels')
def step_impl(context, levels):
    context.game.inputMap = context.text


@given(u'this is level {level:d}')
def step_impl(context, level):
    context.game.setLevel(level)


@given(u'the max level  is {max:d}')
def step_impl(context, max):
    context.game.setMaxLevel(max)


@given(u'the game uses animation')
def step_impl(context):
    context.game.useAnimation()


@given(u'this is the last level')
def step_impl(context):
    context.game.setLevel(1)
    context.game.setMaxLevel(1)


@given(u'initialize the display')
def step_impl(context):
    context.display.init(None)

#
# Whens
#


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
    context.keyboard.keyPressed(key)


@when(u'initialize the display')
def step_impl(context):
    context.display.init(None)


@when(u'we refresh the display with the buffer "{string}"')
def step_impl(context, string):
    output = [ord(c) for c in string]
    sequence = ""
    for b in output:
        sequence += '{:02X}'.format(b)
    context.ansiMap[string] = sequence
    context.display.refresh(string)

#
# Thens
#


@then(u'the game screen is')
def step_impl(context):
    assert context.text == context.game.output


@then(u'the game field should be {x:d} x {y:d}')
def step_impl(context, x, y):
    gamefield = context.game.field
    assert gamefield.width() == x
    assert gamefield.height() == y


@then(u'the player has {lives:d} lives')
def step_impl(context, lives):
    assert context.game.lives == lives


@then(u'the player score is {score:d}')
def step_impl(context, score):
    assert context.game.score == score


@then(u'pacman is at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.pacman.coordinates[0] is x
    assert context.game.pacman.coordinates[1] is y


@then(u'pacman is facing "{direction}"')
def step_impl(context, direction):
    assert context.game.pacman.facing.name == direction


@then(u'ghost is at {x:d} , {y:d}')
def step_impl(context, x, y):
    isGhost = False
    for ghost in context.game.ghosts:
        if (ghost.coordinates[0] is x) and (ghost.coordinates[1] is y):
            isGhost = True
    assert isGhost is True

@then(u'there is a {points:d} point pill at {x:d} , {y:d}')
def step_impl(context, points, x, y):
    pill = context.game.getElement((x, y))
    assert context.game.isPill((x, y)) and pill.score() == points


@then(u'there is a wall at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.isWall((x, y))


@then(u'there is a gate at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.isGate((x, y))


@then(u'there is a force field at {x:d} , {y:d}')
def step_impl(context, x, y):
    assert context.game.isField((x, y))


@then(u'the game lives should be {lives:d}')
def step_impl(context, lives):
    assert context.game.getLives() is lives


@then(u'the game score should be {score:d}')
def step_impl(context, score):
    assert context.game.getScore() is score


@then(u'the display byte stream should be')
def step_impl(context):
    bytestream = ""
    for row in context.table:
        bytestream += context.ansiMap[(row[0])]

    output = [ord(c) for c in context.display.output]
    received = ""
    for b in output:
        received += '{:02X}'.format(b)

    print("expected:" + bytestream)
    print("actual  :" + received)
    assert bytestream == received


@then(u'then pacman goes "{direction}"')
def step_impl(context, direction):
    assert context.game.pacman.facing.name == direction


@then(u'pacman is alive')
def step_impl(context):
    assert context.game.pacman.alive is True


@then(u'pacman is dead')
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
