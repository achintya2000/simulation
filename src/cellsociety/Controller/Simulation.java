package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Simulation {

  protected String SIMULATION_NAME;
  protected int GRID_WIDTH;
  protected int GRID_HEIGHT;
  protected Grid simulationGrid;
  protected Map<Integer, Color> cellColorMap;

  protected void initializeGrid(List<String> cellTypes, Map<String, String> configuration) {
    String[] point = new String[2];
    for (String celltype : cellTypes) {
      String cellLocations = configuration.get(celltype);
      int k = 0;
      while(cellLocations.lastIndexOf("]") != cellLocations.indexOf("]")) {
        point = (cellLocations.substring(cellLocations.indexOf("[")+1, cellLocations.indexOf("]"))).split(",");
        simulationGrid.updateCell(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(configuration.get("state"+celltype)));
        cellLocations = cellLocations.substring(cellLocations.indexOf("]")+1, cellLocations.lastIndexOf("]")+1);
        k = k + 1;
      }
    }
    simulationGrid.initializeDefaultCell(Integer.parseInt(configuration.get("default")));
  }

  public Color getGridColor(int r, int c) {
    return cellColorMap.get(simulationGrid.getCurrentState(r, c));
  }

  protected abstract void initializeColorMap();

  public abstract void loadSimulationContents(File file);

  public abstract void updateGrid();

  public abstract int getSimulationCols();

  public abstract Map<Integer, Color> getCellColorMap();

}
