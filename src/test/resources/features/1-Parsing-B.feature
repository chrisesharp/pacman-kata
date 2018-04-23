@1
Feature: initial game field parsing - pacman
  As a game engine
  I can parse an initial game state as a screen shot
  So that I can initialize all relevant game elements

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs

Scenario: pacman is at 1,1 on a 3x3 game field
Given the game state is
"""
3 0
+-+
|>|
+-+
"""
When we parse the state
Then the game field should be 3 x 3
And pacman should be at 1 , 1


Scenario: pacman is at 2,2 on a 5x5 game field
Given the game state is
"""
3   0
+---+
|   |
| > |
|   |
+---+
"""
When we parse the state
Then pacman should be at 2 , 2
And the player should have 3 lives
And the score should be 0

Scenario: pacman is at 1,1 on a 3x3 game field and pacman faces left
Given the game state is
"""
3 0
+-+
|>|
+-+
"""
When we parse the state
Then pacman should be at 1 , 1
And pacman should be facing "LEFT"

Scenario: pacman is at 1,1 on a 3x3 game field and pacman faces right
Given the game state is
"""
3 0
+-+
|<|
+-+
"""
When we parse the state
Then pacman should be at 1 , 1
And pacman should be facing "RIGHT"

Scenario: pacman facing right at 2,2 on a 5x5 game field
Given the game state is
"""
2  10
+---+
|   |
| < |
|   |
+---+
"""
When we parse the state
Then pacman should be at 2 , 2
And the player should have 2 lives
And the score should be 10

Scenario: pacman facing up at 2,2 on a 5x5 game field
Given the game state is
"""
2  10
+---+
|   |
| V |
|   |
+---+
"""
When we parse the state
Then pacman should be at 2 , 2
And the player should have 2 lives
And the score should be 10
And pacman should be facing "UP"

Scenario: pacman facing down at 2,2 on a 5x5 game field
Given the game state is
"""
2  10
+---+
|   |
| Λ |
|   |
+---+
"""
When we parse the state
Then pacman should be at 2 , 2
And the player should have 2 lives
And the score should be 10
And pacman should be facing "DOWN"

Scenario: pacman is at 1,1 on a 3x3 game field and pacman is dead
Given the game state is
"""
3 0
+-+
|*|
+-+
"""
When we parse the state
Then pacman should be at 1 , 1
And pacman should be dead
