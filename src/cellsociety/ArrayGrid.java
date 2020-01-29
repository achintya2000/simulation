package cellsociety;

public class ArrayGrid extends Grid {

    private int mySize;
    private int[][] myArray;

    public ArrayGrid(int size) { // assume its a square
        mySize = size;
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
        // logic for update based on neighbors goes here
        // below is dummy code
        if (getState(0,1) == 1) {
            for (int i = 0; i < mySize; i++) {
                for (int j = 0; j < mySize; j++) {
                    myArray[i][j] = j;
                }
            }
        } else {
            for (int i = 0; i < mySize; i++) {
                for (int j = 0; j < mySize; j++) {
                    myArray[i][j] = i;
                }
            }
        }
        // above is dummy code
    }

    @Override
    public int getState(int pos_x, int pos_y) {
        return myArray[pos_x][pos_y];
    }

}
