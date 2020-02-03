package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public class Wator extends Simulation{

  @Override
  void loadSimulationContents(String filepath) {
    List<String> cellTypes = List.of("fish", "shark");

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
        simulationGrid.checkNeighbors(r, c, false);
        if (simulationGrid.getReferenceState(r, c) == 1) {
          fishGoesTo(r, c);
        } else if (simulationGrid.getReferenceState(r, c) == 2) {
          sharkGoesTo();
        }
      }
    }
  }

  private void sharkGoesTo() {

  }

  private void fishGoesTo(int r, int c) {

  }

  @Override
  public int getSimulationCols() {
    return GRID_WIDTH;
  }

  @Override
  void initializeColorMap() {
    cellColorMap = new HashMap<>();
    cellColorMap.put(0, Color.BLACK);
    cellColorMap.put(1, Color.GREEN);
    cellColorMap.put(2, Color.BLUE);
  }

  @Override
  public Map<Integer, Color> getCellColorMap() {
    return cellColorMap;
  }
}
