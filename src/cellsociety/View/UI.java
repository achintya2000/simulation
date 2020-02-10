package cellsociety.View;

import java.io.IOException;
import java.util.*;

import cellsociety.Controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.text.Text;
import java.io.File;

public class UI extends Application {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 400;
    private static final String RESOURCES = "cellsociety/View/Resources/";
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private static final String STYLESHEET = "styles.css";
    private static final String DEFAULTSIMULATION = "./Resources/fire.xml";
    private static final String gameoflife = "GameofLife";
    private static final String GAMEOFLIFE = "Game of Life";
    private static final String FIRE = "Fire";
    private static final String PERCOLATION = "Percolation";
    private static final String WATOR = "Wator";
    private static final String SEGREGATION = "Segregation";
    private static final String RPS = "RPS";
    private static final String TITLE = "title";
    private static final String BADINPUT = "badinput";
    private static final String NOTXML = "notXML";
    private static final String CHOOSEANOTHERFILE = "chooseother";
    private static final String NEWSIM = "newSim";


    private static final int SPACING = 100;
    private static final float PERCENT_HEIGHT = 33.33F;
    private static final int colindex0 = 0;
    private static final int rowindex1 = 1;
    private static final int colindex2 = 2;
    private static final int TOP_INSET =20;
    private static final int BOTTOM_INSET =20;
    private static final int RIGHT_INSET =10;
    private static final int LEFT_INSET =0;


    private static final Map<String, Simulation> chooseSim = Map.of(GAMEOFLIFE,new GameOfLife(),FIRE, new Fire(), SEGREGATION, new Segregation(), PERCOLATION, new Percolation(), WATOR, new Wator(), RPS, new RPS());
    private static final Map<String, String> chooseSimName = Map.of(GAMEOFLIFE,"gameoflife",FIRE, "fire", SEGREGATION, "segregation", PERCOLATION, "percolation", WATOR, "wator", RPS, "rps");
    private Map<Integer, String> allNeighbors = Map.ofEntries(Map.entry(0, "NW"),Map.entry(1, "N"),Map.entry(2, "NE"),Map.entry(10 , "W"),Map.entry(12, "E"),Map.entry(20, "SW"),Map.entry(21, "S"),Map.entry(22, "SE"), Map.entry( -21, "NWW"),Map.entry(-22, "NEE"),Map.entry(-20, "WW") ,Map.entry(-26, "EE"));
    private List<String> neighborstosend = new ArrayList<>();

    private static final String BROWSE = "browse";
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    FileChooser fileChooser = new FileChooser();
    private String myNewSimulation = "Fire";
    BorderPane root = new BorderPane();
    Stage PrimaryStage;
    private String myShapeChosen = "Square";
    private TilePane myNeighbors;
    private boolean isRandom = false;
    private String ENVIRONMENT = "finite";
    private int NUMSIDES = 4;

    public UI() throws IOException {
    }

    public static void main (String[] args) {
        launch(args);
    }

    /**
     * start() loads the default simulation, creates the inital timeline, calls makeScene which creates a new scene
     * **/

    @Override
    public void start(Stage primaryStage) throws Exception {
        PrimaryStage = primaryStage;
        String segregationConfiguration = DEFAULTSIMULATION;
       // loadSimulationChoice(FIRE, new File(segregationConfiguration));
        primaryStage.setTitle(myResources.getString(TITLE));
        primaryStage.setScene(makeScene());
        primaryStage.show();

        primaryStage.show();

    }

    private Scene makeScene() throws IOException {
        root.setLeft(setToolBox());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;
    }

    private Node setToolBox(){
        VBox left = new VBox(SPACING);
        GridPane leftPanel = new GridPane();
        leftPanel.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
        leftPanel.getStyleClass().add("leftpanel");
        for(int i = 0; i < 3; i++) {
            RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(PERCENT_HEIGHT);
        }

        leftPanel.add(setComboBox(),0,1);
        leftPanel.add(setChoseShapeeText(), 0, 2);
        leftPanel.add(setShapeComboBox(),1,2);
        leftPanel.add(addRamdomConfigText(),0,3);
        leftPanel.add(setramdomConfigBox(),1,3);
        leftPanel.add(setToroidComboBox(),0,4);
        leftPanel.add(setBrowseButton(),1,5);
        leftPanel.setPadding(new Insets(20, 10, 20, 0));

//        leftPanel.add(setComboBox(),colindex0,rowindex1);
//        leftPanel.add(new Text(myResources.getString("chooseFile")),colindex0,colindex2);
//        leftPanel.add(setBrowseButton(),rowindex1,colindex2);
//        leftPanel.setPadding(new Insets(TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET));

        left.getChildren().add(leftPanel);
        root.setBottom(setChooseNeighborsTilePane());
        return left;
    }

