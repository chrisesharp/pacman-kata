@9
Feature: Multiple levels and clearing a Level
  As a game engine
  I can recognise when all pills have been eaten
  So that I can trigger a suitable event for that situation

Scenario: Game over when all pills eaten
Given the game state is
"""
3     0
+-----+
|<.o. |
|     |
|     |
|     |
+-----+
"""
And this is the last level
When we parse the state
And we play 5 turns
And we render the game
Then the game screen should be
"""
3    70
+-----+
|GAME |
|OVER |
|     |
|     |
+-----+
"""

Scenario: Next level when all pills eaten
Given the game state is
"""
?     ?
+-----+
|<.o. |
|     |
|     |
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
|<.o. |
|     |
|     |
|     |
+-----+
"""
