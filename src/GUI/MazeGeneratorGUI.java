package GUI;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import maze.MazeV2;

/**
 * In this window, the user-defined Maze is created and then solved by the user.
 * @author Erick
 *
 */
public class MazeGeneratorGUI extends JFrame{
	boolean start, solving, solvableMaze, lightsOut;
	

	/**
	 * Create the application.
	 */
	public MazeGeneratorGUI(MazeV2 m) {	
		initialize(m);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(MazeV2 m) {
		this.setBounds(15, 15, 660, 715);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setTitle("MazeV2");
		
		MazeV2 maze = m;
		maze.setBounds(10, 32, 600, 600);
		this.getContentPane().add(maze);
		JButton btnStep = new JButton("LIGHTS!");
		int lightRatio = (int)(maze.getRunner().getW()*.5);
		
		JButton btnStop = new JButton("Start");
		JSlider slider = new JSlider();
		
		btnStop.addKeyListener(maze);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start = !start;
				solving = maze.mazeDone() && maze.getRunner().isDone() && !start;
				maze.getRunner().setDone(false);
				String label = !start ? (!maze.mazeDone() ?"Start": "Solve"):"Pause";
				btnStop.setText(label);
				slider.setEnabled(false);
				new Thread(){
					@Override
					public void run()
					{
						while(!maze.mazeDone() && start)
						{
							
							maze.update();				//==========================================
							maze.repaint();				// This section of code allows the graphics to
							maze.delay(40);				// go a little faster...											
							if(maze.mazeDone()){
								start = false;
								btnStop.setText("Solve");
							}
						}
						
						
						while(!maze.getRunner().isDone() && start)
						{
							if(!solvableMaze)slider.setEnabled(false);
							maze.update();
							maze.repaint();
							maze.delay(40);
							if(maze.getRunner().isDone()){
								btnStop.setText("Solve");
								solving = lightsOut = solvableMaze = true;								
								maze.switchLights(lightsOut);
								btnStep.setEnabled(true);
								slider.setEnabled(true);
								maze.setLightAmp(slider.getValue());								
							}
						}
						
						while(solving && start)
						{
							maze.setLightAmp(slider.getValue());
							slider.setFocusable(false);
							maze.update();
							maze.repaint();
							maze.delay(40);
							if(maze.getRunner().getCurrent().end()){
								maze.update();
								maze.repaint();
								solving = false;
								start = false;								
								JOptionPane.showMessageDialog(null, "You solved the Maze!", "Maze Solved", JOptionPane.INFORMATION_MESSAGE);
								
							}
						}	
					}
				}.start();
				}
		});		
		
		JLabel lblMax = new JLabel("MAX");
		lblMax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMax.setBounds(613, 166, 26, 14);
		getContentPane().add(lblMax);
		
		JLabel lblMin = new JLabel("MIN");
		lblMin.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMin.setBounds(613, 496, 23, 14);
		getContentPane().add(lblMin);
		slider.addKeyListener(maze);
		slider.setValue(lightRatio);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setBounds(610, 177, 27, 322);
		getContentPane().add(slider);
	
		btnStop.setBounds(54, 643, 89, 23);
		this.getContentPane().add(btnStop);
		
		btnStep.setEnabled(false);
		btnStep.addKeyListener(maze);
		btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lightsOut = !lightsOut;
				maze.switchLights(lightsOut);
				playMaze(maze,slider);
			}
		});
		btnStep.setBounds(197, 643, 89, 23);
		this.getContentPane().add(btnStep);
		
		JButton btnExit = new JButton("Calculate");
		btnExit.addKeyListener(maze);
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start = false;
				
				if(!maze.getRunner().isDone())
				{
					do maze.update();
					while(!maze.getRunner().isDone());
					solving = lightsOut = solvableMaze = true;	
					maze.switchLights(lightsOut);
					btnStep.setEnabled(true);
					slider.setEnabled(true);
					btnStop.setText("Solve");
				}
				
				playMaze(maze,slider);
				
				maze.repaint();
			}
		});
		btnExit.setBounds(483, 643, 89, 23);
		this.getContentPane().add(btnExit);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start = solving = lightsOut = solvableMaze = false;
				maze.switchLights(lightsOut);
				btnStep.setEnabled(false);
				slider.setValue(lightRatio);
				slider.setEnabled(false);
				maze.reset();
				maze.repaint();
			}
		});
		btnReset.setBounds(340, 643, 89, 23);
		this.getContentPane().add(btnReset);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 97, 21);
		this.getContentPane().add(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmBackToSettings = new JMenuItem("Back to Settings");
		mntmBackToSettings.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
				MazeSettingsGUI settings = new MazeSettingsGUI();
				settings.setVisible(true);
			}
		});
		mnNewMenu.add(mntmBackToSettings);
		
		JMenuItem mntmOpenMaze = new JMenuItem("Import Maze");
		mnNewMenu.add(mntmOpenMaze);
		
		JMenuItem mntmSaveMaze = new JMenuItem("Save Maze");
		mnNewMenu.add(mntmSaveMaze);
		
	}
	
	/**
	 * Generates the Threads required to create the animation for playing the maze.
	 * @param maze The Maze to be solved.
	 */
	private void playMaze(MazeV2 maze,JSlider slider)
	{
		new Thread(){
			@Override
			public void run()
			{
				while(solving)
				{
					maze.update();
					maze.repaint();
					maze.delay(40);
					maze.setLightAmp(slider.getValue());
					slider.setFocusable(false);
					if(maze.getRunner().getCurrent().end()){
						maze.update();
						maze.reset();
						solving = false;
						JOptionPane.showMessageDialog(null, "You solved the Maze!", "Maze Solved", JOptionPane.INFORMATION_MESSAGE);						
					}
				}	
			}
		}.start();
	}
}
