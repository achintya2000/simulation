package cellsociety.Controller;

import cellsociety.Model.Grid;

public class GameOfLife extends Simulation {

    public GameOfLife(String filepath) {
        super(filepath);
    }

    @Override
    void updateGrid() {
        for(int r = 0; r < Grid.getSize(); r ++){
            for(int c = 0; c < Grid.getSize(); c ++){
                int aliveNeighbors = aliveNeighbors(r,c);
                if(Grid.getCurrentState(cellRow,cellCol) == 0 && aliveNeighbors == 3){           // a dead cell with exactly 3 neighbors comes back to life
                   Grid.updateCell(r,c,1);
                }
                else if(Grid.getCurrentState(cellRow,cellCol) == 1 && aliveNeighbors < 2){       // an alive cell dies if less than 2 alive neighbors
                   Grid.updateCell(r,c,0);
                }
                else if(Grid.getCurrentState(cellRow,cellCol) == 1 && aliveNeighbors > 3){       // an alive cell with more then 3 neighbors dies
                   Grid.updateCell(r,c,0);
                }
            }
        }
    }

    private int aliveNeighbors(int r, int c){
        int alive = 0;
        int[] statusOfNeighbors = Grid.checkNeighbors(r,c);
        int i = 0;
        while (statusOfNeighbors[i] != -1){
            if(statusOfNeighbors[i] == 1){
                alive++;
            }
            i++;
        }
        return alive;
    }
}
