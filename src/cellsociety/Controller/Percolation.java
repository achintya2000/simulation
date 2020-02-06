package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Percolation extends Simulation {

    // Should be reading these in from file, would need to think about how to
    private int open = 0;
    private int percolated = 1;
    private int blocked = 2;

    public void loadSimulationContents(File file) {
        // Change below to list of cell types to change for each sim
        List<String> cellTypes = List.of("open","percolated");
        // See above

        List<String> xmlvals = new ArrayList<String>();
        xmlvals.addAll(List.of("title", "author", "simulation", "width", "height","default"));
        for (String celltype : cellTypes) {
            xmlvals.addAll(List.of("num"+celltype, "state"+celltype,celltype));
        }
        XMLParser parser = new XMLParser("config");
        Map<String, String> configuration = parser.getInfo(file, xmlvals);
        System.out.println(configuration);

        SIMULATION_NAME = configuration.get("simulation");
        GRID_WIDTH = Integer.parseInt(configuration.get("width"));
        GRID_HEIGHT = Integer.parseInt(configuration.get("height"));

        simulationGrid = new ArrayGrid(GRID_WIDTH);
        initializeGrid(cellTypes, configuration);

        initializeColorMap();
        System.out.println();
    }

    @Override
    public void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                simulationGrid.checkNeighbors(r, c, true, true);
                if(canFlow(r,c) && closeToWater(r,c)){
                    simulationGrid.updateCell(r,c,percolated);
                }
            }
        }
    }

    @Override
    public Grid getGrid() {
        return simulationGrid;
    }

    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }

    @Override
    protected void initializeColorMap() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(open, Color.WHITE);
        cellColorMap.put(percolated, Color.BLUE);
        cellColorMap.put(blocked, Color.BLACK);
    }

    @Override
    public Map<Integer, Color> getCellColorMap() {
        return cellColorMap;
    }


    public boolean closeToWater(int r, int c){
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true, true);
        int i = 0;
        while (i < 8 && statusOfNeighbors[i] != -1){
            if(statusOfNeighbors[i] == percolated){
               return true;
            }
            i++;
        }
        return false;
    }

    public boolean canFlow(int r, int c){
        return (simulationGrid.getReferenceState(r,c) != blocked);
    }

}
