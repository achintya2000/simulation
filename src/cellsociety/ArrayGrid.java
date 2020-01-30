package cellsociety;

public class ArrayGrid extends Grid {

    private static int mySize; //for all length calculations I used myArray.length and myArray[0].length just in case myArray is not a square
    public static int[][] myArray;
    private static int[][] myReferenceArray;

    public ArrayGrid(int size) { // assume its a square
        mySize = size;
        initialize();
    }

    @Override
    public void initialize() {
        myArray = new int[mySize][mySize];
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                myArray[i][j] = i;
            }
        }
    }

    @Override
    public void update() {
        //updates myArray by make a copy of the array as a reference
        // logic for update based on neighbors goes here
        // updates for game of life only with refactor to make more general
        myReferenceArray = new int[myArray.length][myArray[0].length];
        System.arraycopy(myArray, 0, myReferenceArray, 0, myArray.length);//makes new copy of myArray so the method has reference

        for (int cellRow = 0; cellRow < myArray.length; cellRow++) {                        // loops through each row of myArray
            for (int cellCol = 0; cellCol < myArray[0].length; cellCol++) {                 // loops through each col of myArray
                int aliveNeighbors = checkNeighbors(cellRow, cellCol, myReferenceArray);
                //insert method that performs certain logic based on state of neighbors and state of current cell
                // lines 35 - 44 are just for game of life
                if(getState(cellRow,cellCol) == 0 && aliveNeighbors == 3){           // a dead cell with exactly 3 neighbors comes back to life
                    myArray[cellRow][cellCol] = 1;
                }
                else if(getState(cellRow,cellCol) == 1 && aliveNeighbors < 2){       // an alive cell dies if less than 2 alive neighbors
                    myArray[cellRow][cellCol] = 0;
                }
                else if(getState(cellRow,cellCol) == 1 && aliveNeighbors > 3){       // an alive cell with more then 3 neighbors dies
                    myArray[cellRow][cellCol] = 0;
                }
            }
        }
    }

   private int checkNeighbors(int cellRow, int cellCol, int[][] referenceArray){
        int[] rDelta = {0,0,1,-1,1,1,-1,-1};                    //parallel arrays rDelta and cDelta check each possible neighbor of a cell
        int[] cDelta = {1,-1,0,0,1,-1,1,-1};
                int aliveNeighbors = 0;                         // this is for just game of life will refactor to make more general
                for(int i = 0; i < rDelta.length; i ++){        // loops through rDelta and cDelta - checking each neighbor
                    int neighborRow = cellRow + rDelta[i];
                    int neighborCol = cellCol + cDelta[i];
                    if(inBounds(neighborRow, neighborCol)){
                        // insert method to perform some logic to check state of neighbors simulation specific
                        if(getState(neighborRow,neighborCol) == 1){ //game of life logic
                            aliveNeighbors++;
                        }
                    }
                }
        return aliveNeighbors;
    }


    @Override
    public int getState(int pos_x, int pos_y) {
        return myReferenceArray[pos_x][pos_y];
    }

    private boolean inBounds(int r, int c){ //
        return (r < myArray.length && r >= 0 && c < myArray[0].length && c >= 0);
    }

}
