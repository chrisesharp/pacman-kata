@4
Feature: Pills and Power Pills
  As a game engine
  I can compute the effect of pacman eating pills
  So that all relevant elements effect the correct behaviour as per the rules

Scenario: pacman goes left and eats a pill
Given the game state is
"""
3    0
+----+
| ..>|
+----+
"""
When we parse the state
And we play 1 turns
And we render the game
Then the game screen should be
"""
3   10
+----+
| .> |
+----+
"""

Scenario: pacman goes left and eats three pills
Given the game state is
"""
3    0
+----+
|...>|
+----+
"""
When we parse the state
And we play 2 turns
And we render the game
Then the game screen should be
"""
3   20
+----+
|.>  |
+----+
"""

Scenario: pacman goes right and eats a power pill
Given the game state is
"""
3    0
+----+
|< o.|
+----+
"""
When we parse the state
And we play 2 turns
And we render the game
Then the game screen should be
"""
3   50
+----+
|  <.|
+----+
"""

Scenario: pacman goes right and eats 2 pills and a power pill
Given the game state is
"""
3     0
+-----+
|<..o |
|.    |
+-----+
"""
When we parse the state
And we play 4 turns
And we render the game
Then the game screen should be
"""
3    70
+-----+
|    <|
|.    |
+-----+
"""

Scenario: pacman goes eats a normal pill and ghost does not change state
Given the game state is
"""
3    0
+----+
|<. M|
+----+
|....|
+----+
"""
When we parse the state
And we play 1 turns
And we render the game
Then the game screen should be
"""
3   10
+----+
| <M |
+----+
|....|
+----+
"""

Scenario: pacman goes eats a power pill and makes ghost change state
Given the game state is
"""
3    0
+----+
|<o M|
+----+
|....|
+----+
"""
When we parse the state
And we play 1 turns
And we render the game
Then the game screen should be
"""
3   50
+----+
| <W |
+----+
|....|
+----+
"""
