@X
Feature: parsing a simple maze
As a game engine
I can parse an initial game state as a screen shot
So that I can initialize game elements

As a developer
I can provide screen representations of a game state
So that I can more easily provide test inputs

Scenario: a 6x5 game field
Given the game state is
"""
3   10
+----+
|    |
|    |
|    |
+----+
"""
When we parse the state
Then the game field should be 6 x 5
And the player has 3 lives
And the player score is 10

Scenario: a 6x4 game field
Given the game state is
"""
2  100
+----+
|    |
|    |
+----+
"""
When we parse the state
Then the game field should be 6 x 4
And the player has 2 lives
And the player score is 100

Scenario: a pacman facing right
Given the game state is
"""
2  100
+----+
|<   |
|    |
+----+
"""
When we parse the state
Then pacman is at 1 , 1
And pacman is facing "RIGHT"

