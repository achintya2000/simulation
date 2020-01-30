package cellsociety;

import java.io.File;
import java.util.Map;

public class Simulation {

  private String SIMULATION_NAME;
  private int GRID_WIDTH;
  private int GRID_HEIGHT;

  Simulation() {

  }

  public void loadSimulationContents(String filepath) {
    Layout gameoflife = new Layout("config");
    Map<String, String> configuration = gameoflife.getInfo(new File(filepath));
    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
  }


}
