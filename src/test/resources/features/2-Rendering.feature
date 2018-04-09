@2
Feature: Rendering
  As a game engine
  I can render the state of all game elements
  So that I can display it to the player

  As a developer
  I can render the state of all game elements
  So that I can more easily assess test results

Scenario: pacman on 1x1 screen
Given the game state is
"""
3 0
+-+
|>|
+-+
"""
When we parse the state
And we render the game
Then the game screen is
"""
3 0
+-+
|>|
+-+
"""

Scenario: everything on the screen
Given the game state is
"""
3     160
+-------+
|<..o...|
| +-+ + |
# | | | #
| + +=+ |
|  M    |
+-------+
"""
When we parse the state
And we render the game
Then the game screen is
"""
3     160
+-------+
|<..o...|
| +-+ + |
# | | | #
| + +=+ |
|  M    |
+-------+
"""
