package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.List;
import java.util.Map;

public class GameOfLife extends Simulation {

    public void loadSimulationContents(String filepath) {

        XMLParser gameoflife = new XMLParser("config");
        List<String> xmlvals = List.of("title", "author", "simulation", "width", "height","numlive","live","dead");
        Map<String, String> configuration = gameoflife.getInfo(new File("./Resources/gameoflife.xml"), xmlvals);
        System.out.println(configuration);

        SIMULATION_NAME = configuration.get("simulation");
        GRID_WIDTH = Integer.parseInt(configuration.get("width"));
        GRID_HEIGHT = Integer.parseInt(configuration.get("height"));

        String liveCells = configuration.get("live");
        String[] point = new String[2];
        int k = 0;
        int[] rows = new int[Integer.parseInt(configuration.get("numlive"))];
        int[] cols = new int[Integer.parseInt(configuration.get("numlive"))];
        while(liveCells.lastIndexOf("]") != liveCells.indexOf("]")) {
            point = (liveCells.substring(liveCells.indexOf("[")+1, liveCells.indexOf("]"))).split(",");
            rows[k] = Integer.parseInt(point[0]);
            cols[k] = Integer.parseInt(point[1]);
            liveCells = liveCells.substring(liveCells.indexOf("]")+1, liveCells.lastIndexOf("]")+1);
            k = k + 1;
        }
        simulationGrid = new ArrayGrid(GRID_WIDTH);
        initializeGrid(rows,cols,1);
    }

    private void initializeGrid(int[] rows, int[] cols, int state) {
        for (int i = 0; i < rows.length; i++) {
            simulationGrid.updateCell(rows[i], cols[i], state);
        }
        simulationGrid.initializeDefaultCell(0);
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
