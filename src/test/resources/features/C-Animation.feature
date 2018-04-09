@C
Feature: Animated Icons
  As a moveable game element
  I can get alternate icons
  So that I can animate when I move

Scenario: pacman goes left and animates
Given the game state is
"""
3  0
+--+
| >|
+--+
"""
And the game uses animation
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|} |
+--+
"""

Scenario: pacman goes right  and animates
Given the game state is
"""
3  0
+--+
|< |
+--+
"""
And the game uses animation
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
| {|
+--+
"""

Scenario: pacman goes down and animates
Given the game state is
"""
3  0
+--+
|Λ |
|  |
+--+
"""
And the game uses animation
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|  |
|^ |
+--+
"""
Scenario: pacman goes up and animates
Given the game state is
"""
3  0
+--+
|  |
|V |
+--+
"""
And the game uses animation
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|v |
|  |
+--+
"""

Scenario: pacman goes left twice animated
Given the game state is
"""
3   0
+---+
|  >|
+---+
"""
And the game uses animation
When we parse the state
And we play 2 turn
And we render the game
Then the game screen is
"""
3   0
+---+
|>  |
+---+
"""
