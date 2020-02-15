package cellsociety.Model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Purpose: The purpose of this class is to implement the logic for the 2-D array version of the simulation grid.
 * Assumptions: This class assumed the method signatures and parameters are defined properly in the interface and have all the required information
 * needed to properly write the logic.
 * Dependencies: This depends on standard java utils like HashMap, Map, and List.
 * How to use: Use this in the Controller package in the Simulation abstract class after you load in the configuration variables from the XML file. Then
 * you can make an instance of this class and use the methods defined to populate the simulation grid.
 */
public class ArrayGrid implements Grid {

    private static int mySize; //for all length calculations I used myArray.length and myArray[0].length just in case myArray is not a square
    private int[][] myArray;
    private int[][] myReferenceArray;
    private Map<String,Integer[]> allNeighbors = Map.ofEntries(Map.entry("NW",new Integer[] {1,-1}),Map.entry("N",new Integer[] {1,0}),Map.entry("NE",new Integer[] {1,1}),Map.entry("W",new Integer[] {0,-1}),Map.entry("E",new Integer[] {0,1}),Map.entry("SW",new Integer[] {-1,-1}),Map.entry("S",new Integer[] {-1,0}),Map.entry("SE",new Integer[] {-1,1}), Map.entry("NWW", new Integer[] {-2,1}),Map.entry("NEE",new Integer[] {2,1}),Map.entry("WW",new Integer[] {-2,0}) ,Map.entry("EE",new Integer[] {2,0}));
    private Map<String,Integer[]> currentNeighbors = new HashMap<>();
    private int myShape = 0;
    private static final int shapeTriangle = 3;
    private int myEdge = 0;
    private static final int edgeToroidal = 2;
    private boolean isNeighborhoodSet = false;
    private static final int INVALID_INDEX = -5;

