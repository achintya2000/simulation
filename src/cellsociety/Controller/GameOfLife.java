package cellsociety.Controller;

import java.util.*;

import javafx.scene.paint.Color;

public class GameOfLife extends Simulation {

    private List<String> defaultNeighbors =  List.of("N","S","E","W","NW","NE","SW","SE");
    private int square = 4;
    private int defaultShape = square;

    private int dead = 0;
    private int live = 1;

    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(defaultNeighbors, defaultShape);
        }
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                int aliveNeighbors = liveNeighbors(r,c);
                if(simulationGrid.getReferenceState(r,c) == live && aliveNeighbors < 2){
                    simulationGrid.updateCell(r,c,dead);
                }
                else if(simulationGrid.getReferenceState(r,c) == live && aliveNeighbors >= 2 && aliveNeighbors <= 3){
                    simulationGrid.updateCell(r,c,live);
                }
                else if(simulationGrid.getReferenceState(r,c) == live && aliveNeighbors > 3){
                    simulationGrid.updateCell(r,c,dead);
                }
                else if(simulationGrid.getReferenceState(r,c) == dead && aliveNeighbors == 3){
                    simulationGrid.updateCell(r,c,live);
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
        cellColorMap = new HashMap<>();
        cellColorMap.put(dead, Color.WHITE);
        cellColorMap.put(live, Color.BLACK);
    }

    private int liveNeighbors(int r, int c){
        Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
        int alive = 0;
        for (String neighbor : statusOfNeighbors.keySet()) {
            if(statusOfNeighbors.get(neighbor) == live){
                alive++;
            }
        }
        return alive;
    }

}
