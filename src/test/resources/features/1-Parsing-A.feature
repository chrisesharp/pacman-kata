@1
@test
Feature: initial game field parsing
  As a game engine
  I can parse an initial game state as a screen shot
  So that I can initialize all relevant game elements

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs

Scenario: a 3x3 game field
Given the game state is
"""
0 0
+-+
| |
+-+
"""
When we parse the state
Then the game field should be 3 x 3

Scenario: a 5x5 game field
Given the game state is
"""
2  10
+---+
|   |
|   |
|   |
+---+
"""
When we parse the state
Then the game field should be 5 x 5

Scenario: a 5x5 game field with status
Given the game state is
"""
2  10
+---+
|   |
|   |
|   |
+---+
"""
When we parse the state
Then the game field should be 5 x 5
And the player has 2 lives
And the player score is 10
