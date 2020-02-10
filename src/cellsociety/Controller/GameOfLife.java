package cellsociety.Controller;

import java.io.File;
import java.util.*;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOfLife extends Simulation {

    private List<String> defaultNeighbors =  List.of("N","S","E","W","NW","NE","SW","SE");
    private static final int SQUARE = 4;
    private int defaultShape = SQUARE;
    private static final int finite = 1;
    private int defaultEdge = finite;
    private static final int numtoLive = 2;
    private static final int numtoDie = 3;

    private static final int dead = 0;
    private static final int live = 1;


    public GameOfLife(){
        loadSimulationContents(new File("./Resources/gameoflife.xml"), "gameoflife", true);
    }

    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(defaultNeighbors, defaultShape, defaultEdge);
        }
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                int aliveNeighbors = liveNeighbors(r,c);
                if(simulationGrid.getReferenceState(r,c) == live && (aliveNeighbors < numtoLive || aliveNeighbors > numtoDie )){
                    simulationGrid.updateCell(r,c,dead);
                }
                else if(simulationGrid.getReferenceState(r,c) == dead && aliveNeighbors == numtoDie){
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
    protected void init() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(dead, Color.WHITE);
        cellColorMap.put(live, Color.BLACK);
    }

    private int liveNeighbors(int r, int c){
        Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
        int alive = 0;
        for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
            if(entry.getValue() == live){
                alive++;
            }
        }
        return alive;
    }

}
