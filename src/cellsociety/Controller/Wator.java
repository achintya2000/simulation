package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public class Wator extends Simulation{

  private int chronon = 0;
  int[] rDelta = {0,0,1,-1};
  int[] cDelta = {1,-1,0,0};
  int[][] sharkEnergy;

  @Override
  public void loadSimulationContents(File file) {
    List<String> cellTypes = List.of("fish", "shark");

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

    sharkEnergy = new int[GRID_WIDTH][GRID_WIDTH];

    simulationGrid = new ArrayGrid(GRID_WIDTH);
    initializeGrid(cellTypes, configuration);

    System.out.println(Arrays.deepToString(simulationGrid.getGrid()));

    initializeColorMap();
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

    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        if (simulationGrid.getCurrentState(r, c) == 2) {
          sharkEnergy[r][c] = 5;
        }
      }
    }
  }

  @Override
  public void updateGrid() {
    chronon++;

    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        simulationGrid.checkNeighbors(r, c, false);
        if (simulationGrid.getReferenceState(r, c) == 1) {
          fishGoesTo(r, c);
        } else if (simulationGrid.getReferenceState(r, c) == 2) {
          sharkGoesTo(r, c);
        }
      }
    }

    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        if (sharkEnergy[r][c] != 0) {
          sharkEnergy[r][c]--;
        }
      }
    }

  }

  @Override
  public Grid getGrid() {
    return simulationGrid;
  }

  private void sharkGoesTo(int r, int c) {
      if (sharkEnergy[r][c] > 0) {
        int[] neighbors = simulationGrid.checkNeighbors(r, c, false);
        for (int i = 0; i < neighbors.length && i < 4; i++) {
          if (neighbors[i] == 1) {
            if (simulationGrid.inBounds(r+rDelta[i], c+cDelta[i])) {
              sharkEnergy[r + rDelta[i]][c + cDelta[i]] = sharkEnergy[r][c]++;
              sharkEnergy[r][c] = 0;

              simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], 2);
              simulationGrid.updateCell(r, c, 0);
              break;
            }
          }
        }
        for (int i = 0; i < neighbors.length && i < 4; i++) {
          if (neighbors[i] == 0) {
            if (simulationGrid.inBounds(r+rDelta[i], c+cDelta[i])) {
              if (chronon % 5 == 0) {
                sharkEnergy[r + rDelta[i]][c + cDelta[i]] = 5;
                simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], 1);
                break;
              } else {
                sharkEnergy[r + rDelta[i]][c + cDelta[i]] = sharkEnergy[r][c]++;
                sharkEnergy[r][c] = 0;

                simulationGrid.updateCell(r, c, 0);
                simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], 1);
                break;
              }
            }
          }
        }
      } else {
        simulationGrid.updateCell(r, c, 0);
      }
  }

  private void fishGoesTo(int r, int c) {
    int[] neighbors = simulationGrid.checkNeighbors(r, c, false);
    System.out.println(Arrays.toString(neighbors));
    for (int i = 0; i < neighbors.length && i < 4; i++) {
      if (neighbors[i] == 0) {
        if (simulationGrid.inBounds(r + rDelta[i], c + cDelta[i])) {
          if (simulationGrid.inBounds(r + rDelta[i], c + cDelta[i])) {
            if (chronon % 5 == 0) {
              simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], 1);
              break;
            } else {
              simulationGrid.updateCell(r, c, 0);
              simulationGrid.updateCell(r + rDelta[i], c + cDelta[i], 1);
              break;
            }
          }
        }
      }
    }
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
