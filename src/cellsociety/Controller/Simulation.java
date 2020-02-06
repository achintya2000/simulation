package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;
  public Grid simulationGrid;
  public Map<Integer, Color> cellColorMap;

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

  protected abstract void initializeColorMap();

  public abstract void loadSimulationContents(File file);

  public abstract void updateGrid();

  public abstract Grid getGrid();

  public abstract int getSimulationCols();

  public abstract Map<Integer, Color> getCellColorMap();

}
