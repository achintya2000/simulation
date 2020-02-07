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

    private List<String> requestedNeighbors =  List.of("N","S","E","W","NW","NE","SW","SE");

    private int open = 0;
    private int percolated = 1;
    private int blocked = 2;

    @Override
    public void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                simulationGrid.checkNeighbors(r, c,  true);
                if(canFlow(r,c) && closeToWater(r,c)){
                    simulationGrid.updateCell(r,c,percolated);
                }
            }
        }
    }

    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }

    @Override
    public Map<Integer, Color> getCellColorMap() {
        return cellColorMap;
    }

    @Override
    protected void init() {
        simulationGrid.setNeighbors(requestedNeighbors);
        cellColorMap = new HashMap<>();
        cellColorMap.put(open, Color.WHITE);
        cellColorMap.put(percolated, Color.BLUE);
        cellColorMap.put(blocked, Color.BLACK);
    }


    public boolean closeToWater(int r, int c){
        Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
        for (String neighbor : statusOfNeighbors.keySet()) {
            if(statusOfNeighbors.get(neighbor) == percolated){
                return true;
            }
        }
        return false;
    }

    public boolean canFlow(int r, int c){
        return (simulationGrid.getReferenceState(r,c) != blocked);
    }

}
