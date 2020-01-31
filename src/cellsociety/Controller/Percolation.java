package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.Map;

// 0 - can flow no water
// 1 - can flow has water
// 2 - cannot flow

/*public class Percolation extends Simulation {

    @Override
    void loadSimulationContents(String filepath) {
        XMLParser gameoflife = new XMLParser("config"); //will have to change to percolation when file is made
        Map<String, String> configuration = gameoflife.getInfo(new File(filepath));
        SIMULATION_NAME = configuration.get("simulation");
        GRID_WIDTH = Integer.parseInt(configuration.get("width"));
        GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
        System.out.println(configuration);
        simulationGrid = new ArrayGrid(GRID_WIDTH);
    }

    @Override
    void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                if(canFlow(r,c) && closeToWater(r,c)){
                    simulationGrid.updateCell(r,c,1);
                }
            }
        }
    }

    @Override
    Grid getGrid() {
        return simulationGrid;
    }

    @Override
    int getSimulationCols() {
        return GRID_WIDTH;
    }

    public boolean closeToWater(int r, int c){
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c);
        int i = 0;
        while (statusOfNeighbors[i] != -1){
            if(statusOfNeighbors[i] == 1){// has water might need to change based on numbering
               return true;
            }
        }
        return false;
    }

    public boolean canFlow(int r, int c){
        return (simulationGrid.getReferenceState(r,c) != 2); //cell does not flow water
    }
}*/
