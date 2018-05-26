@F
Feature: Display and colour effects
  As a game engine
  I can rely on the Display provided to me to render everything correctly
  So that I can support multiple displays

  As a game element
  I can rely on the Display provided to me to render everything correctly
  So that my behaviour is consistent across displays


Scenario: a display recognizes the correct game dimensions
Given a display
And the game state is
"""
0 0
+-+
| |
+-+
"""
When we parse the state
And initialize the display
Then the game dimensions should equal the display dimensions

Scenario: display buffer rendered as a new screen
Given a display
And initialize the display
And the ANSI "clearscreen" sequence is "1B5B481B5B324A"
And the ANSI "bold" sequence is "1B5B316D"
And the ANSI "reset" sequence is "1B5B306D"
And the ANSI "newline" sequence is "0A"
And the ANSI "+" sequence is "2B"
When we refresh the display with the buffer "+"
Then the display byte stream should be
| SEQUENCE    |
| clearscreen |
| bold        |
| +           |
| reset       |
| newline     |

@leave
Scenario: display buffer rendered as a new screen with bold white pill
Given a colour display
And initialize the display
And the ANSI "clearscreen" sequence is "1B5B481B5B324A"
And the ANSI "bold" sequence is "1B5B316D"
And the ANSI "black-background" sequence is "1B5B34306D"
And the ANSI "white-foreground" sequence is "1B5B33376D"
And the ANSI "reset" sequence is "1B5B306D"
And the ANSI "newline" sequence is "0A"
And the ANSI "+" sequence is "2B"
When we refresh the display with the buffer "++"
Then the display byte stream should be
| SEQUENCE         |
| clearscreen      |
| bold             |
| white-foreground |
| black-background |
| +                |
| +                |
| reset            |
| newline          |

@leave
Scenario: display buffer rendered as a new screen with yellow pacman
Given a colour display
And initialize the display
And the ANSI "clearscreen" sequence is "1B5B481B5B324A"
And the ANSI "bold" sequence is "1B5B316D"
And the ANSI "black-background" sequence is "1B5B34306D"
And the ANSI "white-foreground" sequence is "1B5B33376D"
And the ANSI "yellow" sequence is "1B5B33336D"
And the ANSI "reset" sequence is "1B5B306D"
And the ANSI "newline" sequence is "0A"
When the display renders the icon "<" in yellow and refreshes
Then the display byte stream should be
| SEQUENCE         |
| clearscreen      |
| bold             |
| white-foreground |
| black-background |
| yellow           |
| <                |
| reset            |
| white-foreground |
| black-background |
| reset            |
| newline          |