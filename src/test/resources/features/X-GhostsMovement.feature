@X
Feature: ghosts movement
  As a game engine
  I can determine ghost movement
  So that they can move automatically
  
  Scenario: ghost moves along a passage 
  Given the game state is
  """
  3     160
  +-+-+---+
  | | |   |
  |.+ +-+ |
  |.| |<| |
  |.+ +-+ |
  |M|     |
  +-------+
  """
  When we parse the state
  And we play 4 turns
  And we render the game
  Then the game screen should be
  """
  3     160
  +-+-+---+
  |M| |   |
  |.+ +-+ |
  |.| |<| |
  |.+ +-+ |
  | |     |
  +-------+
  """
  
  Scenario: ghost follows maze to right
  Given the game state is
  """
  3     160
  +---+---+
  |   |   |
  |.+ +-+ |
  |.| |<| |
  |.+ +-+ |
  |M|     |
  +-------+
  """
  When we parse the state
  And we play 11 turns
  And we render the game
  Then the game screen should be
  """
  3     160
  +---+---+
  |   |   |
  |.+ +-+ |
  |.| |<| |
  |.+ +-+ |
  | | M   |
  +-------+
  """

  Scenario: ghost follows maze to left
  Given the game state is
  """
  3     160
  +---+---+
  |   |   |
  | + + + |
  | | |M| |
  |-+ +-+ |
  |<|     |
  +-------+
  """
  When we parse the state
  And we play 20 turns
  And we render the game
  Then the game screen should be
  """
  3     160
  +---+---+
  |   |   |
  | + + + |
  |M| | | |
  |-+ +-+ |
  |<|     |
  +-------+
  """

