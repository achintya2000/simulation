package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.*;

import javafx.scene.paint.Color;

public class GameOfLife extends Simulation {


    public void loadSimulationContents(File file) {

        // Change below to list of cell types to change for each sim
        List<String> cellTypes = List.of("live");
        // See above

        List<String> xmlvals = new ArrayList<String>();
        xmlvals.addAll(List.of("title", "author", "simulation", "width", "height","default"));
        for (String celltype : cellTypes) {
            xmlvals.addAll(List.of("num"+celltype, "state"+celltype,celltype));
        }
        XMLParser parser = new XMLParser("config");
        Map<String, String> configuration = parser.getInfo(file, xmlvals);

        SIMULATION_NAME = configuration.get("simulation");
        GRID_WIDTH = Integer.parseInt(configuration.get("width"));
        GRID_HEIGHT = Integer.parseInt(configuration.get("height"));

        simulationGrid = new ArrayGrid(GRID_WIDTH);
        initializeGrid(cellTypes, configuration);

        initializeColorMap();
    }

    @Override
    public void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                int aliveNeighbors = aliveNeighbors(r,c);
                if(simulationGrid.getReferenceState(r,c) == 1 && aliveNeighbors < 2){       // an alive cell dies if less than 2 alive neighbors
                    simulationGrid.updateCell(r,c,0);
                }
                else if(simulationGrid.getReferenceState(r,c) == 1 && aliveNeighbors >= 2 && aliveNeighbors <= 3){
                    simulationGrid.updateCell(r,c,1);
                }
                else if(simulationGrid.getReferenceState(r,c) == 1 && aliveNeighbors > 3){       // an alive cell with more then 3 neighbors dies
                    simulationGrid.updateCell(r,c,0);
                }
                else if(simulationGrid.getReferenceState(r,c) == 0 && aliveNeighbors == 3){           // a dead cell with exactly 3 neighbors comes back to life
                    simulationGrid.updateCell(r,c,1);
                }
            }
        }
    }

    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }

    @Override
    protected void initializeColorMap() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(0, Color.WHITE);
        cellColorMap.put(1, Color.BLACK);
    }

    @Override
    public Map<Integer, Color> getCellColorMap() {
        return cellColorMap;
    }

    private int aliveNeighbors(int r, int c){
        int alive = 0;
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true, true);
        int i = 0;
        while (i < statusOfNeighbors.length && statusOfNeighbors[i] != -1 ){
            if(statusOfNeighbors[i] == 1){
                alive++;
            }
            i++;
        }
        return alive;
    }

}
