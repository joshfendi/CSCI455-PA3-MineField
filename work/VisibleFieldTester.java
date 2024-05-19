public class VisibleFieldTester {

    public static void main(String[] args) {
       
        System.out.println("Hello, VisibleFieldTester!");
     
       boolean[][] array = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, true, true, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
        };
       
       MineField testObj = new MineField(array);
       
       
       VisibleField visObj = new VisibleField(testObj);
   
    }
}