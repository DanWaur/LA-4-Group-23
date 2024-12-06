package controller;

import model.YahtzeeGame;
import model.Player;
import model.ScoreCategory;
import java.util.List;
import java.util.Map;

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
    

    public Object[][] getScorecardData() {
        return game.getScorecardData();
    }


    public List<String> getSelectableCategories(String playerName) {
        return game.getSelectableCategories(playerName);
    }
    
    public boolean scoreCategory(String playerName, ScoreCategory category) {
        return game.scoreCategoryForPlayer(playerName, category);
    }

    public String getCurrentPlayerName() {
        return game.getCurrentPlayerName();
    }
    
    
    
    


}
