@Z
Feature: Main Loop
  As a player
  I can run the game from the command line
  So that I can enjoy it in the terminal

Scenario: Run the game with a specified file for maps
Given the command arg "--file=data/pacman-test.txt"
And the command arg "--debug"
When I run the command with the args
Then I should get the following output:
"""
3                           0
+------------+ +------------+
|............| |............|
|o+--+.+---+.| |.+---+.+--+o|
|.|  |.|   |.| |.|   |.|  |.|
|.+--+.+---+.+-+.+---+.+--+.|
|...........................|
|.+--+.+-+.+-----+.+-+.+--+.|
|.+--+.| |.+-----+.| |.+--+.|
|......| |...   ...| |......|
+----+.| +----=----+ |.+----+
     #.| |M|M| |M|M| |.#     
+----+.| +---------+ |.+----+
|......| |.........| |......|
|.+--+.| |.+-----+.| |.+--+.|
|.+--+.+-+.+-----+.+-+.+--+.|
|.............>.............|
|.+--+.+---+.+-+.+---+.+--+.|
|.|  |.|   |.| |.|   |.|  |.|
|o+--+.+---+.| |.+---+.+--+o|
|............| |............|
+------------+ +------------+
"""
