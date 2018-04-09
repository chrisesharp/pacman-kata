@3
Feature: Movement
  As a game engine
  I can compute the movement of moveable game elements
  So that those elements will be in the correct location next turn

  As a developer
  I can provide screen representations of a game state
  So that I can more easily provide test inputs

Scenario: pacman goes left
Given the game state is
"""
3  0
+--+
| >|
+--+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|> |
+--+
"""

Scenario: pacman goes right
Given the game state is
"""
3  0
+--+
|< |
+--+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
| <|
+--+
"""

Scenario: pacman goes up
Given the game state is
"""
3  0
+--+
|  |
|V |
+--+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|V |
|  |
+--+
"""

Scenario: pacman goes down
Given the game state is
"""
3  0
+--+
| Λ|
|  |
+--+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
3  0
+--+
|  |
| Λ|
+--+
"""

Scenario: pacman stops at wall to left
Given the game state is
"""
3   0
+---+
|  >|
+---+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen is
"""
3   0
+---+
|>  |
+---+
"""

Scenario: ghost goes left
Given the game state is
"""
3  0
+--+
| M|
+--+
"""
When we parse the state
And we play 1 turns
And we render the game
Then the game screen is
"""
3  0
+--+
|M |
+--+
"""
Scenario: ghost follows maze to right
Given the game state is
"""
3     160
+-------+
|   |   |
|.+ + + |
|.| | | |
|.+ +-+ |
|M|     |
+-------+
"""
When we parse the state
And we play 11 turns
And we render the game
Then the game screen is
"""
3     160
+-------+
|   |   |
|.+ + + |
|.| | | |
|.+ +-+ |
| | M   |
+-------+
"""
Scenario: ghost follows maze to left
Given the game state is
"""
3     160
+-------+
|   |   |
| + + + |
| | |M| |
| + +-+ |
| |     |
+-------+
"""
When we parse the state
And we play 22 turns
And we render the game
Then the game screen is
"""
3     160
+-------+
|   |   |
| + + + |
| | | | |
| + +-+ |
|M|     |
+-------+
"""
