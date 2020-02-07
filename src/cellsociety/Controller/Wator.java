package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.paint.Color;

public class Wator extends Simulation{

  private List<String> requestedNeighbors =  List.of("N","S","E","W");

  private int chronon = 0;
  private int[][] sharkEnergy;

  private int empty = 0;
  private int fish = 1;
  private int shark = 2;

  private int shark_lives = 3;

  @Override
  protected void init() {
    simulationGrid.setNeighbors(requestedNeighbors);
    cellColorMap = new HashMap<>();
    cellColorMap.put(0, Color.BLACK);
    cellColorMap.put(1, Color.GREEN);
    cellColorMap.put(2, Color.BLUE);
    sharkEnergy = new int[GRID_WIDTH][GRID_WIDTH];
    createSharkEnergyGrid();
  }

  private void createSharkEnergyGrid() {
    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        if (simulationGrid.getCurrentState(r, c) == shark) {
          //sharkEnergy.updateCell(r,c,shark_lives);
          sharkEnergy[r][c] = shark_lives;
        } else {
          //sharkEnergy.updateCell(r,c,1);
          sharkEnergy[r][c] = 0;
        }
      }
    }
    //sharkEnergy.setNeighbors(requestedNeighbors);
  }

  @Override
  public void updateGrid() {
    chronon++;
    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        simulationGrid.checkNeighbors(r, c, false);
        if (simulationGrid.getCurrentState(r, c) == shark) { // Must use current state because sharks can move during updates
          if (sharkEnergy[r][c] <= 0) {
            simulationGrid.updateCell(r, c, empty);
          } else {
            sharkGoesTo(r, c);
          }
        } else if (simulationGrid.getCurrentState(r, c) == fish) { // Must use current state because fish can move during updates
          fishGoesTo(r, c);
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

  private void sharkGoesTo(int r, int c) {
    boolean fisheaten = false; // if fish not eaten after first loop, shark has moved
    Map<String,Integer> neighbors = simulationGrid.checkNeighbors(r, c, false);

    // Below determines if there are any fish around the shark, if they are, they are eaten
    for (String neighbor: neighbors.keySet()) {
      if (neighbors.get(neighbor) == fish) { // take first neighbor that is fish
        if (simulationGrid.inBounds(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1])) { // this needs to get fixed asap
          sharkEnergy[r+ simulationGrid.getOffset(neighbor)[0]][c+ simulationGrid.getOffset(neighbor)[1]] = sharkEnergy[r][c]+1;
          sharkEnergy[r][c] = 0;
          simulationGrid.updateCell(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1], shark);
          simulationGrid.updateCell(r, c, empty);
          fisheaten=true;
          if (chronon % 5 == 0) {
            sharkEnergy[r][c] = shark_lives;
            simulationGrid.updateCell(r,c, shark);
          }
          break;
        }
      }
    }
    // If the shark did not move to eat the fish, and a nearby location is empty, move
    if (!fisheaten) {
      for (String neighbor: neighbors.keySet()) {
        if (neighbors.get(neighbor) == empty) {
          if (simulationGrid.inBounds(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1])) {
            sharkEnergy[r+simulationGrid.getOffset(neighbor)[0]][c+simulationGrid.getOffset(neighbor)[1]]=sharkEnergy[r][c];
            sharkEnergy[r][c]=0;
            simulationGrid.updateCell(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1], shark);
            simulationGrid.updateCell(r, c, empty);
            if (chronon % 5 == 0) {
              sharkEnergy[r][c] = shark_lives;
              simulationGrid.updateCell(r,c, shark);
            }
            break;
          }
        }
      }
    }
  }

  private void fishGoesTo(int r, int c) {
    Map<String,Integer> neighbors = simulationGrid.checkNeighbors(r, c,  false);

    for (String neighbor: neighbors.keySet()) {
      if (neighbors.get(neighbor) == empty) {
        if (simulationGrid.inBounds(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1])) {
          simulationGrid.updateCell(r + simulationGrid.getOffset(neighbor)[0], c + simulationGrid.getOffset(neighbor)[1], fish);
          if (chronon % 5 != 0) { // Put fish in new spot
            simulationGrid.updateCell(r, c, empty);
          }
          break;
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

}
