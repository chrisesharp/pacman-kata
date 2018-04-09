@1
Feature: initial game field parsing - pills
  As a game engine
  I can parse an initial game state as a screen shot
  So that I can initialize all relevant game elements

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs


Scenario: pacman facing down at 2,2 on a 5x5 game field and there is a 10 point pill at 2,1
Given the game state is
"""
2  10
+---+
| . |
| Λ |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 10 point pill at 2 , 1
And pacman is facing "DOWN"

Scenario: pacman facing down at 2,2 on a 5x5 game field and there is a 10 point pill at 1,1
Given the game state is
"""
2  10
+---+
|.  |
| Λ |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 10 point pill at 1 , 1
And pacman is facing "DOWN"

Scenario: pacman facing down at 2,2 on a 5x5 game field and there is a 50 point pill at 2,1
Given the game state is
"""
2  10
+---+
| o |
| Λ |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 50 point pill at 2 , 1
And pacman is facing "DOWN"

Scenario: pacman facing up at 2,2 on a 5x5 game field and there is a 50 point pill at 1,1
Given the game state is
"""
2  10
+---+
|o  |
| V |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 50 point pill at 1 , 1
And pacman is facing "UP"

Scenario: pacman facing up at 2,2 on a 5x5 game field and there is a 10 point pill at 1,1 and another at 2,1
Given the game state is
"""
2  10
+---+
|.. |
| V |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 10 point pill at 1 , 1
And there is a 10 point pill at 2 , 1
And pacman is facing "UP"

Scenario: pacman facing up at 2,2 on a 5x5 game field and there is a 50 point pill at 1,1 and another at 2,1
Given the game state is
"""
2  10
+---+
|oo |
| V |
|   |
+---+
"""
When we parse the state
Then pacman is at 2 , 2
And the player has 2 lives
And the player score is 10
And there is a 50 point pill at 1 , 1
And there is a 50 point pill at 2 , 1
And pacman is facing "UP"
