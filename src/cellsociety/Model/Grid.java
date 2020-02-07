package cellsociety.Model;

import java.util.List;
import java.util.Map;

public abstract class Grid {
    public abstract int getSize();
    public abstract void initializeDefaultCell(int state);
    public abstract void updateCell(int row, int col, int newState);
    public abstract void setNeighbors(List<String> requestedNeighbors);
    public abstract Map<String, Integer> checkNeighbors(int row, int col, boolean atomicUpdate);
    public abstract Integer[] getOffset(String neighbor);
    public abstract int getCurrentState(int row, int col);
    public abstract int[][] getGrid();
    public abstract int getReferenceState(int row, int col);
    public abstract boolean inBounds(int r, int c);
}
