package model;

import java.util.List;

public interface Observer {
    void userRollDice();
    void userToggleDice(List<Integer> diceIndices);
    void gameChooseScore(ScoreCategory scoreChoice);
}
