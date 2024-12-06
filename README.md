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

- Upon scoring, it will be the next player's turn, repeat the previous steps. If it is a Cpu, the decisions will be automated.

- The game follows the standard Yahtzee rules, found here: https://www.officialgamerules.org/board-games/yahtzee

## Score Categories



****Update as needed according to the actual implementation, additions and modifications.
