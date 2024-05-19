// Name: Joshua Fendi
// USC NetID: 5712969966
// CS 455 PA3
// Spring 2024


import java.sql.SQLOutput;

/**
 VisibleField class
 This is the data that's being displayed at any one point in the game (i.e., visible field, because
 it's what the user can see about the minefield). Client can call getStatus(row, col) for any
 square.  It actually has data about the whole current state of the game, including the underlying
 mine field (getMineField()).  Other accessors related to game status: numMinesLeft(),
 isGameOver().  It also has mutators related to actions the player could do (resetGameDisplay(),
 cycleGuess(), uncover()), and changes the game state accordingly.

 It, along with the MineField (accessible in mineField instance variable), forms the Model for the
 game application, whereas GameBoardPanel is the View and Controller in the MVC design pattern.  It
 contains the MineField that it's partially displaying.  That MineField can be accessed
 (or modified) from outside this class via the getMineField accessor.



   thisMineField- corresponds to the Visible Field for where the mines are
   thisVisibleField- corresponsds to its valid mine field, and provides user interface
   numGuesses- number of time user guesses is >= 0
   thisnumRows- number of rows in a field
   thisnumCols- number of columns in a field
   numUncovered- number of squares uncovered
   exploded- true if mine is explided, game is over
   actualNumMines- this is the number of mines that is not mutated
 */
