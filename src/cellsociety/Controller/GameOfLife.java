package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.Map;

public class GameOfLife extends Simulation {

    public void loadSimulationContents(String filepath) {
        XMLParser gameoflife = new XMLParser("config");
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
                int aliveNeighbors = aliveNeighbors(r,c);
                if(simulationGrid.getCurrentState(r,c) == 0 && aliveNeighbors == 3){           // a dead cell with exactly 3 neighbors comes back to life
                    simulationGrid.updateCell(r,c,1);
                }
                else if(simulationGrid.getCurrentState(r,c) == 1 && aliveNeighbors < 2){       // an alive cell dies if less than 2 alive neighbors
                    simulationGrid.updateCell(r,c,0);
                }
                else if(simulationGrid.getCurrentState(r,c) == 1 && aliveNeighbors > 3){       // an alive cell with more then 3 neighbors dies
                    simulationGrid.updateCell(r,c,0);
                }
            }
        }
    }

    private int aliveNeighbors(int r, int c){
        int alive = 0;
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c);
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