    private Node setBrowseButton(){
        Button browse = new Button();
        browse.setText(myResources.getString(BROWSE));
        browse.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(PrimaryStage);
            loadSimulationChoice(myNewSimulation, selectedFile);
        });
        return browse;
    }

    private Node setComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add("combobox");
        String[] choiceProperties = {NEWSIM, PERCOLATION, gameoflife, WATOR, SEGREGATION, FIRE, RPS};

        for(String choice: choiceProperties){
            comboBox.getItems().add(myResources.getString(choice));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e -> {
            String simulationChosen = (String) comboBox.getSelectionModel().getSelectedItem();
            myNewSimulation = simulationChosen;
        });
        return comboBox;
    }


    private Node setChoseShapeeText(){
        Text text = new Text(myResources.getString("chooseShape"));
        return text;
    }

    private void loadSimulationChoice(String simulation, File xmlFile) {
        try {
            ViewingWindow window = new ViewingWindow(chooseSim.get(simulation), xmlFile, chooseSimName.get(simulation), isRandom, neighborstosend, ENVIRONMENT,NUMSIDES );
        }
        catch(XMLException e){
                setErrorBox(chooseSim.get(simulation).getERROR_MESSAGE());
            }
        }

    private void setErrorBox(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(myResources.getString(BADINPUT));
        alert.setHeaderText(myResources.getString(NOTXML));
        alert.setContentText(message);
        //alert.setContentText(myResources.getString(CHOOSEANOTHERFILE));
        alert.showAndWait();
    }

    private Node setChooseNeighborsText(){
        Text text = new Text(myResources.getString("chooseNeighbors"));
        return text;
    }

    private Node addRamdomConfigText(){
        Text text = new Text(myResources.getString("randomConfiguration"));
        return text;
    }
    private CheckBox setramdomConfigBox(){
        CheckBox box = new CheckBox();
        box.setOnMousePressed(e->{
            isRandom = true;
        });
        return box;
    }

    public Node setChooseNeighborsTilePane(){
         myNeighbors = new TilePane();
        HBox box = new HBox();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double tileSize = 50;
                Square tile = new Square(i,j,tileSize,Color.WHITE);
                if(i == 1 && j == 1){
                    tile.myShape.setFill(Color.RED);
                }
                tile.myShape.setOnMousePressed(e-> {
                    if(tile.myNumber != 11){
                        tile.myShape.setFill(Color.BLUE);
                        int number = tile.myNumber;
                        String direction = allNeighbors.get(number);
                        System.out.println(direction);
                        neighborstosend.add(direction);
                    }
                });
                myNeighbors.getChildren().add(tile.myShape);
            }
        }
        myNeighbors.setHgap(5);
        myNeighbors.setVgap(5);
        myNeighbors.setAlignment(Pos.CENTER);
        myNeighbors.setPrefColumns(3);
        myNeighbors.setPadding(new Insets(100, 75, 20, 75));
        myNeighbors.prefRowsProperty();
        box.getChildren().add(myNeighbors);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node setQuestionText(){
        Text text = new Text(myResources.getString("customNeighbors"));
        return text;
    }

    private Node setToroidComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add("combobox");
        String[] environProp = {"environment", "finite", "toroid"};
        for(String s : environProp){
            comboBox.getItems().add(myResources.getString(s));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e->{
            ENVIRONMENT = (String) comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase();
        });
        return comboBox;
    }
    private Node setShapeComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add("combobox");
        String[] shapeProperties = {"shape", "square", "triangle"};
        for (String shape : shapeProperties){
            comboBox.getItems().add(myResources.getString(shape));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e-> {
            myShapeChosen = (String) comboBox.getSelectionModel().getSelectedItem();
        });
        return comboBox;
    }

}


