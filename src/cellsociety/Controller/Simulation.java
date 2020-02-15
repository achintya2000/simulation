package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.paint.Color;

/**
 * Purpose: The purpose of this abstract class is create good design for our project by abstracting up common logic into this class so that all new
 * simulations can make use of this. Method signatures are written to be implemented so that logic for specific simulations can be implemented in each
 * class that extends this.
 * Assumptions: We assume that the simulation that extends this class will have an XML file that is formatted in the correct way and has no other
 * new requirements because this centralized the reading and grid building so a new type will not work.
 * Dependencies: Depends on standard java utils like List, Map, Random, and File as well as our own model classes Grid and ArrayGrid.
 * Example: An example of how to use this is any of the other simulation files in this package. You must extend this abstract package
 * and implement its methods in that child class.
 */
public abstract class Simulation {
  private static final int MIN_NEIGHBOR_EDGES = 2;
  private static final int MAX_NEIGHBOR_EDGES = 9;

  private static final Map<String, Integer> edgeTypes = Map.of("finite",1,"toroid",2);

  protected String SIMULATION_NAME;
  protected int GRID_WIDTH;
  protected int GRID_HEIGHT;
  protected Grid simulationGrid;
  protected Map<Integer, Color> cellColorMap;
  private File infoFile = new File("./Resources/simInfo.xml");
  private Map<String, String> configuration;
  private String ERROR_MESSAGE = "";

  /**
   * Loads contents of selected XML file for simulation
   * @return
   */
  public void loadSimulationContents(File simFile, String simName, boolean random) {
    List<String> cellTypes = getCellTypes(simName);
    List<String> xmlvals = getXMLTags(cellTypes);
    XMLParser simParser = new XMLParser("config");
    configuration = simParser.getInfo(simFile, xmlvals);
    checkXMLFileError(configuration);
    SIMULATION_NAME = configuration.get("simulation");
    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
    simulationGrid = new ArrayGrid(GRID_WIDTH);
    initializeGrid(cellTypes, configuration, random, cellTypes.size()+1);
    init();
  }

  /**
   * Allows user to set custom simulation parameters such as shape and edge type from UI
   * @return
   */
  public void setSimulationParameters(List<String> neighborhood, int shape, String edge) { // call after loadsimcontents!!
    if (validShape(shape) && edgeTypes.containsKey(edge)) {
      simulationGrid.setNeighbors(neighborhood,shape,edgeTypes.get(edge));
    } else {
      System.out.println("This neighborhood/shape/edge combination is invalid");
    }
  }

  /**
   * Allows users to dynamically update cell types from UI
   * @return
   */
  public void addCells(int numNewCells, int state) {
    int iter = 0;
    int maxiter = 50*numNewCells;
    while(numNewCells >= 0 && maxiter > iter) {
      int randRow = (int)((simulationGrid.getSize()-1)* Math.random());
      int randCol = (int)((simulationGrid.getSize()-1)* Math.random());
      if (simulationGrid.getCurrentState(randRow,randCol)!=state) {
        simulationGrid.updateCell(randRow,randCol,state);
        numNewCells--;
      }
    }
  }

