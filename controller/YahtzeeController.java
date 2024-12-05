package controller;

import model.Player;
import model.ScoreCategory;
import view.YahtzeeGUI;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class YahtzeeController {
    private final List<Player> players;
    private Player currentPlayer;
    private final YahtzeeGUI gui;

    public YahtzeeController(YahtzeeGUI gui) {
        this.gui = gui;
        this.players = gui.getPlayers();  // Retrieve players from YahtzeeGUI
    }

    public void initializeGame(JFrame frame) {
        if (players.isEmpty()) {
            int numPlayers = promptForNumber(frame, "Select the Number of Players:", 2, 4);
            if (numPlayers == -1) throw new IllegalStateException("Game initialization canceled.");

            int numCPUs = promptForNumber(frame, "Select the Number of Computers:", 0, numPlayers);
            if (numCPUs == -1) throw new IllegalStateException("Game initialization canceled.");

            // Add human players
            for (int i = 0; i < numPlayers - numCPUs; i++) players.add(new Player());
            // Add CPU players (if any)
            for (int i = 0; i < numCPUs; i++) players.add(new Player()); // CPU logic can be added here
        }

        if (!players.isEmpty()) {
            currentPlayer = players.get(0); // Start with the first player
        }
    }

    private int promptForNumber(JFrame frame, String message, int min, int max) {
        String[] options = new String[max - min + 1];
        for (int i = min; i <= max; i++) {
            options[i - min] = String.valueOf(i);
        }

        String input = (String) JOptionPane.showInputDialog(frame, message, "Players",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return input == null ? -1 : Integer.parseInt(input);
    }

    public void handleRollDice() {
        if (currentPlayer.getRollsLeft() >= 0) {
            currentPlayer.rollDice();
            currentPlayer.setHasRolled(true);
            gui.updateDiceDisplay(currentPlayer);

            displayPotentialScores();
            if (currentPlayer.getRollsLeft() == 0) {
                gui.showMessage("Rolls finished. Please choose a score.");
            }
        } else {
            gui.showMessage("No rolls left! Please choose a score.");
        }
    }

    public void handleChooseScore() {
        if (!currentPlayer.hasRolled()) {
            gui.showMessage("You must roll at least once before choosing a score.");
            return;
        }

        boolean scoreChosen = promptPlayerToChooseScore();
        if (scoreChosen) {
            moveToNextPlayer();
        }
    }

    private void displayPotentialScores() {
        int playerColumnIndex = players.indexOf(currentPlayer) + 1; // Column in the scorecard

        for (ScoreCategory category : ScoreCategory.values()) {
            if (currentPlayer.getScoreForCategory(category) == null) { // Check if the category is unscored
                int potentialScore = currentPlayer.calculateScoreForCategory(category); // Calculate the score
                gui.updateScore(category.ordinal() + 1, playerColumnIndex, String.valueOf(potentialScore));
            }
        }
    }

    private boolean promptPlayerToChooseScore() {
        List<String> selectableCategories = new ArrayList<>();

        for (ScoreCategory category : ScoreCategory.values()) {
            if (currentPlayer.getScoreForCategory(category) == null) { // Check if the category is unscored
                int potentialScore = currentPlayer.calculateScoreForCategory(category); // Calculate potential score
                selectableCategories.add(category.name() + " (" + potentialScore + ")");
            }
        }

        // here we can add end game logic (but need to make sure both players == selectableCategories.isEmpty())
        if (selectableCategories.isEmpty()) {
            gui.showMessage("All categories scored!");
            return false;
        }

        String selected = gui.showInputDialog("Choose a scoring category:", selectableCategories.toArray(new String[0]));
        if (selected == null) return false;

        String selectedCategoryName = selected.split(" ")[0];
        ScoreCategory chosenCategory = ScoreCategory.valueOf(selectedCategoryName);

        boolean success = currentPlayer.scoreCategory(chosenCategory); // Score the chosen category
        if (!success) {
            gui.showMessage("This category has already been scored!");
            return false;
        }

        return true;
    }

    private void moveToNextPlayer() {
        int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
        currentPlayer = players.get(nextPlayerIndex);
        currentPlayer.prepareForNextTurn();

        gui.updateDiceDisplay(currentPlayer);
        gui.showMessage("It's now Player " + (nextPlayerIndex + 1) + "'s turn!");
    }

    // The method to toggle the dice hold state
    public void handleToggleDice(int index) {
        if (index < 0 || index >= currentPlayer.getDiceFaces().size()) {
            return; // Invalid index, just return
        }
    
        // Check if the player has rolled before allowing dice toggling
        if (!currentPlayer.hasRolled()) {
            System.out.println("Cannot toggle dice before rolling.");
            return; // Prevent toggling if the player has not rolled yet
        }
    
        // Get the dice index and toggle its hold state through the Player class
        List<Integer> dicePos = new ArrayList<>();
        dicePos.add(index); // Add the clicked dice index to the list
    
        // Call the toggleDice method in Player class
        currentPlayer.toggleDice(dicePos); // Toggling dice state using the Player class method
    
        // Update the dice display to reflect the change
        gui.updateDiceDisplay(currentPlayer);
    }
    
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            playerNames.add("Player " + (players.indexOf(player) + 1));
        }
        return playerNames;
    }
}
