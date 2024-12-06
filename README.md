# Yahtzee

## Overview

This is an implementation of the classic dice game Yahtzee in Java, featuring an interactive Swing GUI. The game allows playing against a single CPU, or in games of 2-4 players. The game cycles between each player, allowing them to roll and score their dice hands until all scorecard slots are filled.

## How to Run

1. Clone this repository

2. Compile the Java files

3. Run [GUI_FILENAME], which will open the main GUI for you to start playing the game.

## Game Instructions

- Upon running the game, a prompt will appear. Enter how many players will play (1-4) or if you want to play against a CPU.
  
- It will now be Player 1's turn. Click the "Roll Dice" button at the bottom of the window to roll the dice.

- 5 Randomized dice should appear on the right side of the window.

- The potential scores will be shown under the Player's column with the given dice hand.

- You can choose to hold certain dice, and reroll the rest up to 2 more times. To do this, click on the dice you would like to hold. They will be outlined in red.

- At any point in your 3 rolls, you can score with your given hand. Click the "Choose Score" button at the bottom of the window and select a slot to place your score. The amount
  of points you will earn in that category is displayed next to the Category you choose.

- Note that you must place a score in a category per turn, meaning that if you aren't able to score any points in any category, a score of 0 must be entered.

- Upon scoring, it will be the next player's turn, repeat the previous steps. If it is a Cpu, the decisions will be automated for its turn.

- The game follows the standard Yahtzee rules, found here: https://www.officialgamerules.org/board-games/yahtzee

## Score Categories and Points

These scorings are all done automatically based on your hand and the selected category.
Listed below are the different categories you may select and the points you may get from each.

- ONES-SIXES: Counts and adds only the dice of the given type.

- THREE OF A KIND: Adds and scores total of all dice in hand, if there is a pair of 3.

- FOUR OF A KIND: Adds and scores total of all dice in hand, if there is a pair of 4.

- FULL HOUSE: Scores 25 points, if there is a pair of 3 and a pair of 2.

- SMALL STRAIGHT: Scores 30 points, if there are 4 consecutive dice in hand.

- LARGE STRAIGHT: Scores 40 points, if there are 5 consecutive dice in hand.

- YAHTZEE: Scores 50 points, if all dice are of the same value.

- CHANCE: Adds total of all dice in hand.

## Structure

- This program is implemented using a Model-view-controller pattern. This means that the view (Swing GUI) calls and retrieves information from our controller, and the controller
  gathers information from the model, which does the actual calculation and algorithm running.

- The 'resources' folder contains the images for the dice. This is displayed in the GUI upon rolling.

- The 'model' folder represents the backend code with all the classes that are needed to calculate the functionality of the game. 'YahtzeeGame.java' is the central model class
  which controls the player amounts, the score cards, and values of all player's scores.

- The 'controller' contains the 'YahtzeeController.java'. This retrieves information from the model and determines what data to send back to the user upon a request.

- The 'view' contains the 'YahtzeeGUI.java', which displays the game for the user, and communicates with the controller to update the display.

- The 'tests' folder contains all of the testcases for the Yahtzee game. Running the tests will get > 90% code coverage for every java file, except for 'YahtzeeGUI.java' as
  this was tested through playing the game first hand.


## Authors: Juan Alvaro Rogel Acedo, Devin Dinh, Marco Pena, Daniel Reynaldo
