@score
Feature: Scoreboard
  As a player
  I can have my scores posted to a shared scoreboard
  So that I can brag to my friends

Scenario: Post a score to the scoreboard
Given the game field of 1 x 1
And the score is 10000
And the user is "chris"
When I post the score to the scoreboard
And I get the scores
Then I should get the following response:
"""
chris:10000
"""
