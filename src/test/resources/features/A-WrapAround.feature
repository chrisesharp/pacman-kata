@A
Feature: Wrapping playfield
  As a pacman
  I can travel off one side of the play field and come back on the other
  So that I can not fall off the play field!

Scenario: pacman goes right and comes on left
Given the game state is
"""
3    0
+----+
#   <#
+----+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen should be
"""
3    0
+----+
#<   #
+----+
"""

Scenario: pacman goes left and comes on right
Given the game state is
"""
3    0
+----+
#>   #
+----+
"""
When we parse the state
And we play 3 turns
And we render the game
Then the game screen should be
"""
3    0
+----+
#   >#
+----+
"""
