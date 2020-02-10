package cellsociety.Model;

import java.util.List;
import java.util.Map;

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
