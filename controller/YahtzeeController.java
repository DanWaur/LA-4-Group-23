package controller;

import java.util.List;
import java.util.Map;
import model.DiceValue;
import model.Player;
import model.ScoreCategory;
import model.YahtzeeGame;

public class YahtzeeController {
    private YahtzeeGame game;

    public YahtzeeController(int numPlayers, boolean cpuMode) {
        game = new YahtzeeGame(numPlayers, cpuMode);
    }

    public boolean rollDice() {
        return game.rollDice();
    }

    public boolean toggleDice(List<Integer> diceIndices) {
        return game.toggleDice(diceIndices);
    }

    public boolean chooseScore(ScoreCategory category) {
        return game.chooseScore(category);
    }
    
    public ScoreCategory getCpuAim() {
        return game.getCpuAim();
    }
    
    public ScoreCategory iterateCpuChoices(ScoreCategory aimFor) {
        return game.iterateCpuChoices(aimFor);
    }

    public Map<String, Integer> getPlayerScores() {
        return game.getPlayerScores();
    }

    public boolean isGameOver() {
        return game.isGameOver();
    }

    public int getCurrentRound() {
        return game.getCurrentRound();
    }

    public List<Player> getPlayers() {
        return game.getPlayers();
    }
    
    public int getPlayerScoreByIndex(int playerIndex) {
        return game.getPlayerScore(playerIndex);
    }
    

    public Integer getCategoryScoreForPlayer(String playerName, ScoreCategory category) {
        return game.getCategoryScoreForPlayer(playerName, category);
    }
    

//    public Object[][] getScorecardData() {
//        int numCategories = ScoreCategory.values().length;
//        int numPlayers = game.getNumberOfPlayers();
//        Object[][] data = new Object[numCategories + 1][numPlayers + 1]; // +1 for header row
//    
//        // Fill category rows
//        for (int row = 0; row < numCategories; row++) {
//            ScoreCategory category = ScoreCategory.values()[row];
//            data[row + 1][0] = category.name(); // Category name
//            for (int col = 0; col < numPlayers; col++) {
//                String playerName = game.getPlayerName(col);
//                Integer score = game.getCategoryScoreForPlayer(playerName, category);
//                data[row + 1][col + 1] = (score != null) ? score : "-"; // Placeholder if null
//            }
//        }
//    
//        return data;
//    }
    

    

    public List<String> getSelectableCategories(String playerName) {
        return game.getSelectableCategories(playerName);
    }
//    
//    public boolean scoreCategory(String playerName, ScoreCategory category) {
//        return game.scoreCategoryForPlayer(playerName, category);
//    }

    public String getCurrentPlayerName() {
        return game.getCurrentPlayerName();
    }
    
    
//    public int calculateTotalScore(String playerName) {
//        int total = 0;
//        for (ScoreCategory category : ScoreCategory.values()) {
//            Integer score = game.getCategoryScoreForPlayer(playerName, category);
//            if (score != null) {
//                total += score;
//            }
//        }
//        return total;
//    }


    public boolean scoreSelectedCategory(String playerName, String selectedCategory) {
        
        String categoryName = selectedCategory.split(" ")[0];
        ScoreCategory category = ScoreCategory.valueOf(categoryName);
    
        // Delegate scoring to the game
        return game.scoreCategoryForPlayer(playerName, category);
    }
    
    

    public boolean hasPlayerRolled(String playerName) {
        return game.hasPlayerRolled(playerName); // Delegate to game
    }

    public Map<ScoreCategory, Integer> getPotentialScores(String playerName) {
        return game.calculatePotentialScoresForPlayer(playerName); // Delegate to game
    }

    public int getCurrentPlayerIndex() {
        return game.getCurrentPlayerIndex(); // Delegate to game
    }


    public void advanceToNextPlayer() {
        game.advanceTurn(); // Delegate to the model
    }
    
    /**
     * checks if the current player is the CPU
     * @return true if the current player is the CPU, false otherwise
     */
    public boolean isCurrentPlayerCPU() {
        return game.isCurrentPlayerCPU();
    }
    

    public List<DiceValue> getCurrentDiceFaces() {
        return game.getCurrentPlayerDiceFaces();
    }

    public List<Boolean> getCurrentDiceHolds() {
        return game.getCurrentPlayerDiceHolds();
    }

}
