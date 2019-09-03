# Maze-Generation-Game
----

Super simple game in which user defines values for `columns` and `rows` in a maze. Afterwards, the GUI will transition into the Maze view. The user can then choose to click `start` to view the live generation of the maze. Alternatively, the user can simply click `calculate` and try to solve the maze.

Since the algorithm used in this implementation doesn't guarantee that the maze is solvable on the first-shot of clicking `start`, the user can either click `solve` to view how the AI attempts to solve the maze and how it "fixes" the maze in case it finds it to be unsolvable. Again, the user can simply click `calculate` in order to skip the animation.

Lastly, the `lights` of the maze will turn off as soon as the maze is ready to be solved. The user will have a limited view of the maze and their surroundings. The field of view can be turned higher by using the slider to the right. The light can be turned on by using the `LIGHTS!` button towards the bottom.

Questions/Suggestions are welcome! Submit a pull-request if you add features that may improve the game.
