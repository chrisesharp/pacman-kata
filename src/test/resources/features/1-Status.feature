@Y
Feature: rendering a status line
As a game engine
I can render the state of the game 
So that I can display it to the player

As a developer
I can render the state of all game elements
So that I can more easily assess test results

Scenario: a 5 column status line
Given the screen column width is 5
And the player has 3 lives
And the player score is 10
When we render the status line
Then I should get the following output:
"""
3  10
"""