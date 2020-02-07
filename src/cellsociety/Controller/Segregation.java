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

  private List<String> defaultNeighbors =  List.of("N","S","E","W","NW","NE","SW","SE");
  private int square = 4;
  private int defaultShape = square;

  private int empty = 0;
  private int agent1 = 1;
  private int agent2 = 2;

  @Override
  public void updateGrid() {
      if (!simulationGrid.isNeighborhoodSet()) {
          simulationGrid.setNeighbors(defaultNeighbors, defaultShape);
      }
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
  public int getSimulationCols() {
    return GRID_WIDTH;
  }

  @Override
  public Map<Integer, Color> getCellColorMap() {
    return cellColorMap;
  }

  @Override
  protected void init() {
      cellColorMap = new HashMap<>();
      cellColorMap.put(empty, Color.WHITE);
      cellColorMap.put(agent1, Color.BLUE);
      cellColorMap.put(agent2, Color.RED);
  }

  private void movesLocation(int r, int c, int state) {
      Map<String, Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r,c,true);
      int count = 0;
      for (String neighbor : statusOfNeighbors.keySet()) {
          if(statusOfNeighbors.get(neighbor) == state){
              count++;
          }
      }
      if (count/((float) statusOfNeighbors.size()) < moveProb) {
        outerloop:
        for (int i = 0; i < simulationGrid.getSize(); i++) {
          for (int j = 0; j < simulationGrid.getSize(); j++) {
            if (simulationGrid.getCurrentState(i, j) == empty) {
              simulationGrid.updateCell(i, j, state);
              break outerloop;
            }
          }
        }
        simulationGrid.updateCell(r, c, empty);
      }
  }
}
