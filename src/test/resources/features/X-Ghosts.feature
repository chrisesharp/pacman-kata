@X
Feature: ghosts
  As a game engine
  I can identify ghosts
  So that I can simulate them

Scenario: there is a ghost at 3,1
Given the game state is
"""
2  10
+---+
|  M|
|   |
|   |
+---+
"""
When we parse the state
Then ghost should be at 3 , 1

Scenario: there is a ghost at 1,1
Given the game state is
"""
2  10
+---+
|M  |
|   |
|   |
+---+
"""
When we parse the state
Then ghost should be at 1 , 1

Scenario: there is a ghost at 1,1 and another at 2,1
Given the game state is
"""
2  10
+---+
|MM |
| V |
|   |
+---+
"""
When we parse the state
Then ghost should be at 1 , 1
And ghost should be at 2 , 1

Scenario: 2 ghosts are rendered
Given the game state is
"""
3   0
+---+
|M M|
+---+
"""
When we parse the state
And we render the game
Then the game screen should be
"""
3   0
+---+
|M M|
+---+
"""

Scenario: ghost is panicked
Given the game state is
"""
3   0
+---+
|M W|
+---+
"""
When we parse the state
Then ghost at 1 , 1 should be calm
And ghost at 3 , 1 should be panicked

