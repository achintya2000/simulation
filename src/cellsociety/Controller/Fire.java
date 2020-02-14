package cellsociety.Controller;

import java.util.*;
import javafx.scene.paint.Color;

public class Fire extends Simulation {

    private static final List<String> DEFAULT_NEIGHBORS =  List.of("N","S","E","W");
    private static final int SQUARE = 4;
    private static final int DEFAULT_SHAPE = SQUARE;
    private static final int FINITE = 1;
    private static final int DEFAULT_EDGE = FINITE;

    private static final float PROB_CATCH = 1F;
    private static final float PROB_GROW = .01F;

    private int empty = 0;
    private int tree = 1;
    private int burning = 2;

    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(DEFAULT_NEIGHBORS, DEFAULT_SHAPE, DEFAULT_EDGE);
        }
        for(int r = 0; r < simulationGrid.getSize(); r ++) {
            for (int c = 0; c < simulationGrid.getSize(); c++) {
                simulationGrid.checkNeighbors(r, c, true);
                updateCurrentCell(r, c);
            }
        }
    }

    private void updateCurrentCell(int r, int c) {
        if(simulationGrid.getReferenceState(r,c)==burning) {
            simulationGrid.updateCell(r,c,empty);
        } else if (simulationGrid.getReferenceState(r,c)==tree && catchesFire(r,c)) {
            simulationGrid.updateCell(r,c,burning);
        } else if (simulationGrid.getReferenceState(r,c)==empty && growsTree(r,c)) {
            simulationGrid.updateCell(r, c,tree);
        }
    }

    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }

    @Override
    protected void init() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(empty, Color.WHITE);
        cellColorMap.put(tree, Color.GREEN);
        cellColorMap.put(burning, Color.ORANGE);
    }

    private boolean catchesFire(int r, int c) {
        Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
        Random rand = new Random();
        float float_random = rand.nextFloat();
        for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
            if(entry.getValue() == burning && float_random < PROB_CATCH){
                return true;
            }
        }
        return false;
    }

    private boolean growsTree(int r, int c) {
        Random rand = new Random();
        float float_random = rand.nextFloat();
        return float_random < PROB_GROW;
    }

}
