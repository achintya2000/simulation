package cellsociety.Controller;

import cellsociety.Model.Grid;

/*public class Percolation extends Simulation {
    public Percolation(String filepath) {
        super(filepath);
    }

    @Override
    void updateGrid() {
        for(int r = 0; r < Grid.getSize(); r ++){
            for(int c = 0; c < Grid.getSize(); c ++){
                if(canFlow(r,c) && closeToWater(r,c)){
                    Grid.updateCell(r,c,1);
                }
            }
        }
    }

public boolean closeToWater(int r, int c){
        int[] statusOfNeighbors = Grid.checkNeighbors(r,c);
        int i = 0;
        while (statusOfNeighbors[i] != -1){
            if(statusOfNeighbors[i] == 1){// has water might need to change based on numbering
               return true;
            }
        }
        return false;
    }

    public boolean canFlow(int r, int c){
        return (Grid.getCurrentState() != 2); //cell does not flow water
    }
} */
