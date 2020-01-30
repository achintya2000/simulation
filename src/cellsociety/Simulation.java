package cellsociety;

import java.io.File;
import java.util.Map;

public class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;

  Simulation(String filepath) {
    Layout gameoflife = new Layout("config");
    Map<String, String> configuration = gameoflife.getInfo(new File(filepath));
    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
  }

  public ArrayGrid loadSimulationContents() {
    ArrayGrid simulation = new ArrayGrid(GRID_HEIGHT);
    return simulation;
  }

}
