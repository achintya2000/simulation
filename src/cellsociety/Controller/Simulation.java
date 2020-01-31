package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.Map;

public abstract class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;
  public Grid simulationGrid;

  abstract void loadSimulationContents(String filepath);

  abstract void updateGrid();

  abstract Grid getGrid();

  abstract int getSimulationCols();

}
