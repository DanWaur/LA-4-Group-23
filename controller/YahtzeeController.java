package controller;

import model.Player;
import model.Cpu;
import model.ScoreCategory;
import model.YahtzeeGame;
import view.YahtzeeGUI;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class YahtzeeController {
    private final YahtzeeGame model;
    // private Player currentPlayer;

    public YahtzeeController(int playerCount, boolean hasCPU) {
        this.model = new YahtzeeGame(playerCount, hasCPU);
    }

    public void handleRollDice() {
        model.rollDice();
    }

    public void handleChooseScore() {
        boolean chooseScoreBool = model.chooseScore(null);
        if (chooseScoreBool == false) {
            // gui.showMessage("You must roll at least once before choosing a score.");
            return;
        }

        // boolean scoreChosen = promptPlayerToChooseScore();
        // if (scoreChosen) {
        //     moveToNextPlayer();
        // }
    }

    // private void displayPotentialScores() {
    //     int playerColumnIndex = players.indexOf(currentPlayer) + 1; // Column in the scorecard

    //     for (ScoreCategory category : ScoreCategory.values()) {
    //         if (currentPlayer.getScoreForCategory(category) == null) { // Check if the category is unscored
    //             int potentialScore = currentPlayer.calculateScoreForCategory(category); // Calculate the score
    //             gui.updateScore(category.ordinal() + 1, playerColumnIndex, String.valueOf(potentialScore));
    //         }
    //     }
    // }

    // private boolean promptPlayerToChooseScore() {
    //     List<String> selectableCategories = new ArrayList<>();

    //     for (ScoreCategory category : ScoreCategory.values()) {
    //         if (currentPlayer.getScoreForCategory(category) == null) { // Check if the category is unscored
    //             int potentialScore = currentPlayer.calculateScoreForCategory(category); // Calculate potential score
    //             selectableCategories.add(category.name() + " (" + potentialScore + ")");
    //         }
    //     }

    //     // here we can add end game logic (but need to make sure both players == selectableCategories.isEmpty())
    //     if (selectableCategories.isEmpty()) {
    //         gui.showMessage("All categories scored!");
    //         return false;
    //     }

    //     String selected = gui.showInputDialog("Choose a scoring category:", selectableCategories.toArray(new String[0]));
    //     if (selected == null) return false;

    //     String selectedCategoryName = selected.split(" ")[0];
    //     ScoreCategory chosenCategory = ScoreCategory.valueOf(selectedCategoryName);

    //     boolean success = currentPlayer.scoreCategory(chosenCategory); // Score the chosen category
    //     if (!success) {
    //         gui.showMessage("This category has already been scored!");
    //         return false;
    //     }

    //     return true;
    // }

    // private void moveToNextPlayer() {
    //     int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
    //     currentPlayer = players.get(nextPlayerIndex);
    //     currentPlayer.prepareForNextTurn();

    //     gui.updateDiceDisplay(currentPlayer);
    //     gui.showMessage("It's now Player " + (nextPlayerIndex + 1) + "'s turn!");
    // }

    // The method to toggle the dice hold state
    public void handleToggleDice(int index) {
        if (index < 0 || index >= 5) {
            return; // Invalid index, just return
        }

        // Get the dice index and toggle its hold state through the Player class
        List<Integer> dicePos = new ArrayList<>();
        dicePos.add(index); // Add the clicked dice index to the list
        boolean validToggle = model.toggleDice(dicePos);
        if (validToggle == false) { // Prevent toggling if the player has not rolled yet
            System.out.println("Cannot toggle dice before rolling.");
            return; 
        }
        // // Update the dice display to reflect the change
        // gui.updateDiceDisplay(currentPlayer);
    }
    
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        Set<String> playerMapKeys = model.getPlayerScores().keySet();
        String[] players = playerMapKeys.toArray(new String[playerMapKeys.size()]);
        for (String name : players) {
            playerNames.add(name);
        }
        // for (Player player : model.getPlayerScores()) {
        //     playerNames.add("Player " + (players.indexOf(player) + 1));
        // }
        return playerNames;
    }
}
