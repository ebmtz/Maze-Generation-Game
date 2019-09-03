package maze;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This Class is used as a component in a Maze. Each Cell has four Walls: Top, Right, Bottom, and Left. 
 * @author Erick
 *
 */
public class Cell {
	
	protected int width,height; 
	protected int x,y;	
	protected boolean [] WALLS;
	protected boolean visited,tracked,end;	
	
	/**
	 * Constructor for the Class
	 * @param X Position in the Image of the JPanel.
	 * @param Y Position in the Image of the JPanel.
	 * @param W The width.
	 * @param H The height.
	 */
	public Cell(int X, int Y, int W, int H)
	{
		x=X;
		y=Y;
		width=W;
		height=H;
		WALLS = new boolean[4];
		visited = false;
		tracked = false;
		end = false;
		for(int x=0;x<4;x++) WALLS[x] = true;
	}
	
	/**
	 * Checks if there is a top wall.
	 * @return True if there is a top wall.
	 */
	public boolean NORTH(){return WALLS [0];}
	
	/**
	 * Checks if there is a right wall.
	 * @return True if there is a right wall.
	 */
	public boolean EAST() {return WALLS [1];}
	
	/**
	 * Checks if there is a bottom wall.
	 * @return True if there is a bottom wall.
	 */
	public boolean SOUTH(){return WALLS [2];}
	
	/**
	 * Checks if there is a left wall.
	 * @return True if there is a left wall.
	 */
	public boolean WEST() {return WALLS [3];}
	
	/** 
	 * @return True if the Cell has been visited by a MazeBuilder.
	 */
	public boolean getVisited(){return visited;}
	
	/**
	 * 
	 * @return True if the Cell is considered to be the end of the Maze.
	 */
	public boolean end(){return end;}
	
	/** 
	 * @return True if the Cell is considered Tracked by a MazeRunner.
	 */
	public boolean tracked(){return tracked;}
	
	/**
	 * Checks if the Cell is adjacent to another Cell.
	 * @param c The Cell to be tested with this Cell.
	 * @return True if the Cells are adjacent to each other.
	 */
	public boolean adjacent(Cell c)
	{
		if(x == c.getX() && y-height == c.getY())	return true; //Up
		if(x+width == c.getX() && y == c.getY())	return true; //Right
		if(x == c.getX() && y+height == c.getY())	return true; //Down
		if(x-width == c.getX() && y == c.getY())	return true; //Left
		return false;
	}
	
	/**
	 * Set the Cell status to Visited.
	 * @param b The next visited status.
	 */
	public void setVisited(boolean b){visited = b;}
	
	/**
	 * Sets the "End" status the Cell will have.
	 * @param b "End" status the Cell will have.
	 */
	public void setEnd(boolean b){end = b;}
	
	/**
	 * Sets the "Tracked" status for the Cell.
	 * @param b "Tracked" status for the Cell.
	 */
	public void setTracked(boolean b){tracked = b;}
	
	/**
	 * Sets the North wall to be up or down.
	 * @param b True for up, False for down.
	 */
	public void setNorth(boolean b){WALLS[0]=b;}
	
	/**
	 * Sets the East wall to be up or down.
	 * @param b True for up, False for down.
	 */
	public void setEast (boolean b){WALLS[1]=b;}
	
	/**
	 * Sets the South wall to be up or down.
	 * @param b True for up, False for down.
	 */
	public void setSouth(boolean b){WALLS[2]=b;}
	
	/**
	 * Sets the West wall to be up or down.
	 * @param b True for up, False for down.
	 */
	public void setWest (boolean b){WALLS[3]=b;}	
	
	/**
	 * Removes a Wall between two Cells.
	 * @param c The Cell adjacent to the current cell which wall also needs removed.
	 */
	public void removeWall(Cell c)
	{
		
		if (x == c.getX() && y - height == c.getY()) {
			this.setNorth(false); //Up
			c.setSouth(false);
			
		}else
		if (x + width == c.getX() && y == c.getY()) {
			this.setEast(false); //Right
			c.setWest(false);
			
		}else
		if (x == c.getX() && y + height == c.getY()) {
			this.setSouth(false); //Down
			c.setNorth(false);
			
		}else
		if (x - width == c.getX() && y == c.getY()) {
			this.setWest(false); //Left
			c.setEast(false);
			
		} 
		
	}	
	
	/** 
	 * @return The X position.
	 */
	public int getX() {return x;}
	
	/** 
	 * @return The Y position.
	 */
	public int getY() {return y;}
	
	/** 
	 * @return The Width.
	 */
	public int getW() {return width;}
	
	/** 
	 * @return The Height.
	 */
	public int getH() {return height;}
	
	/**
	 * Finds the Cells that neighbor the current Cell.
	 * @param cells The ArrayList of Cells in the Maze.
	 * @return ArrayList<Cell> of neighboring Cells.
	 */
	ArrayList<Cell> getNeighbors(ArrayList<Cell> cells)
	{		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		for(Cell c: cells)
			if(this.adjacent(c) && !c.getVisited())
				neighbors.add(c);			
		
		
		return neighbors;
	}
	
	/**
	 * Uses Graphics2D in order to draw itself in a JPanel.
	 * @param p: Graphics2D Object.
	 */
	public void draw(Graphics2D p)
	{
		
		if (this.end()) {
			p.setColor(new Color(0, 20, 0, 60));
			p.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
		}
		
		// This section commented out as it is unnecessary
//		if (this.getVisited()) {
//			p.setColor(new Color(0, 0, 200, 255));
//			p.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
//		} 
//		else {
//			p.setColor(Color.WHITE);
//			p.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
//		}
		if (this.tracked()) {
			p.setColor(new Color(100, 0, 0, 150));
			p.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
		}
		p.setColor(Color.BLACK.darker());
		if (this.NORTH())
			p.drawLine(this.getX(), this.getY(), this.getX() + this.getW(), this.getY());
		if (this.EAST())
			p.drawLine(this.getX() + this.getW(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH());
		if (this.SOUTH())
			p.drawLine(this.getX(), this.getY() + this.getH(), this.getX() + this.getW(), this.getY() + this.getH());
		if (this.WEST())
			p.drawLine(this.getX(), this.getY(), this.getX(), this.getY() + this.getH());
	}
	
	/**
	 * Gets the distance (in pixels) between two Cells
	 * @param c Target Cell.
	 * @return Distance (in pixels) between this Cell and target Cell.
	 */
	public double distance(Cell c)
	{
		double xdist = Math.abs(x-c.getX());
		double ydist = Math.abs(y-c.getY());
		
		
		return Math.sqrt((xdist*xdist)+(ydist*ydist));
	}
	
	/**
	 * Prints the main Cell properties for debugging purposes.
	 */
	public void println()
	{
		System.out.println("Cell coor is "+x/width+","+y/height+"\tVisited = "+visited);
	}
	
	/**
	 * Prints the status of the Walls surrounding the Maze for debugging purposes.
	 */
	public void printWalls()
	{
		System.out.println("N/E/S/W =  "+WALLS[0]+","+WALLS[1]+","+WALLS[2]+","+WALLS[3]);
	}
}
