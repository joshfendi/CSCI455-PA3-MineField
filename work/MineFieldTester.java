public class MineFieldTester {

    public static void main(String[] args) {
       
        System.out.println("Hello, MineFieldTester!");
       boolean[][] array = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, true, true, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
        };
       
       MineField testObj = new MineField(array);
//
//       System.out.println("numAdjacent Mines, 2: " + testObj.numAdjacentMines(1,1));
//       System.out.println("inRange, false: " + testObj.inRange(1,5));
//       System.out.println("numRows, 5: " + testObj.numRows());
//       System.out.println("numCols, 5: " + testObj.numCols());
//       System.out.println("has mine, true: " + testObj.hasMine(1,2));
//       System.out.println("num mines possible, should be 8: " + testObj.numMines());
//
//       System.out.print(testObj.toString());

//       testObj.populateMineField();
       
   
    }
}
