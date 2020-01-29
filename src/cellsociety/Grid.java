package cellsociety;

public abstract class Grid {

    public abstract void initialize();
    public abstract void update();
    public abstract int getState(int pos_x, int pos_y);

}
