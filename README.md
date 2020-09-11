# Domineering
A Java implementation of the game Domineering which uses game trees for the computer opponent.

This a terminal-based implementation of the game Domineering which allows you to play against an AI opponent.

I made this for an assignment during my first year of University and was originally intended that my AI player would be used as a black-box player that would play against my lecturer's various black box players of differing skill, which is why the code presentation might appear different to how some would have built it if it were purely going to be made as a human vs AI game.

If you would like to play this game, the main method is in BlackBoxDomineering2 (I have omitted unnecessary classes that were used in previous parts of the assignment) where there are 4 command line arguments. The first is a string which must be either "first" or "second" and this indicates if the AI will go first or second. The first player is always horizontal and the second player is always vertical. The second argument is either the string "horizontal" or "vertical". This is unfortunately redundant as it was only needed in order to comply with the lecturer's marking script, as the requirements were changed during the lifetime of the assignment. The third argument is the number of columns and the fourth argument is the number of rows.
Example: "java BlackBoxDomineering2 first horizontal 4 4" (don't forget to compile the .java files first).

The AI will play optimally for board sizes upto 4x5/5x4 but for board sizes bigger, a heuristic approach is used. Please note that for big board sizes like 8x8 upwards, it may take some time to generate the game tree. To make a move you must specify a move as the number of columns in followed by a comma followed by the number of rows down. So the top left hand corner has coordinates "0,0" while the bottom right hand corner of a 4x4 grid is "3,3". The top row is governed by "x,0" where x goes from 0 to the number of columns subtract 1, and the left column is governed by "0, y" where y goes from 0 to the number of rows subtract 1. This then applies for other rows and columns (e.g. "2,3" etc.).

I have used my implementation of immutable binary search trees within this assignment to show their use as a game tree for a computer opponent.
