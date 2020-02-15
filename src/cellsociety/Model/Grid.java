package cellsociety.Model;

import java.util.List;
import java.util.Map;

/**
 * Purpose: The purpose of this interface is to define method signatures that any data structure that we decide to use for our simulation grid will need
 * to have functionality for.
 * Assumptions: We assume that a data structure we use will have all to have all these features implemented.
 * If we don't use a data structure that requires this then some of these methods will be redundant and that would be bad design.
 * Dependencies: This only depends on standard java utils like List and Map
 * How to use: Implement this in a data structure that represents a simulation grid and write the logic for the method for that specific data structure.
 * @author Sarah, Doherty, Achintya
 */
public interface Grid {
    int getSize();
    void initializeDefaultCell(int state);
    void updateCell(int row, int col, int newState);
    void setNeighbors(List<String> requestedNeighbors, int shape, int edge);
    Map<String, Integer> checkNeighbors(int row, int col, boolean atomicUpdate);
    Integer[] getOffset(String neighbor);
    int getCurrentState(int row, int col);
    int[][] getGrid();
    boolean isNeighborhoodSet();
    int getReferenceState(int row, int col);
    boolean inBounds(int r, int c);
}
