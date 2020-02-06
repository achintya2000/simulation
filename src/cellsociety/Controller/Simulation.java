package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;
  public Grid simulationGrid;
  public Map<Integer, Color> cellColorMap;

  public void loadSimulationContents(File simFile, String simName) {
    File infoFile = new File("./Resources/simInfo.xml");

    List<String> numTypesRequest = new ArrayList<String>();
    numTypesRequest.addAll(List.of(simName+"numtypes"));
    XMLParser metaParser = new XMLParser("config");
    Map<String, String> numTypesFromFile = metaParser.getInfo(infoFile, numTypesRequest);
    int numtypes = Integer.parseInt(numTypesFromFile.get(simName+"numtypes"));

    List<String> cellTypes = new ArrayList<String>();
    for (int i = 0; i < numtypes-1; i ++) {
      cellTypes.add("celltype"+i);
    }

    List<String> xmlvals = new ArrayList<String>();
    xmlvals.addAll(List.of("title", "author", "simulation", "width", "height","default"));
    for (String celltype : cellTypes) {
      xmlvals.addAll(List.of("num"+celltype, "state"+celltype,celltype));
    }
    XMLParser simParser = new XMLParser("config");
    Map<String, String> configuration = simParser.getInfo(simFile, xmlvals);

    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));

    simulationGrid = new ArrayGrid(GRID_WIDTH);
    initializeGrid(cellTypes, configuration);
    initializeColorMap();
    init();
  }

  protected void initializeGrid(List<String> cellTypes, Map<String, String> configuration) {
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

  protected abstract void initializeColorMap();

  public abstract void updateGrid();

  public abstract Grid getGrid();

  public abstract int getSimulationCols();

  public abstract Map<Integer, Color> getCellColorMap();

  protected abstract void init();

}
