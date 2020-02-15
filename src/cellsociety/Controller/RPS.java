package cellsociety.Controller;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RPS extends Simulation {
    private static final List<String> DEFAULT_NEIGHBORS = List.of("N","S","E","W","NW","NE","SW","SE");
    private static final int SQUARE = 4;
    private static final int DEFAULT_SHAPE = SQUARE;
    private static final int FINITE = 1;
    private static final int DEFAULT_EDGE = FINITE;

    private static final int numNeighborTypes = 3;
    private Map<Integer,Integer> winnerToLoser = Map.of(0,2,1,0,2,1);

    private static final int WIN_THRESH = 3;

    /**
     * Updates simulation grid based on rules for RPS game
     */
    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(DEFAULT_NEIGHBORS, DEFAULT_SHAPE, DEFAULT_EDGE);
        }
        for(int r = 0; r < simulationGrid.getSize(); r ++) {
            for (int c = 0; c < simulationGrid.getSize(); c++) {
                Map<String,Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r, c, true);
                int[] typeNeighbor = countNeighborTypes(statusOfNeighbors);
                updateCurrentCell(r, c, typeNeighbor);
            }
        }
    }

    private void updateCurrentCell(int r, int c, int[] typeNeighbor) {
        for (int i = 0; i < numNeighborTypes; i++) {
            if (typeNeighbor[i] > WIN_THRESH && beats(i,simulationGrid.getReferenceState(r,c))) {
                simulationGrid.updateCell(r,c,i);
            }
        }
    }

    private int[] countNeighborTypes(Map<String, Integer> statusOfNeighbors) {
        int[] typeNeighbor = new int[numNeighborTypes];
        for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
            for (int i = 0; i < numNeighborTypes; i++) {
                if (entry.getValue() == i) {
                    typeNeighbor[i] = typeNeighbor[i]+1;
                }
            }
        }
        return typeNeighbor;
    }

    private boolean beats(int candidateWinner, int candidateLoser) {
        return winnerToLoser.get(candidateWinner) == candidateLoser;
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
        cellColorMap.put(0, Color.LIGHTGRAY);
        cellColorMap.put(1, Color.ANTIQUEWHITE);
        cellColorMap.put(2, Color.BLUE);
    }

}
