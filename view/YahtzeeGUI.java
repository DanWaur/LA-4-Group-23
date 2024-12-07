/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

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

	/**
	 * Initializes and starts the Yahtzee game GUI.
	 * 
	 * GUI components include:
	 * - Scorecard Panel: Displays scores and categories.
	 * - Dice Panel: Displays dice for gameplay.
	 * - Command Panel: Provides game control buttons..
	 */
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

	/**
	 * Creates and initializes the main JFrame for the Yahtzee application.
	 * Frame's properties, including size, layout, and title,
	 * and adds a title label to the frame.
	 * 
	 * @return the initialized JFrame instance.
	 */
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

	/**
	 * Creates and initializes the scorecard panel.
	 * Generates a table with headers and rows for scoring categories, including totals.
	 * Ensures the table is non-editable and wraps it in a scroll pane for display.
	 * 
	 * @return the initialized JPanel containing the scorecard table.
	 */
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

	/**
	 * Creates and initializes the dice panel.
	 * Configures a grid layout with five rows and a light gray background
	 * for displaying the dice.
	 * 
	 * @return the initialized JPanel for the dice display.
	 */
	private static JPanel createDicePanel() {
		dicePanel = new JPanel(new GridLayout(5, 1, 10, 10));
		dicePanel.setBackground(Color.LIGHT_GRAY);
		return dicePanel;
	}

	/**
	 * Creates and initializes the command panel.
	 * Adds buttons for rolling the dice and choosing a score, with appropriate
	 * action listeners for their functionalities.
	 * 
	 * @return the initialized JPanel containing the command buttons.
	 */
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

	/**
	 * Handles the logic for rolling the dice during a player's turn.
	 * Updates the dice display and calculates potential scores if rolls are available.
	 * Displays a warning message if no rolls are left and prompts the player to choose a score.
	 */
	private static void handleRollDice() {
		if (controller.rollDice()) {
			updateDicePanel(); // Update the dice display
			displayPotentialScores(); // Update potential scores for the current player
			chooseScoreButton.setEnabled(true); // Enable the "Choose Score" button
		} else {
			JOptionPane.showMessageDialog(null, "No rolls left! Please choose a score.");
		}
	}

	/**
	 * Creates the headers for the scorecard table dynamically.
	 * @return an array of strings representing the column headers for the scorecard table.
	 */
	private static String[] createScorecardHeaders() {
		int playerCount = controller.getPlayers().size(); // Fetch the number of players dynamically
		String[] headers = new String[playerCount + 1];
		headers[0] = "Category"; // First column header
		for (int i = 1; i <= playerCount; i++) {
			headers[i] = controller.getPlayers().get(i - 1).getName(); // Player names from the controller
		}
		return headers;
	}

	/**
	 * Handles the logic for selecting and scoring a category.
	 * Ensures the player has rolled the dice at least once before scoring, and validates 
	 * that there are selectable categories available. Prompts the player to choose a 
	 * category, delegates the scoring process to the controller, and updates the game state.
	 */
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

	/**
	 * Updates the dice panel to display the current player's dice values and hold states.
	 * Clears the panel and adds new labels for each die, reflecting its face and whether it is held.
	 * If a die has not been rolled yet, displays a placeholder image.
	 * 
	 * Ensures the panel layout is refreshed and repainted to reflect the updated dice state.
	 */
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

	/**
	 * Clears all dice labels from the dice panel, effectively hiding it.
	 */
	private static void hideDicePanel() {
		dicePanel.removeAll(); // Clear existing dice labels from the panel
	}

	/**
	 * Creates a JLabel representing a die with its current face value and hold state.
	 * Displays an image corresponding to the die face and applies a red border if the die is held.
	 * Adds a mouse listener to toggle the hold state of the die when clicked, ensuring 
	 * the player has rolled the dice and it is not the CPU's turn before toggling.
	 * 
	 * @param face the face value of the die to display.
	 * @param isHeld a boolean indicating if the die is currently held.
	 * @param index the index of the die in the dice list, used for toggling.
	 * @return a JLabel representing the die.
	 */
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

	/**
	 * Updates the scores displayed in the scorecard table for all players.
	 * Retrieves the players and their respective scores from the controller,
	 * and populates the table with scores for each category and the total score.
	 * 
	 */
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

	/**
	 * Displays the potential scores for the current player based on their dice roll.
	 * Fetches the potential scores for each category from the controller and updates the
	 * scorecard table with these values. Skips the update if the dice have not been rolled.
	 * dash ("-") if a score is not available
	 */
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

	/**
	 * Initializes the players for the Yahtzee game by prompting the user.
	 * First, determines if the game will include a CPU player and, if so, sets the player count to 2.
	 * If no CPU is added, prompts the user to select human players (2 to 4).
	 * Throws an exception if the initialization process is canceled.
	 * 
	 * @param frame the parent frame for the prompt dialogs.
	 * @return the number of players participating in the game.
	 */
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

	/**
	 * Prompts the user with a yes/no question.
	 * Used to check if a CPU player will be added to the game.
	 * 
	 * @param frame the parent frame for the prompt dialog.
	 * @param message the message displayed in the prompt dialog.
	 * @return true if the user selects "Yes," false otherwise.
	 */
	private boolean promptForBool(JFrame frame, String message) {
		int input = JOptionPane.showOptionDialog(frame, message, "Add CPU player?", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, 1);
		return input == 0; // default boolean returned should be false (for no)
	}

	/**
	 * Prompts the user to select a number within a specified range using a dialog box.
	 * Displays the range of numbers as options in a drop-down menu, allowing the user to choose one.
	 * 
	 * @param frame the parent frame for the prompt dialog.
	 * @param message the message displayed in the dialog.
	 * @param min the minimum number in the range of options.
	 * @param max the maximum number in the range of options.
	 * @return the number selected by the user, or -1 if the dialog is canceled.
	 */
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

	/**
	 * Advances the game to the next player's turn.
	 * Checks if the game is over after advancing; if so, handles the end-game logic.
	 * Otherwise, displays a message indicating the next player's turn and updates the game state:
	 * - Refreshes the dice display and scorecard for the new player.
	 * - Displays potential scores for the new player.
	 * - Adjusts button states based on whether the next player is a CPU or human.
	 * 
	 * If the next player is the CPU, disables player interaction and starts the CPU's turn automatically.
	 * For human players, enables the roll button and resets the dice panel.
	 */
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

	/**
	 * Executes the CPU player's turn in the game.
	 * Determines the CPU's target scoring category and iteratively refines its choices
	 * using a timer to simulate decision-making.
	 * 
	 * - Once finalized, the CPU selects the score category, displays a message with the chosen category,
	 *   and updates the game state.
	 * - Advances the game to the next player's turn after completing the CPU's actions.
	 * 
	 */
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

	/**
	 * Handles the end of the game by determining the winner(s) and displaying the final results.
	 * Retrieves player scores, identifies the highest score, and checks for ties.
	 * Constructs and displays a message with the winner(s) and their score(s).
	 * 
	 * Prompts the user to decide Yes/No:
	 * - If "Yes," disposes of the current game frame and restarts the game.
	 * - If "No," exits the application.
	 */
	private static void handleGameOver() {
		Map<String, Integer> playerScores = controller.getPlayerScores();
		List<Integer> scores = new ArrayList<>(playerScores.values());
		int highestScore = Collections.max(scores);
		List<String> winners = new ArrayList<>();
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
