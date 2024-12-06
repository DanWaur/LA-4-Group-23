package view;

import controller.YahtzeeController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.DiceValue;
import model.Player;
import model.ScoreCategory;

public class YahtzeeGUI {

	private static YahtzeeController controller;
	private static JTable scorecardTable;
	private static DefaultTableModel tableModel;
	private static JPanel dicePanel;
	private static JButton rollButton;
	private static JButton chooseScoreButton;
	private static JFrame frame;

	private boolean hasCPU;

	public static void main(String[] args) {
		startGame();
	}

	private static void startGame() {
		SwingUtilities.invokeLater(() -> {
			// Create the main frame
			frame = createMainFrame();

			// Initialize player settings
			YahtzeeGUI yahtzeeGUI = new YahtzeeGUI();
			int numPlayers = yahtzeeGUI.initializePlayers(frame);

			// Initialize the controller with the chosen settings
			boolean isCPU = numPlayers == 2 && yahtzeeGUI.hasCPU; // If CPU is enabled, this is true
			controller = new YahtzeeController(numPlayers, isCPU);

			// Create and display the GUI
			frame.add(createScorecardPanel(), BorderLayout.WEST);
			frame.add(createDicePanel(), BorderLayout.CENTER);
			frame.add(createCommandPanel(), BorderLayout.SOUTH);
			frame.setVisible(true);
		});
	}

	private static JFrame createMainFrame() {
		JFrame frame = new JFrame("Yahtzee");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Yahtzee", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
		frame.add(titleLabel, BorderLayout.NORTH);

		return frame;
	}