public class VisibleField {
   // ----------------------------------------------------------
   // The following public constants (plus values [0,8] mentioned in comments below) are the
   // possible states of one location (a "square") in the visible field (all are values that can be
   // returned by public method getStatus(row, col)).

   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):

   // values in the range [0,8] corresponds to number of mines adjacent to this opened square

   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already
   // (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of
   // losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused
   // you to lose)
   // ----------------------------------------------------------

   // <put instance variables here>
   private MineField thisMineField;
   private int[][] thisVisibleField;
   private int numGuesses;
   private int thisnumRows;
   private int thisnumCols;
   private int numUncovered;
   private boolean exploded;
   private int actualNumMines;


   /**
    Create a visible field that has the given underlying mineField.
    The initial state will have all the locations covered, no mines guessed, and the game not
    over.
    @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      // MINE FIELD
      thisMineField = mineField;

      // numROws and numCols
      thisnumRows = thisMineField.numRows();
      thisnumCols = thisMineField.numCols();
      numUncovered = 0;
      exploded = false;
      actualNumMines = 0;

      // VISIBLE FIELD
      thisVisibleField = new int[thisnumRows][thisnumCols];

      // fill visible field with -1 to represent all covered squares
      for(int i=0; i<thisnumRows; ++i) {
         for(int j=0; j<thisnumCols; ++j) {
            // increase actual mines counts
            if(thisMineField.hasMine(i,j)) {
               ++actualNumMines;
            }
            thisVisibleField[i][j] = -1;
         }
      }
   }


   /**
    Reset the object to its initial state (see constructor comments), using the same underlying
    MineField.
    */
   public void resetGameDisplay() {
      // reset values
      exploded = false;
      numUncovered = 0;

      // fill visible field with -1 to represent all covered squares
      for(int i=0; i<thisnumRows; ++i) {
         for(int j=0; j<thisnumCols; ++j) {
            thisVisibleField[i][j] = -1;
         }
      }

      // number of Guesses
      numGuesses = 0;
   }


   /**
    Returns a reference to the mineField that this VisibleField "covers"
    @return the minefield
    */
   public MineField getMineField() {
      return thisMineField;
   }


   /**
    Returns the visible status of the square indicated.
    @param row  row of the square
    @param col  col of the square
    @return the status of the square at location (row, col).  See the public constants at the
    beginning of the class for the possible values that may be returned, and their meanings.
    PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return thisVisibleField[row][col];
   }


   /**
    Returns the the number of mines left to guess.  This has nothing to do with whether the mines
    guessed are correct or not.  Just gives the user an indication of how many more mines the user
    might want to guess.  This value will be negative if they have guessed more than the number of
    mines in the minefield.
    @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return thisMineField.numMines() - numGuesses;
   }


   /**
    Cycles through covered states for a square, updating number of guesses as necessary.  Call on
    a COVERED square changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to
    QUESTION;  call on a QUESTION square changes it to COVERED again; call on an uncovered square
    has no effect.
    @param row  row of the square
    @param col  col of the square
    PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      switch(thisVisibleField[row][col]) {
         case -1: // COVERED
            thisVisibleField[row][col] = -2;
            ++numGuesses; // increment number of guesses
            break;
         case -2: // MINE_GUESS
            thisVisibleField[row][col] = -3;
            --numGuesses; // undo the guess
            break;
         case -3: // QUESTION
            thisVisibleField[row][col] = -1;
            break;
      }
   }


   /**
    Uncovers this square and returns false iff you uncover a mine here.
    If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in the
    neighboring area that are also not next to any mines, possibly uncovering a large region.
    Any mine-adjacent squares you reach will also be uncovered, and form (possibly along with
    parts of the edge of the whole field) the boundary of this region.
    Does not uncover, or keep searching through, squares that have the status MINE_GUESS.
    Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
    or a loss (opened a mine).
    @param row  of the square
    @param col  of the square
    @return false   iff you uncover a mine at (row, col)
    PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      // if square is uncovered or is MINE_GUESS
      if(isUncovered(row,col) || thisVisibleField[row][col] == -2) {
         return true;
      }

      ++numUncovered; // increment numUncovered

      // if square has a mine
      if(thisMineField.hasMine(row,col)) {
         thisVisibleField[row][col] = 11;
         exploded = true;

         uncoverMines();
         return false;
      }


      // if square has adjacent mine, change visible field to display number
      if(thisMineField.numAdjacentMines(row,col) > 0) {
         thisVisibleField[row][col] = thisMineField.numAdjacentMines(row,col);
         return true;
      }


      // square doesnt have any adjacent mines
      thisVisibleField[row][col] = 0;

      // recurse and uncover all neighboring squares that are not mines
      for(int i=row-1; i<row+2; ++i) {
         for(int j=col-1; j<col+2; ++j) {
            // if in range
            if(thisMineField.inRange(i,j) && !(i == row && j == col)) {
               uncover(i,j);
            }
         }
      }
      return true;
   }




   /**
    Returns whether the game is over.
    (Note: This is not a mutator.)
    @return whether game has ended
    */
   public boolean isGameOver() {
      // if all possible squares are uncovered or mine exploded
      if(numUncovered == ((thisnumRows*thisnumCols) - thisMineField.numMines())) {
         uncoverMines();
         return true;
      }

      // if any value is 9, 10, or 11
      for(int i=0; i<thisnumRows; ++i) {
         for(int j=0; j<thisnumCols; ++j) {
            if(thisVisibleField[i][j] == 9 || thisVisibleField[i][j] == 10 || thisVisibleField[i][j] == 11) {
               return true;
            }
         }
      }
      return false;
   }


   /**
    Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states,
    vs. any one of the covered states).
    @param row of the square
    @param col of the square
    @return whether the square is uncovered
    PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      return (thisVisibleField[row][col] >= 0);
   }


   // <put private methods here>
   /**
    Uncovers all mines that are covered
    */
   private void uncoverMines() {
      for(int i=0; i<thisnumRows; ++i) {
         for(int j=0; j<thisnumCols; ++j) {
            // incorrect guess
            if(thisVisibleField[i][j] == -2 && !thisMineField.hasMine(i,j)) {
               thisVisibleField[i][j] = 10;
            }
            // mine uncovered
            else if(thisVisibleField[i][j] == -1 && thisMineField.hasMine(i,j)) {
               thisVisibleField[i][j] = 9;
            }
         }
      }
   }
}
