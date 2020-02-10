package cellsociety.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public class Wator extends Simulation{

  private List<String> defaultNeighbors =  List.of("N","S","E","W");
  private final static int SQUARE = 4;
  private int defaultShape = SQUARE;
  private int finite = 1;
  private int defaultEdge = finite;

  private int chronon = 0;
  private int[][] sharkEnergy;

  private int empty = 0;
  private int fish = 1;
  private int shark = 2;

  private static final int shark_lives = 3;
  private static final int fishRepTime = 5;

  @Override
  protected void init() {
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
          sharkEnergy[r][c] = shark_lives;
        } else {
          sharkEnergy[r][c] = 0;
        }
      }
    }
  }

  @Override
  public void updateGrid() {
    if (!simulationGrid.isNeighborhoodSet()) {
      simulationGrid.setNeighbors(defaultNeighbors, defaultShape, defaultEdge);
    }
    chronon++;
    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        simulationGrid.checkNeighbors(r, c, false);
        moveSharkFish(r, c);
      }
    }
    updateSharkEnergy();
  }

  private void moveSharkFish(int r, int c) {
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

  private void updateSharkEnergy() {
    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        if (sharkEnergy[r][c] != 0) {
          sharkEnergy[r][c]--;
        }
      }
    }
  }

  private void sharkGoesTo(int r, int c) {
    Map<String,Integer> neighbors = simulationGrid.checkNeighbors(r, c, false);
    boolean fishEaten = huntFish(neighbors,r,c);
    if (!fishEaten) {
      moveShark(neighbors,r,c);
    }
  }

  private boolean huntFish(Map<String,Integer> statusOfNeighbors, int r, int c) {
    boolean fishEaten = false;
    // Below determines if there are any fish around the shark, if they are, they are eaten
    for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
      if (entry.getValue() == fish && (simulationGrid.inBounds(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1]))) {
        sharkEnergy[r+ simulationGrid.getOffset(entry.getKey())[0]][c+ simulationGrid.getOffset(entry.getKey())[1]] = sharkEnergy[r][c]+1;
        sharkEnergy[r][c] = 0;
        simulationGrid.updateCell(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1], shark);
        simulationGrid.updateCell(r, c, empty);
        fishEaten=true;
        if (chronon % fishRepTime == 0) {
          sharkEnergy[r][c] = shark_lives;
          simulationGrid.updateCell(r,c, shark);
        }
        break;
      }
    }
    return fishEaten;
  }

  private void moveShark(Map<String,Integer> statusOfNeighbors, int r, int c) {
    // If the shark did not move to eat the fish, and a nearby location is empty, move
    for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
      if (entry.getValue() == empty && (simulationGrid.inBounds(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1]))) {
        sharkEnergy[r+simulationGrid.getOffset(entry.getKey())[0]][c+simulationGrid.getOffset(entry.getKey())[1]]=sharkEnergy[r][c];
        sharkEnergy[r][c]=0;
        simulationGrid.updateCell(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1], shark);
        simulationGrid.updateCell(r, c, empty);
        if (chronon % fishRepTime == 0) {
          sharkEnergy[r][c] = shark_lives;
          simulationGrid.updateCell(r,c, shark);
        }
        break;
      }
    }
  }

  private void fishGoesTo(int r, int c) {
    Map<String,Integer> statusOfNeighbors = simulationGrid.checkNeighbors(r, c,  false);
    for (Map.Entry<String,Integer> entry : statusOfNeighbors.entrySet()) {
      if (entry.getValue() == empty && (simulationGrid.inBounds(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1]))) {
        simulationGrid.updateCell(r + simulationGrid.getOffset(entry.getKey())[0], c + simulationGrid.getOffset(entry.getKey())[1], fish);
        if (chronon % fishRepTime != 0) { // Put fish in new spot
          simulationGrid.updateCell(r, c, empty);
        }
        break;
      }
    }

  }

  @Override
  public int getSimulationCols() {
    return GRID_WIDTH;
  }


}
