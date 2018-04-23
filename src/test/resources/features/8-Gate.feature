@8
Feature: Ghost gates and force fields
  As a moveable game element
  I can recognise gates and force fields
  So that I can obey the personal restrictions imposed by those game elements

Scenario: ghost only goes through gate one way
Given the game state is
"""
3 0
+-+
| |
| |
+=+
|M|
+-+
"""
When we parse the state
And we play 5 turns
And we render the game
Then the game screen should be
"""
3 0
+-+
|M|
| |
+=+
| |
+-+
"""

Scenario: ghost can't go through force fields
Given the game state is
"""
3    0
+----+
|M # |
+----+
"""
When we parse the state
And we play 2 turns
And we render the game
Then the game screen should be
"""
3    0
+----+
|M # |
+----+
"""

Scenario: pacman goes right through invisible gate
Given the game state is
"""
3    0
+----+
|< # |
+----+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen should be
"""
3    0
+----+
|  #<|
+----+
"""
