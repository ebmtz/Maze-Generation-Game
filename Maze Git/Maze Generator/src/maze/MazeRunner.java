package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Class used to determine if a Maze is solvable and fix the issue if it is not.
 * @author Erick
 *
 */
public class MazeRunner extends Cell {
	private final int COLS, ROWS;
	private int direction,col,row;
	private boolean impossible, done, backtracking;
	private Stack<Cell> trail;
	private Cell [][] maze;
	private Cell current;
	public final int EAST = 0, NORTH = 90, WEST = 180, SOUTH = 270;
	
	/**
	 * Constructor for the MazeRunner
	 * @param X The 'x' position in the image.
	 * @param Y The 'y' position in the image.
	 * @param W The Width of the Cell.
	 * @param H The Height of the Cell.
	 * @param cols The No. of Columns in the Maze the runner will be in.
	 * @param rows The No. of Rows in the Maze the runner will be in.
	 * @param cells An ArrayList of Cells that compose the Maze that the MazeRunner will solve.
	 */
	public MazeRunner(int X, int Y, int W, int H, int cols, int rows,ArrayList<Cell> cells) {
		super(X, Y, W, H);
		// TODO Auto-generated constructor stub
		COLS = cols;
		ROWS = rows;
		trail = new Stack<Cell>();
		lowerWalls();
		impossible = done = false;
		direction = EAST;
		col = x / width;
		row = y / height;
		maze = new Cell[cols][rows];
		int index = 0;
		for(int x = 0;x < cols;x++)
			for(int y = 0;y < rows;y++){
				maze[x][y] = cells.get(index);
				index++;
			}
		current = maze[0][0];
	}
	
	/**
	 * Positions the MazeRunner onto the (0,0) location.
	 */
	public void reset(){
		col = row = x = y = 0;
		current = maze[0][0];
	}
	
	/**
	 * Returns the cell which the MazeRunner currently occupies.
	 * @return Cell occupied by the MazeRunner.
	 */
	public Cell getCurrent(){return current;}
	
	/**
	 * Determines whether the MazeRunner has solved the Maze.
	 * @return True if the MazeRunner is positioned in the "End" Cell of the Maze.
	 */
	public boolean isDone(){return done;}
	
	/**
	 * Sets the Boolean value of isDone.
	 * @param b
	 */
	public void setDone(boolean b){done = b;}
	
	/**
	 * Sets the Wall values for the MazeRunner to false.
	 */
	public void lowerWalls()
	{
		for(int x = 0;x<WALLS.length;x++)WALLS[x] = false;
	}
	
	/**
	 * Moves the Cell up in the grid.
	 */
	public void moveUp()	{
		row--;
		y = row * height;
		current = maze[col][row];}
	
	/**
	 * Moves the Cell right in the grid.
	 */
	public void moveRight()	{
		col++;
		x = col * width;
		current = maze[col][row];}
	
	/**
	 * Moves the Cell down in the grid.
	 */
	public void moveDown()	{
		row++;
		y = row * height;
		current = maze[col][row];}
	
	/**
	 * Moves the Cell left in the grid.
	 */
	public void moveLeft()	{
		col--;
		x = col * width;
		current = maze[col][row];}
	
	/**
	 * Sets the direction in which the MazeRunner will be looking.
	 * @param d The Direction which the MazeRunner will try to go next.
	 */
	public void setDirection(int d){direction = d;}
	
	/**
	 * Determines whether the MazeRunner overlaps a Cell in the Maze.
	 * @param c Cell to test
	 * @return True if the (x,y) position of MazeRunner is equal to the position of the test Cell.
	 */
	public boolean overlaps(Cell c){return x == c.getX() && y == c.getY();}
	
	/**
	 * Sets the Direction towards a specific Cell.
	 * @param c The Cell to set the direction to.
	 */
	public void setDirection(Cell c)
	{
		if(x == c.getX() && y-height == c.getY())
			direction = NORTH; //Up
		
		if(x+width == c.getX() && y == c.getY())
			direction = EAST; //Right
		
		if(x == c.getX() && y+height == c.getY())
			direction = SOUTH; //Down
		
		if(x-width == c.getX() && y == c.getY())
			direction = WEST; //Left
	}
	
	/**
	 * Get Cells around the MazeRunner.
	 * @return An ArrayList<Cell> of neighbors that have not been tracked and are reachable. 
	 */
	ArrayList<Cell> getNeighbors()
	{
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		if(canAdvance(NORTH) && !maze[col][row - 1].tracked) neighbors.add(maze[col][row - 1]);
		if(canAdvance(EAST)  && !maze[col + 1][row].tracked) neighbors.add(maze[col + 1][row]);
		if(canAdvance(SOUTH) && !maze[col][row + 1].tracked) neighbors.add(maze[col][row + 1]);
		if(canAdvance(WEST)  && !maze[col - 1][row].tracked )neighbors.add(maze[col - 1][row]);
		
		return neighbors;
	}
	
	/**
	 * Get Cells tracked Cells around MazeRunner.
	 * @return An ArrayList<Cell> of reachable, tracked neighbors.
	 */
	ArrayList<Cell> getTrackedNeighbors()
	{
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		if(canAdvance(NORTH) && maze[col][row - 1].visited) neighbors.add(maze[col][row - 1]);
		if(canAdvance(EAST)  && maze[col + 1][row].visited) neighbors.add(maze[col + 1][row]);
		if(canAdvance(SOUTH) && maze[col][row + 1].visited) neighbors.add(maze[col][row + 1]);
		if(canAdvance(WEST)  && maze[col - 1][row].visited) neighbors.add(maze[col - 1][row]);
		
		return neighbors;
	}
	
