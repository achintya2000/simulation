package cellsociety.Controller;

import java.io.File;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Percolation extends Simulation {

    private List<String> defaultNeighbors =  List.of("N","S","E","W","NW","NE","SW","SE");
    private static final int SQUARE = 4;
    private int defaultShape = SQUARE;
    private int finite = 1;
    private int defaultEdge = finite;

    private int open = 0;
    private int percolated = 1;
    private int blocked = 2;

    /*
    * Creates Percolation object
    * */
    public Percolation() {
        loadSimulationContents(new File("./Resources/percolation.xml"), "percolation", true);
    }

    /**
     * Updates the simulation grid with rules specific to the percolation simulation..
     */
    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(defaultNeighbors, defaultShape,defaultEdge);
        }
        for(int r = 0; r < simulationGrid.getSize(); r ++){
            for(int c = 0; c < simulationGrid.getSize(); c ++){
                simulationGrid.checkNeighbors(r, c,  true);
                if(canFlow(r,c) && closeToWater(r,c)){
                    simulationGrid.updateCell(r,c,percolated);
                }
            }
        }
    }

    /**
     * Gets the number of simulation columns
     * @return int value of sim columns
     */
    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }


    @Override
    protected void init() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(open, Color.WHITE);
        cellColorMap.put(percolated, Color.BLUE);
        cellColorMap.put(blocked, Color.BLACK);
    }


    private boolean closeToWater(int r, int c){
        Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
        for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
            if(entry.getValue() == percolated){
                return true;
            }
        }
        return false;
    }

    private boolean canFlow(int r, int c){
        return (simulationGrid.getReferenceState(r,c) != blocked);
    }

}
