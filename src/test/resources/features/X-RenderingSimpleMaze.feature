@X
Feature: rendering a simple maze
As a game engine
I can render the state of all game elements
So that I can display it to the player

As a developer
I can render the state of all game elements
So that I can more easily assess test results

Scenario: a 3x3 game field
Given the game field of 3 x 3
And a pacman at 1 , 1 facing "LEFT"
And walls at the following places:
  | icon|x:d|y:d|
  |  +  | 0 | 0 |
  |  +  | 0 | 2 |
  |  +  | 2 | 0 |
  |  +  | 2 | 2 |
  |  -  | 1 | 0 |
  |  -  | 1 | 2 |
  | \|  | 0 | 1 |
  | \|  | 2 | 1 |
And the score is 0
And the lives are 3
When we play 1 turn
And we render the game
Then the game screen is
"""
3 0
+-+
|>|
+-+
"""

Scenario: pacman up 
Given the game state is
"""
3 0
+-+
|V|
+-+
"""
When we parse the state
And we render the game
Then the game screen is
"""
3 0
+-+
|V|
+-+
"""

Scenario: pacman alive 
Given the game state is
"""
3 0
+-+
|<|
+-+
"""
When we parse the state
And we render the game
Then the game screen is
"""
3 0
+-+
|<|
+-+
"""
And pacman is alive

Scenario: pacman is dead
Given the game state is
"""
3 0
+-+
|*|
+-+
"""
When we parse the state
Then pacman is at 1 , 1
And pacman is dead

