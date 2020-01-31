package cellsociety.Model;

public abstract class Grid {

    public abstract int getSize();
    public abstract void initializeCell(int row, int col, int state);
    public abstract void initializeDefaultCell(int state);
    public abstract void updateCell(int row, int col, int newState);
    public abstract int[] checkNeighbors(int row, int col);
    public abstract int getCurrentState(int row, int col);

}
