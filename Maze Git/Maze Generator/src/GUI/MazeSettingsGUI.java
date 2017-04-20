package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import maze.Maze;

/**
 * Creates window in which user chooses the columns and and a maze should have.
 * @author Erick
 *
 */
public class MazeSettingsGUI extends JFrame{
	boolean toGenerate;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MazeSettingsGUI window = new MazeSettingsGUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MazeSettingsGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the MazeSettingsGUI.
	 */
	private void initialize() {
		this.setBounds(100, 100, 450, 206);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		
		JLabel lblMazeGenerator = new JLabel("Maze Generator");
		lblMazeGenerator.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		lblMazeGenerator.setBounds(140, 11, 154, 23);
		this.getContentPane().add(lblMazeGenerator);
		
		JLabel lblNumberOfColumns = new JLabel("Number of Columns:");
		lblNumberOfColumns.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
		lblNumberOfColumns.setBounds(160, 58, 134, 15);
		this.getContentPane().add(lblNumberOfColumns);
		
		JLabel lblNumberOfRows = new JLabel("Number of Rows:");
		lblNumberOfRows.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
		lblNumberOfRows.setBounds(160, 84, 134, 15);
		this.getContentPane().add(lblNumberOfRows);
		
		JTextField cols = new JTextField();
		cols.setText("10");
		cols.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		cols.setBounds(322, 55, 59, 20);
		this.getContentPane().add(cols);
		
		JTextField rows = new JTextField();
		rows.setText("10");
		rows.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		rows.setBounds(322, 81, 59, 20);
		this.getContentPane().add(rows);
		
		Maze maze = new Maze(20,20,120,120);
		this.getContentPane().add(maze);
		
		new Thread(){
			@Override
			public void run()
			{
				while(!toGenerate)
				{
					maze.update();				//==========================================
					maze.repaint();				// This section of code allows the graphics to
					maze.delay(40);
					if(maze.getRunner().isDone())
						maze.reset();
				}
			}
		}.start();
		
		JButton btnNewButton = new JButton("Create");
		btnNewButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int col = 10,row = 10;
				Maze generate;
				try{
					col = Integer.parseInt(cols.getText());
					row = Integer.parseInt(rows.getText());
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, "Invalid Input.\nSetting default Values.", "ERROR", JOptionPane.ERROR_MESSAGE);
					col = 20;
					row = 20;
				}
				
				generate = new Maze(col,row,600,600);
				MazeGeneratorGUI theMaze = new MazeGeneratorGUI(generate);
				theMaze.setVisible(true);
				toGenerate = true;
				dispose();
				}
		});
		btnNewButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		btnNewButton.setBounds(172, 123, 89, 23);
		this.getContentPane().add(btnNewButton);
	}
}
