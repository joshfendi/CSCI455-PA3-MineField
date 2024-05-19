// Name: Joshua Fendi
// USC NetID: 5712969966
// CS 455 PA3
// Spring 2024

import java.util.Random;

/** 
   MineField
      Class with locations of mines for a minesweeper game.
      This class is mutable, because we sometimes need to change it once it's created.
      Mutators: populateMineField, resetEmpty
      Includes convenience method to tell the number of mines adjacent to a location.


    thisMineData- 2d array, row col pair is true if there is a mine
    thisRows- number of rows in mine field
    thisCols- number of columns in mine field
    originalNumMines- the original number of mines
    currNumMines- the current number of mines
 */
public class MineField {
   
   // <put instance variables here>
   private boolean[][] thisMineData;
   private int thisRows;
   private int thisCols;
   private int currNumMines;
   private boolean alreadyHasMines;
   
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in
      the array such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice
      versa.  numMines() for this minefield will corresponds to the number of 'true' values in 
      mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      // get rows and cols
      int thisRows = mineData.length;
      int thisCols = mineData[0].length;
      thisMineData = new boolean[thisRows][thisCols];
      alreadyHasMines = true;

      // count number of mines
      currNumMines = 0;

      // copy all data from mineData to thisMineData
      for(int i=0; i<thisRows; ++i) {
         for(int j=0; j<thisCols; ++j) {
            thisMineData[i][j] = mineData[i][j];
            if(mineData[i][j]){
               ++currNumMines;
            }
         }
      }
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a 
      MineField, numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      thisRows = numRows;
      thisCols = numCols;
      
      thisMineData = new boolean[thisRows][thisCols];
      currNumMines = numMines;
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on
      the minefield, ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      if(!alreadyHasMines) {
         // creates new mineField, effectively removing all old mines
         thisMineData = new boolean[thisRows][thisCols];

         int i=0;

         // create instance of Random class
         Random rand = new Random();

         while(i<numMines()) {

            int randRow = rand.nextInt(thisRows);
            int randCol = rand.nextInt(thisCols);

            // if the randomly generated placement is the square of
            // the original row,col pair or already has a mine in that square
            if(!((randRow == row && randCol == col) || hasMine(randRow,randCol))) {
               thisMineData[randRow][randCol] = true; // place a mine
               ++i; // increment since one mine was placed
            }
         }
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or
      numCols().  Thus, after this call, the actual number of mines in the minefield does not match
      numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in at the 
      beginning of a game.
    */
   public void resetEmpty() {
      thisMineData = new boolean[numRows()][numCols()];
      // reset all to empty squares
      for(int i=0; i<thisRows; ++i) {
         for(int j=0; j<thisCols; ++j) {
            thisMineData[i][j] = false;
         }
      }
   }

   
  /**
     Returns the number of mines adjacent to the specified location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int countMines = 0;
      
      // start bottom left corner of checking square
      // check all squares adjacent to specified location
      for(int currRow = row-1; currRow < row+2; ++currRow) {
         for(int currCol = col-1; currCol < col+2; ++currCol) {
            // if in range and not the specified location
            if(inRange(currRow, currCol) && !((currRow == row) && (currCol == col))) {               
               if(hasMine(currRow, currCol)) { ++countMines; } // increment count
            }
         }
      }
     
      return countMines;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      if(row >= numRows() || row < 0 || col >= numCols() || col < 0) {
         return false;
      }
      else {
         return true;
      }
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return thisMineData.length;       // length of 1st dimension (rows)
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return thisMineData[0].length;       // length of 2nd dimension (cols)
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      return thisMineData[row][col];     
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg
      constructor, some of the time this value does not match the actual number of mines currently
      on the field.  See doc for that constructor, resetEmpty, and populateMineField for more
      details.
      @return number of mines
    */
   public int numMines() {
      return currNumMines;
   }
   
   /**
      Converts thisMineData to String type
    */
   public String toString() {
      String tempStr = "";
      
      for(int i=0; i<numRows(); ++i) {
         tempStr += "\n";
         for(int j=0; j<numCols(); ++j) {
            tempStr += thisMineData[i][j] + " ";;
         }
      }
      
      return tempStr;
   }
   
   // <put private methods here>
   
         
}

