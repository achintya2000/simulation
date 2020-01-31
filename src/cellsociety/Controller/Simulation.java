package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.Map;

public abstract class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;


  public Simulation(String filepath) {
    XMLParser gameoflife = new XMLParser("config");
    Map<String, String> configuration = gameoflife.getInfo(new File(filepath));
    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
    Grid myGrid = new ArrayGrid(GRID_HEIGHT);
  }

  public ArrayGrid loadSimulationContents() {
    ArrayGrid simulation = new ArrayGrid(GRID_HEIGHT);
    return simulation;
  }

  abstract void updateGrid();

}
