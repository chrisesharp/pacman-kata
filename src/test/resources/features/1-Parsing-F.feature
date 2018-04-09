@1
Feature: initial game field parsing - wildcard status
  As a game engine
  I can parse an initial game state as a screen shot
  So that I can initialize all relevant game elements

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs

Scenario: wildcard lives and scores should default to 3 and 0
Given the game state is
"""
? ?
+-+
|#|
+-+
"""
When we parse the state
Then the game lives should be 3
And the game score should be 0

Scenario: wildcard lives and scores should not change existing values
Given the game state is
"""
? ?
+-+
|#|
+-+
"""
And the score is 100
And the lives are 2
When we parse the state
Then the game lives should be 2
And the game score should be 100
