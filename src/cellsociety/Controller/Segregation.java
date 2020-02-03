package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public class Segregation extends Simulation {

  @Override
  public void loadSimulationContents(String filepath) {

    List<String> cellTypes = List.of("agent1", "agent2");

    List<String> xmlvals = new ArrayList<String>();
    xmlvals.addAll(List.of("title", "author", "simulation", "width", "height","default"));
    for (String celltype : cellTypes) {
      xmlvals.addAll(List.of("num"+celltype, "state"+celltype,celltype));
    }
    XMLParser parser = new XMLParser("config");
    Map<String, String> configuration = parser.getInfo(new File(filepath), xmlvals);
    System.out.println(configuration);

    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));

    simulationGrid = new ArrayGrid(GRID_WIDTH);
    initializeGrid(cellTypes, configuration);

    initializeColorMap();
    System.out.println();
  }

  private void initializeGrid(List<String> cellTypes, Map<String, String> configuration) {
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

  @Override
  public void updateGrid() {
      for (int r = 0; r < simulationGrid.getSize(); r++) {
        for (int c = 0; c < simulationGrid.getSize(); c++) {
            
        }
      }
  }

  @Override
  public Grid getGrid() {
    return simulationGrid;
  }

  @Override
  int getSimulationCols() {
    return GRID_WIDTH;
  }

  @Override
  void initializeColorMap() {
    cellColorMap = new HashMap<>();
    cellColorMap.put(0, Color.WHITE);
    cellColorMap.put(1, Color.BLUE);
    cellColorMap.put(2, Color.RED);
  }

  @Override
  public Map<Integer, Color> getCellColorMap() {
    return cellColorMap;
  }
}
