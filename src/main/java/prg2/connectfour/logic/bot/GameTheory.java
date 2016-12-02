package prg2.connectfour.logic.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class GameTheory extends Player{
	
	private Player thisPlayer;
	private Player enemyPlayer;
	private Grid board;
    private int maxColumn;
    private int maxRow;
    private int column;
    private int row;
    private ArrayList<Integer> possibleSolutions;
    private ArrayList<Integer> veryBadIdeas;

	public GameTheory (String name, Color color){
		//ToDo: Can't be the real solution
		super(name, color);
	}
	
	public int getNextMove(Grid newBoard){
		
		this.board = newBoard;
		
	    this.possibleSolutions = new ArrayList<Integer>();
	    this.veryBadIdeas = new ArrayList<Integer>();
	    this.maxColumn = board.getWidth();
	    this.maxRow = board.getHeight();
	    
	    if (board.isBoardEmpty())
	    {
	      return 4;  //Stein in mitte legen
	    }
	    
	    if(canIWin() >= 0){
	    	return canIWin();
	    }
	    
	    if(canEnemyWin() >= 0){
	    	return canEnemyWin();
	    }
	    
	    this.detectSmartMoves();
	    this.removeStupidMoves();
	    this.preferMiddle();
	    this.detectBadMoves();
	          
	    // Pick a random solution
	    // Some solutions are there more than ones.
	    // This are the better ones.
	    if (!possibleSolutions.isEmpty())
	    {
	      Collections.shuffle(possibleSolutions);
	      return (int)possibleSolutions.get(0);
	    }
	        
	    // If there are only bad ideas, be a man and choose a bad one!
	    if (!veryBadIdeas.isEmpty())
	    {
	      Collections.shuffle(veryBadIdeas);
	      return (int)veryBadIdeas.get(0);
	    }
	      
	    return column;
	}    
	    
	
	private int canIWin(){
	    for (column = 0; column < maxColumn; column++)
	    {
	      row = board.getRowValue(column);
	      // if column is full, row = -1:
	      if (row != -1 && board.checkXInARow(column, row, 4, thisPlayer, board)) // CheckXInARow checks if X values in a row exists (Vertically, horizontally, diagonally 2x)
	      {
	        return column; //I win!
	      }
	    }
	    return -1;
	}
	
	private int canEnemyWin(){
	    for (column = 0; column < maxColumn; column++)
	    {
	      row = board.getRowValue(column);
	      // if column is full, row = -1:
	      if (row != -1 && board.checkXInARow(column, row, 4, enemyPlayer, board)) // CheckXInARow checks if X values in a row exists (Vertically, horizontally, diagonally 2x)
	      {
	        return column; // Otherwise enemy win
	      }
	    }
	    return -1;
	}
	
	private void detectSmartMoves(){
	    for (column = 0; column < maxColumn; column++)
	    {
	      row = board.getRowValue(column);

	      if (row != -1 && board.checkXInARow(column, row, 3, enemyPlayer, board))
	      {
	    	// Try to destroy enemies chances to win. In all of this moves the enemy can have 3 in a row
	        possibleSolutions.add(column);
	      }
	      if(possibleSolutions.isEmpty()){
		      if (row != -1 && board.checkXInARow(column, row, 2, enemyPlayer, board))
		      {
		    	// Enemy player can't have 3 in a row, so check for 2 in a row...
		        possibleSolutions.add(column);
		      }
	      }
	    }
	}
	
	private void removeStupidMoves(){
		// does any solution enable my enemy to win next turn?
	      Iterator<Integer> solutionsItr = possibleSolutions.iterator();
	      int possibleColumn;
	      while (solutionsItr.hasNext())
	      {
	        possibleColumn = solutionsItr.next();
	        int nextRow = board.getRowValue(possibleColumn) + 1;
	        if (nextRow < maxRow && board.checkXInARow(possibleColumn, nextRow, 4, enemyPlayer, board))
	        {
	        	solutionsItr.remove();
	        }
	      }
	}
	
	private void preferMiddle(){
		// prefer solutions in the middle of field:
	      int nrOfSolutions = possibleSolutions.size();
	      for (int i = 0; i < nrOfSolutions; i++)
	      {
	        if (possibleSolutions.get(i) > 1 && possibleSolutions.get(i) < 5)
	          possibleSolutions.add(possibleSolutions.get(i));
	      }
	}
	
	private void detectBadMoves(){
	      for (int col = 0; col < maxColumn; col++)
	      {
	         // add moves that enable my enemy to win to veryBadIdeas
	          int nextRow = board.getRowValue(col) + 1;
	          if (nextRow < maxRow && board.checkXInARow(col, nextRow, 4, enemyPlayer, board))
	            veryBadIdeas.add(col);
	      }
	}
}