	private static JPanel createScorecardPanel() {
		String[] headers = createScorecardHeaders();
		int rows = ScoreCategory.values().length + 2; // Categories + totals
		Object[][] data = new Object[rows][headers.length]; // Ensure rows match
		int i = 1;
		for (ScoreCategory cat : ScoreCategory.values()) {
			data[i][0] = cat;
			i++;
		}
		data[i][0] = "Total score";

		tableModel = new DefaultTableModel(data, headers) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		scorecardTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(scorecardTable);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private static JPanel createDicePanel() {
		dicePanel = new JPanel(new GridLayout(5, 1, 10, 10));
		dicePanel.setBackground(Color.LIGHT_GRAY);
		return dicePanel;
	}

	private static JPanel createCommandPanel() {
		JPanel panel = new JPanel();

		// Roll Dice Button
		rollButton = new JButton("Roll Dice");
		rollButton.addActionListener(e -> handleRollDice());
		panel.add(rollButton);

		// Choose Score Button
		chooseScoreButton = new JButton("Choose Score");
		chooseScoreButton.setEnabled(false);
		chooseScoreButton.addActionListener(e -> handleChooseScore());
		panel.add(chooseScoreButton);

		return panel;
	}

	private static void handleRollDice() {
		if (controller.rollDice()) {
			updateDicePanel(); // Update the dice display
			displayPotentialScores(); // Update potential scores for the current player
			chooseScoreButton.setEnabled(true); // Enable the "Choose Score" button
		} else {
			JOptionPane.showMessageDialog(null, "No rolls left! Please choose a score.");
		}
	}

	private static String[] createScorecardHeaders() {
		int playerCount = controller.getPlayers().size(); // Fetch the number of players dynamically
		String[] headers = new String[playerCount + 1];
		headers[0] = "Category"; // First column header
		for (int i = 1; i <= playerCount; i++) {
			headers[i] = controller.getPlayers().get(i - 1).getName(); // Player names from the controller
		}
		return headers;
	}

	private static void handleChooseScore() {
		if (!controller.hasPlayerRolled(controller.getCurrentPlayerName())) {
			JOptionPane.showMessageDialog(null, "You must roll the dice at least once before choosing a score.");
			return;
		}

		String currentPlayerName = controller.getCurrentPlayerName(); // Get the current player's name
		List<String> selectableCategories = controller.getSelectableCategories(currentPlayerName); // Fetch selectable
																									// categories

		if (selectableCategories.isEmpty()) {
			JOptionPane.showMessageDialog(null, "All categories scored!");
			return;
		}

		String[] categoryArray = selectableCategories.toArray(new String[0]); // Convert list to array for input dialog
		String selectedCategory = (String) JOptionPane.showInputDialog(null, "Choose a scoring category:",
				"Score Selection", JOptionPane.QUESTION_MESSAGE, null, categoryArray, categoryArray[0]);

		if (selectedCategory == null) {
			// User pressed cancel
			return;
		}

		// Delegate scoring to the controller
		boolean scoredSuccessfully = controller.scoreSelectedCategory(currentPlayerName, selectedCategory);
		System.out.println("Score successful: " + scoredSuccessfully);
		System.out.println("Current Player before move: " + controller.getCurrentPlayerName());

		if (scoredSuccessfully) {
			JOptionPane.showMessageDialog(null, "Score saved!");
			updateScores(); // Update the scores table
			updateDicePanel(); // Update the dice panel
			moveToNextPlayer(); // Move to the next player
			chooseScoreButton.setEnabled(false); // Disable the button for the next player
		} else {
			JOptionPane.showMessageDialog(null, "Category already scored!");
		}
	}

	private static void updateDicePanel() {
		dicePanel.removeAll(); // Clear existing dice labels from the panel

		// Get the current player's dice information from the controller
		List<DiceValue> diceFaces = controller.getCurrentDiceFaces(); // Retrieve dice faces via the controller
		List<Boolean> diceHolds = controller.getCurrentDiceHolds(); // Retrieve dice hold states via the controller

		for (int i = 0; i < diceFaces.size(); i++) {
			JLabel diceLabel;
			if (diceFaces.get(i) == null) {
				// Show a placeholder image for unset dice
				diceLabel = new JLabel(new ImageIcon("resources/dice-ONE.png"), SwingConstants.CENTER);
			} else {
				// Create a label for the dice face and hold state
				diceLabel = createDiceLabel(diceFaces.get(i), diceHolds.get(i), i);
			}
			dicePanel.add(diceLabel); // Add the label to the panel
		}

		dicePanel.revalidate(); // Refresh the layout
		dicePanel.repaint(); // Repaint the panel
	}

	private static void hideDicePanel() {
		dicePanel.removeAll(); // Clear existing dice labels from the panel
	}

	private static JLabel createDiceLabel(DiceValue face, boolean isHeld, int index) {
		String imagePath = "resources/dice-" + face.name() + ".png";
		ImageIcon icon = new ImageIcon(
				new ImageIcon(imagePath).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon, SwingConstants.CENTER);

		if (isHeld) {
			label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		}

		label.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				String currentPlayerName = controller.getCurrentPlayerName();
				if (!controller.hasPlayerRolled(currentPlayerName)) {
					JOptionPane.showMessageDialog(null, "You must roll the dice before toggling!");
					return;
				}
				if (controller.getCurrentPlayerName() == "CPU") {
					JOptionPane.showMessageDialog(null, "You cannot hold the dice when it is CPU's turn!");
					return;
				}
				controller.toggleDice(Collections.singletonList(index));
				updateDicePanel();
			}
		});

		return label;
	}

	private static void updateScores() {
		Map<String, Integer> playerScores = controller.getPlayerScores();
		List<Player> players = controller.getPlayers();

		// Update the table
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			int columnIndex = i + 1; // Player's column index
			String playerName = player.getName();
			Integer totalScore = playerScores.getOrDefault(playerName, 0); // Default to 0 if not found

			// Populate scores for each category
			for (ScoreCategory category : ScoreCategory.values()) {
				int rowIndex = category.ordinal() + 1; // Row index for the category
				Integer score = controller.getCategoryScoreForPlayer(playerName, category);
				tableModel.setValueAt(score != null ? score : "-", rowIndex, columnIndex);
			}

			// Update total score in the last row
			int totalRowIndex = ScoreCategory.values().length + 1;
			tableModel.setValueAt(totalScore, totalRowIndex, columnIndex);
		}
	}

	private static void displayPotentialScores() {
		String currentPlayerName = controller.getCurrentPlayerName(); // Get the current player's name

		if (!controller.hasPlayerRolled(currentPlayerName)) {
			System.out.println("Dice have not been rolled yet. Skipping potential scores update.");
			return; // Skip updating scores if dice haven't been rolled
		}

		Map<ScoreCategory, Integer> potentialScores = controller.getPotentialScores(currentPlayerName); // Fetch
																										// potential
																										// scores
		int columnIndex = controller.getCurrentPlayerIndex() + 1; // Current player's column index

		for (Map.Entry<ScoreCategory, Integer> entry : potentialScores.entrySet()) {
			ScoreCategory category = entry.getKey();
			Integer score = entry.getValue();
			tableModel.setValueAt(score != null ? score : "-", category.ordinal() + 1, columnIndex);
		}

		tableModel.fireTableDataChanged(); // Notify the table to refresh
	}

	// Initialize players list
	private int initializePlayers(JFrame frame) {
		// prompt for cpu first, no need to prompt number of players if it'll be
		// irrelevant
		boolean cpuBool = promptForBool(frame, "Will this game have a CPU player?");
		this.hasCPU = cpuBool;
		if (this.hasCPU) {
			return 2; // we know if it's a CPU game, we can return that there's 2 players
		}

		int numPlayers = promptForNumber(frame, "Select the Number of Players:", 2, 4);
		if (numPlayers == -1)
			throw new IllegalStateException("Game initialization canceled.");

		return numPlayers;
	}

	private boolean promptForBool(JFrame frame, String message) {
		int input = JOptionPane.showOptionDialog(frame, message, "Add CPU player?", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, 1);
		return input == 0; // default boolean returned should be false (for no)
	}

	private int promptForNumber(JFrame frame, String message, int min, int max) {
		// Create an array of options based on the range from min to max
		int range = max - min + 1; // Calculate the total number of options
		String[] options = new String[range]; // Initialize the options array with the size of the range

		// Fill the options array with numbers from min to max
		for (int i = 0; i < range; i++) {
			options[i] = String.valueOf(min + i); // Convert each number to a String
		}

		// Show the input dialog with the options
		String input = (String) JOptionPane.showInputDialog(frame, message, "Select Number",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		// If input is null (i.e., user canceled), return -1; otherwise, return the
		// selected number
		if (input == null) {
			return -1; // User canceled
		} else {
			return Integer.parseInt(input); // Parse and return the selected number
		}
	}

	private static void moveToNextPlayer() {
		controller.advanceToNextPlayer(); // Advance to the next player in the controller
		
		if (controller.isGameOver()) {
			handleGameOver();
			return;
		}

		String nextPlayerName = controller.getCurrentPlayerName(); // Fetch the next player's name
		JOptionPane.showMessageDialog(null, "It's now " + nextPlayerName + "'s turn!");

		updateDicePanel(); // Refresh the dice display
		updateScores(); // Update the scorecard with the new player's scores
		displayPotentialScores(); // Show potential scores for the new player

		if (nextPlayerName == "CPU") {
			rollButton.setEnabled(false);
			rollButton.setEnabled(false);

			cpuTurn();
		} else {
			rollButton.setEnabled(true); // Enable rolling for the new player
			chooseScoreButton.setEnabled(false); // Disable choosing score until they roll
			hideDicePanel();
		}
	}

	private static void cpuTurn() {
		ScoreCategory aim = controller.getCpuAim();
		updateDicePanel(); // Refresh the dice display
		updateScores(); // Update the scorecard with the new player's scores
		displayPotentialScores(); // Show potential scores for the new player
		System.out.println("aiming for: " + aim);

		// ResultHolder is used because actionPerformed does not allow a
		// non final immutable object to be modified
		ScoreCategory[] resultHolder = new ScoreCategory[1];
		resultHolder[0] = null;

		Timer timer = new Timer(750, null);

		ActionListener decisions = new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				System.out.println(controller.getCurrentPlayerName());
				if (resultHolder[0] == null) {
					resultHolder[0] = controller.iterateCpuChoices(aim);
					updateDicePanel(); // Refresh the dice display
					updateScores(); // Update the scorecard with the new player's scores
					displayPotentialScores(); // Show potential scores for the new player
				} else {
					controller.chooseScore(resultHolder[0]);
					timer.stop();
					JOptionPane.showMessageDialog(null, "CPU scored in " + resultHolder[0]);

					updateDicePanel(); // Refresh the dice display
					updateScores(); // Update the scorecard with the new player's scores
					displayPotentialScores(); // Show potential scores for the new player

					moveToNextPlayer();

				}

			}
		};

		timer.addActionListener(decisions);
		timer.start();

	}

	private static void handleGameOver() {
		// Get winners and scores, already sorted by score in descending order
		Map<String, Integer> playerScores = controller.getPlayerScores();
		List<String> winners = new ArrayList<>();
		int highestScore = (int) playerScores.values().toArray()[0];
		for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
			if (entry.getValue() == highestScore) {
				winners.add(entry.getKey());
			}
		}

		StringBuilder message = new StringBuilder("Game Over!\n");
		if (winners.size() == 1) {
			message.append("Winner: ").append(winners.get(0)).append(" with a score of ").append(highestScore)
					.append("!");
		} else {
			message.append("It's a tie between:\n");
			for (String winner : winners) {
				message.append(winner).append("\n");
			}
			message.append("with a score of ").append(highestScore).append("!");
		}

		// Ask if the user wants to play again
		int option = JOptionPane.showConfirmDialog(null, message.append("\nDo you want to play again?").toString(),
				"Game Over", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			// close previous frame, open new one
			YahtzeeGUI.frame.dispose();
			startGame(); // Restart the game if Yes is selected
		} else {
			System.exit(0); // Exit the application if No is selected
		}
	}

}
