package prg2.connectfour.logic.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.rule.IGridIterator;
import prg2.connectfour.logic.rule.IteratorReduction;
import prg2.connectfour.utils.Pair;

public class GameTheory extends Player {
    private Player enemyPlayer;
    private Grid board;
    private int maxColumn;
    private int maxRow;
    private int column;
    private int row;
    private ArrayList<Integer> possibleSolutions;
    private ArrayList<Integer> veryBadIdeas;

    public GameTheory(String name, Color color) {
        super(name, color);
        this.enemyPlayer = new Player("Awesome dude", Color.Red);
    }

    public int getNextMove(Grid newBoard) {

        this.board = newBoard;
        //this.enemyPlayer = new Player("Awesome dude", Color.Red);
        this.possibleSolutions = new ArrayList<Integer>();
        this.veryBadIdeas = new ArrayList<Integer>();
        this.maxColumn = board.width;
        this.maxRow = board.height;

        if (board.isEmpty()) {
        	return this.maxColumn/2; // Stein in mitte legen
        }

        if (canIWin() >= 0) {
            return canIWin();
        }

        if (canEnemyWin() >= 0) {
            return canEnemyWin();
        }

        this.detectSmartMoves();
        this.removeStupidMoves();
        this.preferMiddle();
        this.detectBadMoves();

        // Pick a random solution
        // Some solutions are there more than ones.
        // This are the better ones.
        if (!possibleSolutions.isEmpty()) {
            Collections.shuffle(possibleSolutions);
            return (int) possibleSolutions.get(0);
        }

        // If there are only bad ideas, be a man and choose a bad one!
        if (!veryBadIdeas.isEmpty()) {
            Collections.shuffle(veryBadIdeas);
            return (int) veryBadIdeas.get(0);
        }
        
        for(column=0; column < maxColumn; column ++){
        	if(this.countFilledCells(column) < maxRow)
        		return column;
        }
        return 0; // Should never happen!
    }

    private int canIWin() {
        for (column = 0; column < maxColumn; column++) {
            row = this.countFilledCells(column);
            if (row != maxRow && this.checkCount(column, row, 3, this)) // CheckXInARow
                                                                                    // checks
                                                                                    // if
                                                                                    // X
                                                                                    // values
                                                                                    // in
                                                                                    // a
                                                                                    // row
                                                                                    // exists
                                                                                    // (Vertically,
                                                                                    // horizontally,
                                                                                    // diagonally
                                                                                    // 2x)
            {
                return column; // I win!
            }
        }
        return -1;
    }

    private int canEnemyWin() {
        for (column = 0; column < maxColumn; column++) {
            row = this.countFilledCells(column);
            if (row != maxRow && this.checkCount(column, row, 3, enemyPlayer)) // CheckXInARow
                                                                                     // checks
                                                                                     // if
                                                                                     // X
                                                                                     // values
                                                                                     // in
                                                                                     // a
                                                                                     // row
                                                                                     // exists
                                                                                     // (Vertically,
                                                                                     // horizontally,
                                                                                     // diagonally
                                                                                     // 2x)
            {
                return column; // Otherwise enemy win
            }
        }
        return -1;
    }

    private void detectSmartMoves() {
        for (column = 0; column < maxColumn; column++) {
            row = this.countFilledCells(column);

            if (row != maxRow && this.checkCount(column, row, 2, enemyPlayer)) {
                // Try to destroy enemies chances to win. In all of this moves
                // the enemy can have 3 in a row
                possibleSolutions.add(column);
            }
            if (possibleSolutions.isEmpty()) {
                if (row != maxRow && this.checkCount(column, row, 1, enemyPlayer)) {
                    // Enemy player can't have 3 in a row, so check for 2 in a
                    // row...
                    possibleSolutions.add(column);
                }
            }
        }
    }

    private void removeStupidMoves() {
        // does any solution enable my enemy to win next turn?
        Iterator<Integer> solutionsItr = possibleSolutions.iterator();
        int possibleColumn;
        while (solutionsItr.hasNext()) {
            possibleColumn = solutionsItr.next();
            int nextRow = this.countFilledCells(possibleColumn) + 1;
            if (nextRow < maxRow && this.checkCount(possibleColumn, nextRow, 3, enemyPlayer)) {
                solutionsItr.remove();
            }
        }
    }

    private void preferMiddle() {
        // prefer solutions in the middle of field:
        int nrOfSolutions = possibleSolutions.size();
        for (int i = 0; i < nrOfSolutions; i++) {
            if (possibleSolutions.get(i) > 1 && possibleSolutions.get(i) < (maxColumn -2))
                possibleSolutions.add(possibleSolutions.get(i));
        }
    }

    private void detectBadMoves() {
        for (int col = 0; col < maxColumn; col++) {
            // add moves that enable my enemy to win to veryBadIdeas
            int nextRow = this.countFilledCells(col) + 1;
            if (nextRow < maxRow && this.checkCount(col, nextRow, 3, enemyPlayer))
                veryBadIdeas.add(col);
        }
    }
    
    private int countFilledCells(int column) {
        Stack<Pair<Player, Integer>> stack = IteratorReduction.reduceWithIterator(board, IGridIterator.Vertical, column, 0);
        int count = 0;
        while (!stack.empty()) {
            Pair<Player, Integer> pair = stack.pop();
            if (pair.left != null)
                count += pair.right;
        }

        return count;
    }

    private boolean checkCount(int column, int row, int count, Player player) {
        for (IGridIterator iterator : IGridIterator.All) {
            Stack<Pair<Player, Integer>> stack = IteratorReduction.reduceWithIterator(board, iterator, column, row);
            while (!stack.empty()) {
                Pair<Player, Integer> pair = stack.pop();
                if(pair.left == null){
                	continue;
                }else if (!pair.left.getClass().equals(player.getClass())) {
					continue;
				}
                if (pair.right == count)
                    return true;
            }
        }

        return false;
    }
}
