@B
Feature: Multiple maps for different levels
  As a game engine
  I can get a map associated with a level
  So that I can display it correctly

Scenario: Different maps per level
Given a game with 2 levels
"""
2
SEPARATOR
?     ?
+-----+
|<.o. |
|     |
|     |
|     |
+-----+
SEPARATOR
?     ?
+-----+
|     |
|     |
|<.o. |
|     |
+-----+
"""
And this is level 1
And the max level  is 2
When we parse the state
And we play 3 turns
And we render the game
Then the game screen should be
"""
3    70
+-----+
|     |
|     |
|<.o. |
|     |
+-----+
"""

