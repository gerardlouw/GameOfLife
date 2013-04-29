package ca.gol.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

import ca.AutomatonVisitor;
import ca.CellularAutomaton;
import ca.gol.GameOfLifeVisitorFixed;
import ca.gol.GameOfLifeVisitorPeriodic;

/**
 * GUI client for Conway's Game of Life. Takes a single command line argument,
 * naming a text file which contains the initial state of the cellular
 * automaton.
 * 
 * @author gerardlouw
 */
public class GameOfLife {
	private static boolean running = false;
	private static int rows;
	private static int columns;
	private static AutomatonVisitor rule;

	/**
	 * Runs the GUI client for Conway's Game of Life.
	 * 
	 * @param args
	 *            single element, name of text file specifying the initial state
	 * @throws IOException
	 *             the specified file is not readable or does not exist
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: java -jar tut04.jar <initfile>");
			System.exit(0);
		}
		
		Scanner input = new Scanner(new FileReader(args[0]));
		rows = input.nextInt();
		columns = input.nextInt();
		input.nextLine();
		final boolean[][] state = new boolean[rows][columns];
		if (input.hasNext()) {
			for (int r = 0; r < rows; r++) {
				String line = input.nextLine();
				for (int c = 0; c < columns; c++)
					if (line.charAt(c) == '0')
						state[r][c] = false;
					else if (line.charAt(c) == '1')
						state[r][c] = true;
					else {
						System.err.println("Error: Invalid input file.");
						System.exit(1);
					}
			}
		}
		input.close();

		// construct CellularAutomaton
		final CellularAutomaton ca = new CellularAutomaton(rows, columns);
		ca.setState(state);
		// create rules for Conway's Game of life
		final GameOfLifeVisitorFixed ruleFixed = new GameOfLifeVisitorFixed();
		final GameOfLifeVisitorPeriodic rulePeriodic = new GameOfLifeVisitorPeriodic();

		rule = ruleFixed;

		// construct panel for cell buttons
		JPanel buttonPanel = new JPanel(new GridLayout(rows, columns));
		final CellButton[][] buttons = new CellButton[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				final int row = r, column = c;
				buttonPanel.add(buttons[r][c] = new CellButton(state[r][c]));
				buttons[r][c].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ca.getCell(row, column).setState(
								!ca.getCell(row, column).getState());
					}
				});
			}

		// construct panel for control of the game
		JPanel controlPanel = new JPanel();

		// construct slider for adjusting frequency
		final JSlider freqSlider = new JSlider(1, 15, 6);

		// construct button for stepping one generation
		final JButton stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ca.setState(ca.accept(rule));
				boolean[][] state = ca.getState();
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						buttons[r][c].setState(state[r][c]);
					}
				}
			}
		});

		// construct button for clearing the state
		final JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ca.setState(new boolean[rows][columns]);
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						buttons[r][c].setState(false);
					}
				}
			}
		});

		// construct drop-down list for choosing the boundary conditions
		final JComboBox<String> ruleCombo = new JComboBox<String>(new String[] {
				"Fixed boundary", "Periodic Boundary" });
		ruleCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				rule = ruleCombo.getSelectedIndex() == 0 ? ruleFixed
						: rulePeriodic;
			}
		});

		// construct button for entering/exiting animation mode
		final JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int r = 0; r < rows; r++)
					for (int c = 0; c < columns; c++) {
						buttons[r][c].setEnabled(running);
					}
				clearButton.setEnabled(running);
				stepButton.setEnabled(running);
				playButton.setText(running ? "Play" : "Pause");
				running = !running;
			}
		});

		// construct thread for animation
		final Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					do {
						try {
							Thread.sleep(3000 / freqSlider.getValue());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (!running);
					ca.setState(ca.accept(rule));
					boolean[][] state = ca.getState();
					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < columns; c++) {
							buttons[r][c].setState(state[r][c]);
						}
					}
				}
			};
		};
		t.start();

		// add buttons to panel
		controlPanel.add(playButton);
		controlPanel.add(stepButton);
		controlPanel.add(ruleCombo);
		controlPanel.add(freqSlider);
		controlPanel.add(clearButton);

		// construct display window
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);

		JFrame frame = new JFrame("Conway's Game of Life");
		frame.setContentPane(mainPanel);

		// set frame parameters
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}

	/**
	 * Special button for representing a cell. Has helper methods for setting
	 * the state.
	 * 
	 * @author gerardlouw
	 */
	private static class CellButton extends JButton {
		private static final long serialVersionUID = -2128869789261315496L;
		private boolean state;

		/**
		 * Constructs a new CellButton representing a cell with the specified
		 * initial state.
		 * 
		 * @param state
		 *            CellButton initial state
		 */
		private CellButton(boolean state) {
			setState(state);
			int dim = 800 / Math.max(rows, columns);
			setPreferredSize(new Dimension(dim, dim));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setState(!CellButton.this.state);
				}
			});
		}

		/**
		 * Sets the state of this CellButton
		 * 
		 * @param state
		 *            CellButton state
		 */
		private void setState(boolean state) {
			setBackground((this.state = state) ? Color.BLACK : Color.WHITE);
		}
	}
}
