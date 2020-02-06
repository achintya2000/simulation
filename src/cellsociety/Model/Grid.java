package cellsociety.Model;

public abstract class Grid {
    public abstract int getSize();
    public abstract void initializeDefaultCell(int state);
    public abstract void updateCell(int row, int col, int newState);
    public abstract int[] checkNeighbors(int row, int col, boolean diagonals, boolean atomic_update);
    public abstract int getCurrentState(int row, int col);
    public abstract int[][] getGrid();
    public abstract int getReferenceState(int row, int col);
    public abstract boolean inBounds(int r, int c);
}
