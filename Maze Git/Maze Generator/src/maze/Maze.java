package maze;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;


/**
 * Class in which the width, height, columns, and rows are set in order to animate
 * the creation of a maze. 
 * @author Erick
 *
 */
public class Maze extends JPanel implements KeyListener
{
	private final int WIDTH,HEIGHT, COLS, ROWS;	
	private int lightAmp;
	Image img;
	Graphics offg;
	ArrayList<Cell> cells;	
	MazeRunner runner;
	MazeBuilder[] builders;
	private boolean mazeDone,lightsOut,givenUp;	
	
	/**
	 * Maze constructor.
	 * @param cols: Number of columns the Maze will have.
	 * @param rows: Number of rows the Maze will have.
	 * @param W: The Width for the Maze.
	 * @param H: The Height for the Maze.
	 */
	public Maze(int cols, int rows,int W, int H)
	{	
		WIDTH  = W;
		HEIGHT = H;
		COLS = cols;
		ROWS = rows;
		lightAmp = 20;
		this.setSize(WIDTH, HEIGHT);
		this.setFocusable(true);
		this.setVisible(true);
		this.addKeyListener(this);
		builders    = new MazeBuilder[4];		
		cells 		= new ArrayList<Cell>();			
		
		for(int x = 0;x<WIDTH;x += WIDTH/COLS)									// Position a cell in every spot of the grid 
			for(int y = 0;y<HEIGHT;y+= HEIGHT/ROWS)
			{
				Cell temp =new Cell(x,y,WIDTH/COLS,HEIGHT/ROWS);
				cells.add(temp);			
			}
		runner	= new MazeRunner(0,0,WIDTH/COLS,HEIGHT/ROWS,COLS,ROWS,cells);	// Position the MazeRunner in the position (0,0)
		
		for(int x = 0;x<builders.length;x++)									// Position MazeBuilders in every corner of the maze
		{
			Cell temp = cells.get(0);
			builders[x] = new MazeBuilder(temp,COLS,ROWS,cells);
			builders[x].setPosition(x);
		}
		cells.get(cells.size()-1).setEnd(true);		//Set the bottom right cell to be the "end" cell.
	}
	
	/**
	 * Reset every component of the Maze.
	 */
	public void reset()
	{
		mazeDone    = false;
		builders    = new MazeBuilder[4];		
		cells 		= new ArrayList<Cell>();			
		
		for(int x = 0;x<WIDTH;x += WIDTH/COLS)
			for(int y = 0;y<HEIGHT;y+= HEIGHT/ROWS)
			{
				Cell temp =new Cell(x,y,WIDTH/COLS,HEIGHT/ROWS);
				cells.add(temp);
			}
		runner	= new MazeRunner(0,0,WIDTH/COLS,HEIGHT/ROWS,COLS,ROWS,cells);
		for(int x = 0;x<builders.length;x++)
		{
			Cell temp = cells.get(0);
			builders[x] = new MazeBuilder(temp,COLS,ROWS,cells);
			builders[x].setPosition(x);
		}
		cells.get(cells.size()-1).setEnd(true);		
	}
	
	/**
	 * Determines if the Maze is completed.
	 * @return True if the Maze has been finished.
	 */
	public boolean mazeDone(){return mazeDone;}
	
	/**
	 * Boolean that checks if user wishes to stop playing the Maze.
	 * @return True if user gives up.
	 */
	boolean givenUp(){return givenUp;}
	
	/**
	 * Paints an image of the Maze
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		render(g);		
	}
	
	/**
	 * Gets the MazeRunner used in the Maze
	 * @return MazeRunner
	 */
	public MazeRunner getRunner(){return runner;}
	
	/**
	 * A sleep method for the animation.
	 */
	public void delay(int t)
	{
		try{Thread.sleep(t);}catch(InterruptedException e){e.printStackTrace();}
	}
	
