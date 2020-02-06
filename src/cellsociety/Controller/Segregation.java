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

  private int empty = 0;
  private int agent1 = 1;
  private int agent2 = 2;

  @Override
  public void updateGrid() {
      for (int r = 0; r < simulationGrid.getSize(); r++) {
        for (int c = 0; c < simulationGrid.getSize(); c++) {
            simulationGrid.checkNeighbors(r, c, true, true);
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
  protected void initializeColorMap() {
    cellColorMap = new HashMap<>();
    cellColorMap.put(empty, Color.WHITE);
    cellColorMap.put(agent1, Color.BLUE);
    cellColorMap.put(agent2, Color.RED);
  }

  @Override
  public Map<Integer, Color> getCellColorMap() {
    return cellColorMap;
  }

  @Override
  protected void init() {

  }

  private void movesLocation(int r, int c, int state) {
      int[] statusOfNeighbors = simulationGrid.checkNeighbors(r, c, true, true);
      int count = 0;
      for (int i = 0; i < statusOfNeighbors.length; i++) {
        if (statusOfNeighbors[i] == state) {
          count++;
        }
      }
      if (count/8.0 < moveProb) {
        outerloop:
        for (int i = 0; i < simulationGrid.getSize(); i++) {
          for (int j = 0; j < simulationGrid.getSize(); j++) {
            if (simulationGrid.getCurrentState(i, j) == 0) {
              simulationGrid.updateCell(i, j, state);
              break outerloop;
            }
          }
        }
        simulationGrid.updateCell(r, c, 0);
      }
  }
}
