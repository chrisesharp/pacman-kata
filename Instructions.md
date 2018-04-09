# PACMAN

Your task is to write a simple pacman game.
The game consists of the following elements:
- Player lives [ starts with 3 ]
- Lives should be left justified to the game field
- Player score [ starts at 0 pts]
- Score should be right justified to the game field
- Player representation (pacman) [ right=<, left=>, up=V, down=Λ, dead=* ]
- Pills [ .=10 pts , o=50 pts ]
- Ghosts [ M, W ]
- Walls [ -, +, | ]
- Gate [ = ]
- Force field [#]
- Player control keys [ left=j, up=i, right=l, down=m ]
- Pacman keeps moving in the last control direction
- Pacman stops when he hits a wall
- Pacman loses a life when touched by a ghost, unless...
- Pacman can eat a ghost for 50 ticks after eating a O pill.
- Pacman gets 200 points for eating a ghost
- Ghosts keep moving but only change direction at junctions or walls
- Ghosts can't return back through the gate
- Ghosts move at half speed when panicked
- Pacman can move through "invisible" gates but Ghosts can't
- Pacman's movement will wrap around horizontally if he goes off the edge of the screen
- Game is over when all lives lost or all pills eaten

The game state progresses 1 tick at a time. An example of play from
1 tick to the next might be like this:
```
3     160
+-------+
|<..o...|
| +-+ + |
# | | | #
| + +-+ |
|  M    |
+-------+
```
to
```
3     170
+-------+
| <.o...|
| +-+ + |
# | | | #
| + +-+ |
| M     |
+-------+
```
