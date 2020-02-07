package cellsociety.Controller;

import java.util.*;

import javafx.scene.paint.Color;

public class GameOfLife extends Simulation {

    private int dead = 0;
    private int live = 1;

    @Override
    public void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                int aliveNeighbors = aliveNeighbors(r,c);
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

    private int aliveNeighbors(int r, int c){
        int alive = 0;
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true, true);
        int i = 0;
        while (i < statusOfNeighbors.length && statusOfNeighbors[i] != -1 ){
            if(statusOfNeighbors[i] == live){
                alive++;
            }
            i++;
        }
        return alive;
    }

}