    /**
     * Constructor to create an ArrayGrid. This is the 2-D array that handles that holds the simulation variables
     * and runs logic to find neighbors.
     * @param size The size of the simulation grid.
     */
    public ArrayGrid(int size) {
        mySize = size;
        myArray = new int[mySize][mySize];
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                myArray[i][j] = -1;
            }
        }
    }

    /**
     * Getter method to return size used to externally to create the rectangular grid with colors.
     * @return Size of grid.
     */
    @Override
    public int getSize() {
        return mySize;
    }

    /**
     * Method used to define default state in the 2-D array.
     * @param state Int value that represents the default state.
     */
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

    /**
     * Method used to change state in a specified cell.
     * @param row Row value of the cell.
     * @param col Column value of the cell.
     * @param newState New state to set that cell to.
     */
    @Override
    public void updateCell(int row, int col, int newState) {
        myArray[row][col] = newState;
    }

    /**
     * Method to set which neighbors you need to check. Used for setting custom parameters.
     * @param requestedNeighbors The neighbors you use to
     * @param shape The shape of the grid's cells.
     * @param edge The type of edge (toroidal or not).
     */
    @Override
    public void setNeighbors(List<String> requestedNeighbors, int shape, int edge) {
        isNeighborhoodSet = true;
        myEdge = edge;
        myShape = shape;
        for (String neighbor: requestedNeighbors) {
            currentNeighbors.put(neighbor, allNeighbors.get(neighbor));
        }
    }

    /**
     * Getter method to check if custom neighbors are set or not.
     * @return Whether or not custom neighbors are set.
     */
    @Override
    public boolean isNeighborhoodSet() {
        return isNeighborhoodSet;
    }

    /**
     * Tells you in the grid how far away something is. For example south-west is mapped to -1,-1
     * @param neighbor The direction neighbor should be.
     * @return Array of how far away it is.
     */
    @Override
    public Integer[] getOffset(String neighbor) {
        return currentNeighbors.get(neighbor);
    }

    /**
     * Checks the value of the neighbors of a cell
     * @param row Cell's row value to check neighbors of
     * @param col Cell's column value to check neighbors of
     * @param atomicUpdate Tells you whether or not to use current or reference state
     * @return Map of statuses of the neighbors
     */
    @Override
    public  Map<String, Integer> checkNeighbors(int row, int col, boolean atomicUpdate){
        if (row==0 && col==0) {
            copyArray();
        }
        Map<String, Integer> statusOfNeighbors = new HashMap<>();
        for(String neighbor : currentNeighbors.keySet()) {
            if (col % 2 != 0 && myShape == shapeTriangle) { // if odd col and triangle, then orientation is flipped
                neighbor = neighbor.replace("N","S");
            }
            int[] validNeighbors = getValidNeighbors(row,col,neighbor);
            if (validNeighbors[0] != INVALID_INDEX && validNeighbors[1] != INVALID_INDEX) {
                addNeighbor(atomicUpdate, statusOfNeighbors, neighbor, validNeighbors);
            }

        }
        return statusOfNeighbors;
    }

    /**
     * Get's the state of the current grid - one that is displayed to users.
     * @param row Cell that we want to check's row
     * @param col Cell that we want to check's column
     * @return State of that cell
     */
    @Override
    public int getCurrentState(int row, int col) {
        return myArray[row][col];
    }

    /**
     * Getter method for the grid.
     * @return The 2-D grid that represents the simulation.
     */
    @Override
    public int[][] getGrid() {
        return myArray;
    }

    /**
     * Return state of 2-D array's copy for logic.
     * @param row Cell that we want to check's row
     * @param col Cell that we want to check's column
     * @return State of that cell
     */
    @Override
    public int getReferenceState(int row, int col) {
        return myReferenceArray[row][col];
    }

    /**
     * Check if the cell passed in is in bounds of 2-D array.
     * @param r Row value of the cell.
     * @param c Column value of the cell.
     * @return Returns true or false based on if value is in bounds of the grid.
     */
    @Override
    public boolean inBounds(int r, int c){
        return (r < myArray.length && r >= 0 && c < myArray[0].length && c >= 0);
    }

    private void addNeighbor(boolean atomicUpdate, Map<String, Integer> statusOfNeighbors, String neighbor, int[] validNeighbors) {
        int neighborRow = validNeighbors[0];
        int neighborCol = validNeighbors[1];
        if (inBounds(neighborRow,neighborCol)) {
            if (atomicUpdate) { // Use to determine whether reference or current state needed
                statusOfNeighbors.put(neighbor, getReferenceState(neighborRow, neighborCol));
            } else {
                statusOfNeighbors.put(neighbor, getCurrentState(neighborRow, neighborCol));
            }
        }
    }

    private void copyArray() {
        myReferenceArray = new int[mySize][mySize];
        for(int r = 0; r < mySize; r ++){
            for(int c = 0; c < mySize; c++){
                myReferenceArray[r][c] = myArray[r][c];
            }
        }
    }

    private int[] getValidNeighbors(int row, int col, String neighbor) {
        int directRow = row + currentNeighbors.get(neighbor)[0];
        int directCol = col + currentNeighbors.get(neighbor)[1];
        if (inBounds(directRow,directCol)) {
            return new int[] {directRow,directCol};
        }
        if (!inBounds(directRow,directCol) && myEdge==edgeToroidal) {
            return getToroidalNeighbors(directRow, directCol);
        }
        return new int[] {INVALID_INDEX,INVALID_INDEX};
    }

    private int[] getToroidalNeighbors(int directRow, int directCol) {
        int neighborRow = directRow;
        int neighborCol = directCol;
        if (directRow < 0) {
            neighborRow = myArray.length-1;
        } else if (directRow >= myArray.length) {
            neighborRow = 0;
        }
        if (directCol < 0) {
            neighborCol = myArray[0].length-1;
        } else if (directCol >= myArray[0].length) {
            neighborCol = 0;
        }
        return new int[] {neighborRow, neighborCol};
    }
}
