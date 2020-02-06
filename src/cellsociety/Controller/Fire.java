package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.*;

import javafx.scene.paint.Color;

public class Fire extends Simulation {

    public static final float PROB_CATCH = .15F;
    public static final float PROB_GROW = .15F;

    public void loadSimulationContents(File file) {

        // Change below to list of cell types to change for each sim
        List<String> cellTypes = List.of("tree","burning");
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
    }

    @Override
    public void updateGrid() {
        for(int r = 0; r < simulationGrid.getSize(); r ++) {
            for (int c = 0; c < simulationGrid.getSize(); c++) {
                simulationGrid.checkNeighbors(r, c, false, true);
                if(simulationGrid.getReferenceState(r,c)==2) {
                    simulationGrid.updateCell(r,c,0);
                } else if (simulationGrid.getReferenceState(r,c)==1 && catchesFire(r,c)) {
                    simulationGrid.updateCell(r,c,2);
                } else if (simulationGrid.getReferenceState(r,c)==0) { // What do I do if cell is empty state?
                    if (growsTree(r,c)) {
                        simulationGrid.updateCell(r, c, 1);
                    } else {
                        simulationGrid.updateCell(r, c, 0);
                    }
                }
            }
        }
        System.out.println(Arrays.deepToString(simulationGrid.getGrid()));
    }

    @Override
    public int getSimulationCols() {
        return GRID_WIDTH;
    }

    @Override
    protected void initializeColorMap() {
        cellColorMap = new HashMap<>();
        cellColorMap.put(0, Color.WHITE);
        cellColorMap.put(1, Color.GREEN);
        cellColorMap.put(2, Color.ORANGE);
    }

    @Override
    public Map<Integer, Color> getCellColorMap() {
        return cellColorMap;
    }

    private boolean catchesFire(int r, int c) {
        int[] statusOfNeighbors = simulationGrid.checkNeighbors(r,c,false, true);
        int i = 0;
        int burning = 0;
        while (i < statusOfNeighbors.length && statusOfNeighbors[i] != -1 ){
            if(statusOfNeighbors[i] == 2){
                burning++;
            }
            i++;
        }
        if (burning != 0) {
            Random rand = new Random();
            float float_random = rand.nextFloat();
            if (float_random < PROB_CATCH) {
                return true;
            }
        }
        return false;
    }

    private boolean growsTree(int r, int c) {
        Random rand = new Random();
        float float_random = rand.nextFloat();
        if (float_random < PROB_GROW) {
            return true;
        }
        return false;
    }

}
