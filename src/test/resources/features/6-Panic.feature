@6
Feature: effects of power pills on ghosts
  As a ghost
  I can effect the appropriate behaviour based on panic levels
  So that I can respond correctly to a power pill being eaten by pacman

Scenario: pacman catches half-speed ghost when panicked
Given the game state is
"""
3       0
+-------+
|<W     |
+-------+
"""
When we parse the state
And we play 1 turn
And we render the game
Then the game screen should be
"""
3     200
+-------+
| <     |
+-------+
"""

Scenario: panicked ghost avoids pacman
Given the game state is
"""
3     0
+-----+
|o> M |
+-----+
|.....|
+-----+
"""
When we parse the state
And we play 5 turns
And we render the game
Then the game screen should be
"""
3    50
+-----+
|>   W|
+-----+
|.....|
+-----+
"""

Scenario: panicked ghost recovers and chases pacman
Given the game state is
"""
3    0
+----+
|o> M|
+----+
|....|
+----+
"""
When we parse the state
And we play 51 turns
And we render the game
Then the game screen should be
"""
3   50
+----+
|> M |
+----+
|....|
+----+
"""

Scenario: eaten ghost restarts in the pen and gets out again
Given the game state is
"""
3    0
+----+
| o >|
+=+--+
|M|. |
+-+--+
"""
When we parse the state
And we play 5 turns
And we render the game
Then the game screen should be
"""
2  250
+----+
|*   |
+=+--+
| |. |
+-+--+
"""
