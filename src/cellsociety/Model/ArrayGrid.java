package cellsociety.Model;

import cellsociety.Model.Grid;

import java.util.*;

public class ArrayGrid extends Grid {

    private static int mySize; //for all length calculations I used myArray.length and myArray[0].length just in case myArray is not a square
    private static int[][] myArray;
    private static int[][] myReferenceArray;
    private Map<String,Integer[]> allNeighbors = Map.of("NW",new Integer[] {1,-1},"N",new Integer[] {1,0},"NE",new Integer[] {1,1},"W",new Integer[] {0,-1},"E",new Integer[] {0,1},"SW",new Integer[] {-1,-1},"S",new Integer[] {-1,0},"SE",new Integer[] {-1,1});
    private Map<String,Integer[]> currentNeighbors = new HashMap<String,Integer[]>();

    public ArrayGrid(int size) { // assume its a square
        mySize = size;
        myArray = new int[mySize][mySize];
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                myArray[i][j] = -1;
            }
        }
    }

    @Override
    public int getSize() {
        return mySize;
    }

    @Override
    public void initializeDefaultCell(int state) {
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                if (myArray[i][j] == -1) {
                    myArray[i][j] = state;
                }
            }
        }
    }

    @Override
    public void updateCell(int row, int col, int newState) {
        myArray[row][col] = newState;
    }

    @Override
    public void setNeighbors(List<String> requestedNeighbors) {
        for (String neighbor: requestedNeighbors) {
            currentNeighbors.put(neighbor, allNeighbors.get(neighbor));
        }
    }

    @Override
    public Integer[] getOffset(String neighbor) {
        return currentNeighbors.get(neighbor);
    }

    @Override
    public  Map<String, Integer> checkNeighbors(int row, int col, boolean atomicUpdate){
        if (row==0 && col==0) {
            myReferenceArray = new int[mySize][mySize];
            for(int r = 0; r < mySize; r ++){
                for(int c = 0; c < mySize; c++){
                    myReferenceArray[r][c] = myArray[r][c];
                }
            }
        }
        Map<String, Integer> statusOfNeighbors = new HashMap<String, Integer>();
        for(String neighbor : currentNeighbors.keySet()) {
           int neighborRow = row + currentNeighbors.get(neighbor)[0];
           int neighborCol = col + currentNeighbors.get(neighbor)[1];
           if (inBounds(neighborRow, neighborCol)) {
               if (atomicUpdate) { // Use to determine whether reference or current state needed
                   statusOfNeighbors.put(neighbor,getReferenceState(neighborRow,neighborCol));
               } else {
                   statusOfNeighbors.put(neighbor,getCurrentState(neighborRow,neighborCol));
               }
           }
        }
        return statusOfNeighbors;
    }

    @Override
    public int getCurrentState(int row, int col) {
        return myArray[row][col];
    }

    @Override
    public int[][] getGrid() {
        return myArray;
    }

    @Override
    public int getReferenceState(int row, int col) {
        return myReferenceArray[row][col];
    }

    @Override
    public boolean inBounds(int r, int c){
        return (r < myArray.length && r >= 0 && c < myArray[0].length && c >= 0);
    }
}
