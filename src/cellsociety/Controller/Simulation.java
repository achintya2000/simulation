package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.paint.Color;
  
public abstract class Simulation {

  private static final int MIN_NEIGHBOR_EDGES = 2;
  private static final int MAX_NEIGHBOR_EDGES = 9;
  private static final int CIRCLE = 360;
  private static final int TRIANGLE = 180;

  private static final Map<String, Integer> edgeTypes = Map.of("finite",1,"toroidal",2);

  protected String SIMULATION_NAME;
  protected int GRID_WIDTH;
  protected int GRID_HEIGHT;
  protected Grid simulationGrid;
  protected Map<Integer, Color> cellColorMap;
  private File infoFile = new File("./Resources/simInfo.xml");
  private Map<String, String> configuration;
  private String ERROR_MESSAGE = "";

  public void setSimulationParameters(List<String> neighborhood, int shape, String edge) { // call after loadsimcontents!!
    if (validShape(shape) && edgeTypes.containsKey(edge)) {
      simulationGrid.setNeighbors(neighborhood,shape,edgeTypes.get(edge));
    } else {
      System.out.println("YIKES - This neighborhood/shape/edge combination is invalid"); // come back and change to exception
    }
  }

  private boolean validShape(int shape) {
    return ( ((shape-MIN_NEIGHBOR_EDGES)*TRIANGLE)*MIN_NEIGHBOR_EDGES % CIRCLE == 0 && shape > MIN_NEIGHBOR_EDGES && shape < MAX_NEIGHBOR_EDGES );
  }

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


//    for (Map.Entry<String, String> entry : configuration.entrySet()) {
//      if(entry.getValue().equals("")) {
//        ERROR_MESSAGE = "XML file value " + entry.getKey().toUpperCase() + " is null";
//        throw new XMLException("XML file value %s is null", entry.getKey());
//      } else if (!entry.getKey().matches("title|author|simulation")) {
//        if (entry.getValue().matches(".*[a-zA-Z]+.*")) {
//          ERROR_MESSAGE = "XML file value " + entry.getKey().toUpperCase() + " has improper format";
//          throw new XMLException("XML file value %s is improperly formatted", entry.getKey());
//        }
//      }
//    }
//
//    SIMULATION_NAME = configuration.get("simulation");
//    GRID_WIDTH = Integer.parseInt(configuration.get("width"));
//    GRID_HEIGHT = Integer.parseInt(configuration.get("height"));
//
//    simulationGrid = new ArrayGrid(GRID_WIDTH);
//
//    initializeGrid(cellTypes, configuration, random, numtypes);
//    init();

  }

  protected void initializeGrid(List<String> cellTypes, Map<String, String> configuration, boolean random, int range) {
    if (random){
      populateRandomGrid(range);
    } else {
      populateFileGrid(cellTypes, configuration);
    }
  }

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

  public Color getGridColor(int r, int c) {
    return cellColorMap.get(simulationGrid.getCurrentState(r, c));
  }

  public abstract void updateGrid();

  public abstract int getSimulationCols();

  protected abstract void init();

  public String getERROR_MESSAGE() {
    return ERROR_MESSAGE;
  }
}