	/**
	 * Renders the Maze before painting paintComponent(Graphics g).
	 * @param g Graphics used to paint the elements in the Maze
	 */
	public void render(Graphics g)
	{
		img=createImage(getWidth(),getHeight());
		offg=img.getGraphics();
		Graphics2D p=(Graphics2D)offg;		
		
		for(Cell c:cells)
		{	
									
			if(c.end()){
				p.setColor(new Color (0,20,0,60));
				p.fillRect(c.getX(), c.getY(), c.getW(), c.getH());
			}
			
			p.setColor(new Color(0,0,100,200));
			if(c.getVisited())p.fillRect(c.getX(), c.getY(), c.getW(), c.getH());
			else{
				p.setColor(Color.WHITE);
				p.fillRect(c.getX(), c.getY(), c.getW(), c.getH());
			}
			if(c.tracked()){
				p.setColor(new Color(100,0,0,150));
				p.fillRect(c.getX(), c.getY(), c.getW(), c.getH());
			}
			
			p.setColor(Color.BLACK);
			if(c.NORTH())p.drawLine(c.getX(), c.getY(), c.getX()+c.getW(), c.getY());
			if(c.EAST()) p.drawLine(c.getX()+c.getW(), c.getY(), c.getX()+c.getW(), c.getY()+c.getH());
			if(c.SOUTH())p.drawLine(c.getX(), c.getY()+c.getH(), c.getX()+c.getW(), c.getY()+c.getH());
			if(c.WEST()) p.drawLine(c.getX(), c.getY(), c.getX(), c.getY()+c.getH());			
		}
		
		if(!mazeDone)
			for(int x = 0;x<builders.length;x++)
			{
				p.setColor(new Color (0,0,0,100));
				Cell temp = builders[x].getCell();
				p.fillRect(temp.getX(), temp.getY(), temp.getW(), temp.getH());	
			}
		
		if(mazeDone){
			p.setColor(new Color(0,100,100,255));
			p.fillOval(runner.getX(), runner.getY(), runner.getW(), runner.getH());
		}
		
		if(lightsOut)hitTheLights(p);
		
		
		if(mazeDone)p.drawString("DONE!!!", 10, HEIGHT+12);
		
		//-------------------------------------------------------------------------------------
		g.drawImage(img,0,0,this);
	}
	
	public void switchLights(boolean b) {lightsOut = b;}

	public void setLightAmp(int value) {lightAmp = value;}
	
	public void hitTheLights(Graphics2D p)
	{
		Area outter = new Area(new Rectangle(0,0,WIDTH,HEIGHT));
		double x0,y0,w0,h0,x1,y1,w1,h1;
		int innerAmp = (int) (lightAmp * 1.2);

		w0 = runner.getW()+(2*lightAmp)-1;
		h0 = runner.getH()+(2*lightAmp)-1;
		x0 = runner.getX()-(w0-runner.getW())/2;
		y0 = runner.getY()-(h0-runner.getH())/2;
		
		w1 = w0+(2*innerAmp)-1;
		h1 = h0+(2*innerAmp)-1;
		x1 = x0-(w1-w0)/2;
		y1 = y0-(h1-h0)/2;
		Area inner = new Area(new Ellipse2D.Double(x0,y0,w0,h0));
		Area oinner = new Area(new Ellipse2D.Double(x1,y1,w1,h1));
		
		outter.subtract(oinner);
		p.setColor(Color.black);
		p.fill(outter);
		
		oinner.subtract(inner);
		p.setColor(new Color(0,0,0,180));
		p.fill(oinner);	
		
		p.setColor(new Color(238,232,170,70));
		p.fill(inner);
		
	}

	/**
	 * Creates the Maze
	 */
	public void createMaze()
	{		
		builders[0].createMaze(cells);
		builders[1].createMaze(cells);
		builders[2].createMaze(cells);
		builders[3].createMaze(cells);	
		
		if(builders[0].mazeDone() 
				&& builders[1].mazeDone()
				&& builders[2].mazeDone()
				&& builders[3].mazeDone())mazeDone = true;		
	}
	
	
	/**
	 * Updates the animation in the JPanel
	 */
	public void update()
	{
		if (!mazeDone)
			createMaze();
		else
			if(!runner.isDone())
				runner.nextDirection();
			else givenUp = false;
					
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getExtendedKeyCode();		
			if (runner.isDone()) {
				if (c == KeyEvent.VK_UP && runner.canAdvance(runner.NORTH))
					runner.moveUp();
				if (c == KeyEvent.VK_DOWN && runner.canAdvance(runner.SOUTH))
					runner.moveDown();
				if (c == KeyEvent.VK_RIGHT && runner.canAdvance(runner.EAST))
					runner.moveRight();
				if (c == KeyEvent.VK_LEFT && runner.canAdvance(runner.WEST))
					runner.moveLeft();
			}		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}


}
