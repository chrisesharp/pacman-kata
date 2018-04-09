@5
Feature: Pacman and Ghost collision
  As a game engine
  I can compute the effect of collision between moveable game elements
  So that those elements will behave correctly as per the rules

Scenario: ghost catches pacman
Given the game state is
"""
3    0
+----+
|>  M|
+----+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen is
"""
2    0
+----+
|*   |
+----+
"""

Scenario: ghost catches pacman with 1 life left
Given the game state is
"""
1    0
+----+
|>  M|
+----+
|    |
|    |
+----+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen is
"""
0    0
+----+
|GAME|
+OVER+
|    |
|    |
+----+
"""

Scenario: ghost catches pacman with 1 life left on bigger screen
Given the game state is
"""
1       0
+-------+
|--+    |
|>M|    |
| -+    |
|       |
|       |
+-------+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen is
"""
0       0
+-------+
|-GAME  |
|*OVER  |
| -+    |
|       |
|       |
+-------+
"""
