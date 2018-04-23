@1
Feature: initial game field parsing - walls
  As a game engine
  I can parse an initial game state as a screen shot
  So that I can initialize all relevant game elements

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs

Scenario: pacman is at 1,1 on a 3x3 game field and there is a wall at 0,1 and another at 1,0 and another at 1,2 and another at 2,1
Given the game state is
"""
3 0
+-+
|>|
+-+
"""
When we parse the state
Then pacman should be at 1 , 1
And the player should have 3 lives
And the score should be 0
And there should be a wall at 0 , 1
And there should be a wall at 1 , 0
And there should be a wall at 1 , 2
And there should be a wall at 2 , 1
And there should be a wall at 0 , 0
And there should be a wall at 2 , 0
And there should be a wall at 0 , 2
And there should be a wall at 2 , 2

Scenario: gate at 1 , 1
Given the game state is
"""
3 0
+-+
|=|
+-+
"""
When we parse the state
Then there should be a gate at 1 , 1

Scenario: force field at 1 , 1
Given the game state is
"""
3 0
+-+
|#|
+-+
"""
When we parse the state
Then there should be a force field at 1 , 1