	/**
	 * Get Cells unreachable Cells around MazeRunner.
	 * @return An ArrayList<Cell> of unreachable, untracked, visited neighbors.
	 */
	ArrayList<Cell> unreachableNeighbors()
	{
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		if(row != 0 	&& !canAdvance(NORTH) && !maze[col][row - 1].tracked && maze[col][row - 1].visited) neighbors.add(maze[col][row - 1]);
		if(col !=COLS-1 && !canAdvance(EAST)  && !maze[col + 1][row].tracked && maze[col + 1][row].visited) neighbors.add(maze[col + 1][row]);
		if(row !=ROWS-1 && !canAdvance(SOUTH) && !maze[col][row + 1].tracked && maze[col][row + 1].visited) neighbors.add(maze[col][row + 1]);
		if(col != 0		&& !canAdvance(WEST)  && !maze[col - 1][row].tracked && maze[col - 1][row].visited) neighbors.add(maze[col - 1][row]);
		
		return neighbors;
	}
	
	/**
	 * Checks if the MazeRunner can advance in the current direction. 
	 * @return True if the next Cell is within bounds and there are no walls in between the Current and the Next Cell. 
	 */
	public boolean canAdvance()
	{
		return canAdvance(direction);
	}
	
	/**
	 * Checks if the MazeRunner can advance in a specific direction. 
	 * @return True if the next Cell is within bounds and there are no walls in between the Current and the Next Cell. 
	 */
	public boolean canAdvance(int dir)
	{
		switch (dir){
		case NORTH:			
			if(row - 1 >= 0 && !current.NORTH()) 
				return true;//---------------------------------
			break;
		case EAST:
			if(col + 1 < COLS && !current.EAST())
				return true;//---------------------------------
			break;
		case SOUTH:
			if(row + 1 < ROWS && !current.SOUTH())
				return true;//---------------------------------
			break;
		case WEST:
			if(col - 1 >= 0 && !current.WEST() )
				return true;//---------------------------------
			break;
		}
		
		return false;
	}
	
	/**
	 * Moves, if possible, the MazeRunner onto the next position.
	 */
	public void advance()
	{
		if(canAdvance() )
			switch (direction){
				case NORTH:
					moveUp();
					break;
				case EAST:
					moveRight();
					break;
				case SOUTH:
					moveDown();
					break;
				case WEST:
					moveLeft();
					break;				
			}
	}	
	
	/**
	 * Retrieves the last visited Cell from the Stack<Cell> in order to find an untracked Cell
	 * to move onto. If the Stack becomes empty before reaching the "End" Cell of the Maze, then the
	 * Maze is unsolvable and a solvable route must be created.
	 */
	public void backtrack()
	{
		if (!impossible) {
			if (!trail.isEmpty()) {
				setDirection(trail.pop());
				backtracking = false;
			} else if (!done) {
				impossible = true;
				for(int x = 0;x<COLS;x++)
					for(int y =0;y<ROWS;y++)
						maze[x][y].setVisited(true);
			} 
		}else if (!trail.isEmpty()) {
			setDirection(trail.pop());
			backtracking = false;
		}		
	}
	
	/**
	 * Determines the Next direction the MazeRunner should move onto.
	 */
	public void nextDirection()
	{		
		if(current.end()){
			done = true;
			this.reset();
			
			for(int x =0;x<COLS;x++)
				for(int y =0;y<ROWS;y++){
					maze[x][y].setVisited(true);
					maze[x][y].setTracked(false);
				}
		}
		
		if (!impossible) 
			calcNotImpossible();
		else //===============================================================================================================
			calcImpossible();
						
		
		if(!done)advance();	
		
	}
	
	/**
	 * Algorithm used to Calculate the next move in a solvable Maze.
	 */
	public void calcNotImpossible()
	{
		ArrayList<Cell> neighbors = this.getNeighbors();
		
		current.setTracked(true);
		if (!neighbors.isEmpty())
			backtracking = false;
		if (canAdvance() && !current.tracked()) {
			trail.push(current);
		} else {
			if (!neighbors.isEmpty()) {
				trail.push(current);
				Random randy = new Random();
				Cell temp = neighbors.get(randy.nextInt(neighbors.size()));
				setDirection(temp);
			} else
				backtracking = true;
		}
		if (backtracking)
			backtrack();
	}
	
	/**
	 * Algorithm used to Calculate the next move in an unsolvable Maze.
	 */
	public void calcImpossible()
	{
		ArrayList<Cell> beenThere = getTrackedNeighbors();
		ArrayList<Cell> unreachables = unreachableNeighbors();
		
		current.setVisited(false);
		if (!beenThere.isEmpty())
			backtracking = false;
		
		if (canAdvance() && current.getVisited()) {
			trail.push(current);
		} else {
			if(!unreachables.isEmpty())
				for(Cell c :unreachables)
				{
					if(c.getVisited() && !c.tracked())
					{
						current.removeWall(c);
						impossible = false;
					}						
				}							
			else if (!beenThere.isEmpty()) {
				trail.push(current);
				Random randy = new Random();
				Cell temp = beenThere.get(randy.nextInt(beenThere.size()));
				setDirection(temp);
			} else
				backtracking = true;
		}
		if (backtracking)
			backtrack();
	}
}
