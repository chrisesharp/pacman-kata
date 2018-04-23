@7
Feature: Player controls
  As pacman
  I can understand direction instructions from a player
  So that I can move appropriately

  As a developer
  I can decouple the input detection from the game play
  So that I can test these elements independently

Scenario: Pacman direction is LEFT when player presses 'j'
Given the game state is
"""
3  0
+--+
| <|
+--+
"""
When we parse the state
When the player presses "j"
Then then pacman should go "LEFT"

Scenario: Pacman direction is RIGHT when player presses 'l'
Given the game state is
"""
3  0
+--+
|> |
+--+
"""
When we parse the state
When the player presses "l"
Then then pacman should go "RIGHT"

Scenario: Pacman direction is UP when player presses 'i'
Given the game state is
"""
3  0
+--+
|  |
| Λ|
+--+
"""
When we parse the state
When the player presses "i"
Then then pacman should go "UP"

Scenario: Pacman direction is DOWN when player presses 'k'
Given the game state is
"""
3  0
+--+
| V|
|  |
+--+
"""
When we parse the state
When the player presses "m"
Then then pacman should go "DOWN"
