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

  private static final float moveProb = (float) 0.30;
  int[] rDelta = {0,0,1,-1,1,1,-1,-1};
  int[] cDelta = {1,-1,0,0,1,-1,1,-1};

  private int empty = 0;
  private int agent1 = 1;
  private int agent2 = 2;

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
            simulationGrid.checkNeighbors(r, c, true);
            if (simulationGrid.getReferenceState(r, c) == agent1) {
              movesLocation(r, c, agent1);
            } else if (simulationGrid.getReferenceState(r, c) == agent2) {
              movesLocation(r, c, agent2);
            }
        }
      }
  }

  @Override
  public Grid getGrid() {
    return simulationGrid;
  }

  @Override
  public int getSimulationCols() {
    return GRID_WIDTH;
  }

  @Override
  void initializeColorMap() {
    cellColorMap = new HashMap<>();
    cellColorMap.put(empty, Color.WHITE);
    cellColorMap.put(agent1, Color.BLUE);
    cellColorMap.put(agent2, Color.RED);
  }

  @Override
  public Map<Integer, Color> getCellColorMap() {
    return cellColorMap;
  }

  private void movesLocation(int r, int c, int state) {
      int[] statusOfNeighbors = simulationGrid.checkNeighbors(r, c, true);
      int count = 0;
      for (int i = 0; i < statusOfNeighbors.length; i++) {
        if (statusOfNeighbors[i] == state) {
          count++;
        }
      }
      if (count/8.0 < moveProb) {
        for (int i = 0; i < statusOfNeighbors.length; i++) {
          if (statusOfNeighbors[i] == 0) {
            if(simulationGrid.inBounds(r + rDelta[i], c + cDelta[i])) {
              simulationGrid.updateCell(r, c, empty);
              simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], state);
              break;
            }
          }
        }
      }
  }
}
