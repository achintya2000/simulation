package cellsociety.Controller;

import javafx.scene.paint.Color;

import java.util.*;

public class RPS extends Simulation {
    private List<String> defaultNeighbors =   List.of("N","S","E","W","NW","NE","SW","SE");
    private int square = 4;
    private int defaultShape = square;
    private int finite = 1;
    private int defaultEdge = finite;

    private int numNeighborTypes = 3;
    private Map<Integer,Integer> winnerToLoser = Map.of(0,2,1,0,2,1);

    private static final int WIN_THRESH = 3;

    @Override
    public void updateGrid() {
        if (!simulationGrid.isNeighborhoodSet()) {
            simulationGrid.setNeighbors(defaultNeighbors, defaultShape, defaultEdge);
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
