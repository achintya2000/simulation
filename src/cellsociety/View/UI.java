package cellsociety.View;

import cellsociety.Controller.*;
import cellsociety.Model.Grid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.text.Text;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class UI extends Application {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private static final int VIEWING_WINDOW_SIZE = 500;
    private static final int MARGIN = 10;
    private static final int MAXTIMESTEP = 1000;
    private static final int MINTIMESTEP = 100;
    private static final int DIVISONFACTOR = 100000; //used with slider so that 10000/100 = 1000(Max) and  10000/1000 = 100(Min). Divided to that the sim speeds up as slider goes to the right
    private static final String RESOURCES = "cellsociety/View/Resources/";
    // use Java's dot notation, like with import, for properties
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

    private static final String PLAY = "play";
    private static final String STOP = "stop";
    private static final String NEXT = "next";
    private static final boolean RANDOM_BOARD_GENERATION = true;


    private double timestep = 1000;
    private Timeline timeline;
    private Text SimulationName;

    Fire fire = new Fire();
    GameOfLife gameOfLife = new GameOfLife();
    Segregation segregation = new Segregation();
    Percolation percolation = new Percolation();
    Wator wator = new Wator();
    RPS rps = new RPS();
    TilePane uiGrid = new TilePane();
    private static final String BROWSE = "browse";
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    FileChooser fileChooser = new FileChooser();
    private String myNewSimulation = "Fire";
    BorderPane root = new BorderPane();
    Stage PrimaryStage;

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
        loadSimulationChoice(FIRE, new File(segregationConfiguration));
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


    private Node chooseBrowserText(){
        Text text = new Text(myResources.getString("chooseFile"));
        return text;
    }


    private Node setToolBox(){
        VBox left = new VBox(100);
        GridPane leftPanel = new GridPane();
        leftPanel.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
        leftPanel.getStyleClass().add("leftpanel");
        for(int i = 0; i < 3; i++){
            RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(33.33);
        }
        leftPanel.add(setComboBox(),0,1);
        leftPanel.add(chooseBrowserText(),0,2);
        leftPanel.add(setBrowseButton(),1,2);
        leftPanel.setPadding(new Insets(20, 10, 20, 0));
        left.getChildren().add(leftPanel);
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

    private void loadSimulationChoice(String simulation, File xmlFile) {
        // = new GameOfLife();
        String simName = "";
        try {
            switch (simulation) {
                case GAMEOFLIFE:
                    Simulation gol = new GameOfLife();
                    simName = "gameoflife";
                    ViewingWindow window = new ViewingWindow(gol, xmlFile, simName);
                    break;
                case FIRE:
                    Simulation fir = new Fire();
                    simName = "fire";
                    ViewingWindow window2 = new ViewingWindow(fir, xmlFile, simName);
                    break;
                case SEGREGATION:
                    Simulation seg = new Segregation();
                    simName = "segregation";
                    ViewingWindow window3 = new ViewingWindow(seg, xmlFile, simName);
                    break;
                case PERCOLATION:
                    Simulation perc = new Percolation();
                    simName = "percolation";
                    ViewingWindow window4 = new ViewingWindow(perc, xmlFile, simName);
                    break;
                case WATOR:
                    Simulation wat = new Wator();
                    simName = "wator";
                    ViewingWindow window5 = new ViewingWindow(wat, xmlFile, simName);
                    break;
                case RPS:
                    Simulation rp = new RPS();
                    simName = "rps";
                    ViewingWindow window6 = new ViewingWindow(rp, xmlFile, simName);
                    break;
            }
        }
        catch(XMLException e){
                setErrorBox();
            }
        }

    public void setErrorBox(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(myResources.getString(BADINPUT));
        alert.setHeaderText(myResources.getString(NOTXML));
        alert.setContentText(myResources.getString(CHOOSEANOTHERFILE));
        alert.showAndWait();
    }
}