  /**
   * Save current state of simulation from UI
   * @return
   */
  public void saveCurrentState() {
    XMLBuilder xmlBuilder = new XMLBuilder();
    int numCellType0 = 0;
    int numCellType1 = 0;
    int defaultNum = 0;
    int stateCellType0 = Integer.parseInt(configuration.get("statecelltype0"));
    int stateCellType1 = Integer.parseInt(configuration.get("statecelltype1"));
    int stateDefault = Integer.parseInt(configuration.get("default"));

    StringBuilder cellType0Location = new StringBuilder();
    StringBuilder cellType1Location = new StringBuilder();

    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        if (simulationGrid.getCurrentState(r, c) == stateCellType0) {
          numCellType0++;
          cellType0Location.append("[").append(r).append(",").append(c).append("]");
        } else if (simulationGrid.getCurrentState(r, c) == stateCellType1) {
          numCellType1++;
          cellType1Location.append("[").append(r).append(",").append(c).append("]");
        } else if (simulationGrid.getCurrentState(r, c) == defaultNum){
          defaultNum++;
        }
      }
    }
    cellType0Location.append("[]");
    cellType1Location.append("[]");
    xmlBuilder.buildXML(SIMULATION_NAME, Integer.toString(GRID_WIDTH), Integer.toString(GRID_HEIGHT),
        Integer.toString(numCellType0), Integer.toString(stateCellType0),
        cellType0Location.toString(), Integer.toString(numCellType1), Integer.toString(stateCellType1), cellType1Location.toString(),
        Integer.toString(stateDefault));
  }

  /**
   * Returns color associated with a given cell state
   * @return color
   */
  public Color getGridColor(int r, int c) {
    return cellColorMap.get(simulationGrid.getCurrentState(r, c));
  }

  /**
   * Updates the grid according to rules of simulation
   * @return
   */
  public abstract void updateGrid();

  /**
   * Returns grid width
   * @return grid width
   */
  public abstract int getSimulationCols();

  /**
   * Gets error message to display in alert box
   * @return string with specified xml tag
   */
  public String getERROR_MESSAGE() {
    return ERROR_MESSAGE;
  }

  protected void initializeGrid(List<String> cellTypes, Map<String, String> configuration, boolean random, int range) {
    if (random){
      populateRandomGrid(range);
    } else {
      populateFileGrid(cellTypes, configuration);
    }
  }

  protected abstract void init();

  private void populateFileGrid(List<String> cellTypes, Map<String, String> configuration) {
    String[] point = new String[2];
    for (String celltype : cellTypes) {
      String cellLocations = configuration.get(celltype);
      int k = 0;
      while (cellLocations.lastIndexOf("]") != cellLocations.indexOf("]")) {
        point = (cellLocations
            .substring(cellLocations.indexOf("[") + 1, cellLocations.indexOf("]"))).split(",");
        simulationGrid.updateCell(Integer.parseInt(point[0]), Integer.parseInt(point[1]),
            Integer.parseInt(configuration.get("state" + celltype)));
        cellLocations = cellLocations
            .substring(cellLocations.indexOf("]") + 1, cellLocations.lastIndexOf("]") + 1);
        k = k + 1;
      }
    }
    simulationGrid.initializeDefaultCell(Integer.parseInt(configuration.get("default")));
  }

  private void populateRandomGrid(int range) {
    for (int r = 0; r < simulationGrid.getSize(); r++) {
      for (int c = 0; c < simulationGrid.getSize(); c++) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, range);
        simulationGrid.updateCell(r, c, randomNum);
      }
    }
  }

  private boolean validShape(int shape) {
    return ( shape > MIN_NEIGHBOR_EDGES && shape < MAX_NEIGHBOR_EDGES );
  }

  private void checkXMLFileError(Map<String, String> configuration) {
    for (Map.Entry<String, String> entry : configuration.entrySet()) {
      if(entry.getValue().equals("")) {
        ERROR_MESSAGE = "XML file value " + entry.getKey().toUpperCase() + " is null";
        throw new XMLException("XML file value %s is null", entry.getKey());
      } else if (!entry.getKey().matches("title|author|simulation")) {
        if (entry.getValue().matches(".*[a-zA-Z]+.*")) {
          ERROR_MESSAGE = "XML file value " + entry.getKey().toUpperCase() + " has improper format";
          throw new XMLException("XML file value %s is improperly formatted", entry.getKey());
        }
      }
    }
  }

  private List<String> getCellTypes(String simName) {
    List<String> numTypesRequest = new ArrayList<>();
    numTypesRequest.addAll(List.of(simName+"numtypes"));
    XMLParser metaParser = new XMLParser("config");
    Map<String, String> numTypesFromFile = metaParser.getInfo(infoFile, numTypesRequest);
    int numtypes = Integer.parseInt(numTypesFromFile.get(simName+"numtypes"));
    List<String> cellTypes = new ArrayList<>();
    for (int i = 0; i < numtypes-1; i ++) {
      cellTypes.add("celltype"+i);
    }
    return cellTypes;
  }

  private List<String> getXMLTags(List<String> cellTypes) {
    List<String> xmlvals = new ArrayList<>();
    xmlvals.addAll(List.of("title", "author", "simulation", "width", "height","default"));
    for (String celltype : cellTypes) {
      xmlvals.addAll(List.of("num"+celltype, "state"+celltype,celltype));
    }
    return xmlvals;
  }
}
