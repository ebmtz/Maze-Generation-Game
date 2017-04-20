package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * This Class uses a Cell from an existing ArrayList of Cells
 * contained in a grid in order to create a simple Maze.
 * @author Erick 
 */
public class MazeBuilder {
	private final int COLS, ROWS;
	private Stack<Cell> path;
	private Cell current;
	private Cell [][] maze;
	private boolean mazeDone;
	
	/**
	 * Constructor.
	 * @param c The cell in which the MazeBuilder starts from.
	 * @param cols Number of Columns in the Maze.
	 * @param rows Number of Rows in the Maze.
	 * @param cells ArrayList<Cell> of the Cells in the Maze.
	 */
	public MazeBuilder(Cell c, int cols, int rows, ArrayList<Cell> cells)
	{
		COLS = cols;
		ROWS = rows;
		current = c;
		maze = new Cell[cols][rows];
		path = new Stack<Cell>();
		
		int index=0;
		for(int col = 0;col<cols;col++)
			for(int row = 0;row<rows;row++)
			{
				maze[col][row] = cells.get(index);
				index++;
			}
	}
	
	/**
	 * Get the Cell in which the MazeBuilder is placed.
	 * @return The Current Cell.
	 */
	public Cell getCell(){return current;}
	
	/**
	 * Sets the "Position" in the Maze where the MazeBuilder will be placed. 
	 * @param pos From 0 to 3, the positions are TopLeft, BottomRight, TopRight, BottomLeft.
	 */
	public void setPosition(int pos)
	{
		switch (pos){
		case 0:
			current = maze[0][0];
			break;
		case 1:
			current = maze[COLS-1][ROWS-1];
			break;
		case 2:
			current = maze[COLS-1][0];
			break;
		case 3:
			current = maze[0][ROWS-1];
			break;
		default: current = maze[0][0];				
		}
	}
	
	/**
	 * Creates the Maze.
	 * @param cells The arrayList of Cells contained within the Maze.
	 */
	public void createMaze(ArrayList<Cell> cells)
	{		
		ArrayList<Cell> neighbors;
		neighbors  = current.getNeighbors(cells);
		current.setVisited(true);
		
		if (neighbors.size() > 0) {		
			path.push(current);
			Random randy = new Random();
			Cell c = neighbors.get(randy.nextInt(neighbors.size()));
			current.removeWall(c);
			current = c;
		} else 
			if(!path.empty()){
				current =  path.pop();			
			}		
		
		if(path.empty())mazeDone = true;		
	}
	
	/**
	 * Boolean to determine whether the Maze is completed.
	 * @return True if the Stack of the MazeBuilder is empty. This indicates that there are
	 * 			no more unvisited Cells to build upon.
	 */
	public boolean mazeDone(){return mazeDone;}

}
