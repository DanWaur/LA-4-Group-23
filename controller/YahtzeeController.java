/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

package controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import model.DiceValue;
import model.Player;
import model.ScoreCategory;
import model.YahtzeeGame;

public class YahtzeeController {
    private YahtzeeGame game;

    // Initializes the controller with the number of players and CPU mode
    public YahtzeeController(int numPlayers, boolean cpuMode) {
        game = new YahtzeeGame(numPlayers, cpuMode);
    }

    // Rolls dice for the current player
    public boolean rollDice() {
        return game.rollDice();
    }

    // Toggles the hold state of selected dice
    public boolean toggleDice(List<Integer> diceIndices) {
        return game.toggleDice(diceIndices);
    }

    // Scores the specified category for the current player
    public boolean chooseScore(ScoreCategory category) {
        return game.chooseScore(category);
    }
    
    // Returns the CPU's target category for scoring
    public ScoreCategory getCpuAim() {
        return game.getCpuAim();
    }
    
    // Simulates the CPU's choice-making process
    public ScoreCategory iterateCpuChoices(ScoreCategory aimFor) {
        return game.iterateCpuChoices(aimFor);
    }

    // Retrieves the scores of all players
    public Map<String, Integer> getPlayerScores() {
        return game.getPlayerScores();
    }

    // Checks if the game has ended
    public boolean isGameOver() {
        return game.isGameOver();
    }

    // Gets the current round number
    public int getCurrentRound() {
        return game.getCurrentRound();
    }

    // Retrieves an unmodifiable list of players to prevent escaping references
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(game.getPlayers());
    }

    // Gets the score of a player by their index
    public int getPlayerScoreByIndex(int playerIndex) {
        return game.getPlayerScore(playerIndex);
    }

    // Gets the score for a specific category for a player
    public Integer getCategoryScoreForPlayer(String playerName, ScoreCategory category) {
        return game.getCategoryScoreForPlayer(playerName, category);
    }

    // Retrieves the selectable categories for a player
    public List<String> getSelectableCategories(String playerName) {
        return game.getSelectableCategories(playerName);
    }

    // Returns the current player's name
    public String getCurrentPlayerName() {
        return game.getCurrentPlayerName();
    }

    // Scores a selected category for a specific player
    public boolean scoreSelectedCategory(String playerName, String selectedCategory) {
        String categoryName = selectedCategory.split(" ")[0];
        ScoreCategory category = ScoreCategory.valueOf(categoryName);
        return game.scoreCategoryForPlayer(playerName, category);
    }

    // Checks if a player has rolled during their turn
    public boolean hasPlayerRolled(String playerName) {
        return game.hasPlayerRolled(playerName);
    }

    // Calculates the potential scores for a player based on their current dice
    public Map<ScoreCategory, Integer> getPotentialScores(String playerName) {
        return game.calculatePotentialScoresForPlayer(playerName);
    }

    // Gets the index of the current player
    public int getCurrentPlayerIndex() {
        return game.getCurrentPlayerIndex();
    }

    // Advances to the next player's turn
    public void advanceToNextPlayer() {
        game.advanceTurn();
    }

    // Checks if the current player is the CPU
    public boolean isCurrentPlayerCPU() {
        return game.isCurrentPlayerCPU();
    }

    // Retrieves the current player's dice faces
    public List<DiceValue> getCurrentDiceFaces() {
        return game.getCurrentPlayerDiceFaces();
    }

    // Retrieves the current player's dice hold states
    public List<Boolean> getCurrentDiceHolds() {
        return game.getCurrentPlayerDiceHolds();
    }
}
 