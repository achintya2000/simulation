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
    private static final int HEIGHT = 600;
    private static final int WIDTH = 400;
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
    private static final String TITLE = "title";
    private static final String BADINPUT = "badinput";
    private static final String NOTXML = "notXML";
    private static final String CHOOSEANOTHERFILE = "chooseother";
    private static final String NEWSIM = "newSim";
    private static final String BROWSE = "browse";
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    FileChooser fileChooser = new FileChooser();
    private String myNewSimulation = "Fire";
    BorderPane root = new BorderPane();
    Stage PrimaryStage;
    List<ViewingWindow> myWindows = new ArrayList<>();

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
        String[] choiceProperties = {NEWSIM, PERCOLATION, gameoflife, WATOR, SEGREGATION, FIRE};
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

    private void loadSimulationChoice(String simulation, File xmlFile){
        Simulation simulation1 = new GameOfLife();
        String simName = "";
        try{
        switch (simulation){
            case GAMEOFLIFE:
                simulation1 = new GameOfLife();
                simName = "gameoflife";
                ViewingWindow window =  new ViewingWindow(simulation1,xmlFile,simName);
                myWindows.add(window);
                break;
            case FIRE:
                simulation1 = new Fire();
                simName = "fire";
                ViewingWindow window2 =  new ViewingWindow(simulation1,xmlFile,simName);
                myWindows.add(window2);
                break;
            case SEGREGATION:
                simulation1 = new Segregation();
                simName = "segregation";
                ViewingWindow window3 =  new ViewingWindow(simulation1,xmlFile,simName);
                myWindows.add(window3);
                break;
            case PERCOLATION:
                simulation1 = new Percolation();
                simName = "percolation";
                ViewingWindow window4 =  new ViewingWindow(simulation1,xmlFile,simName);
                myWindows.add(window4);
                break;
            case WATOR:
                simulation1 = new Wator();
                simName = "wator";
                ViewingWindow window5 =  new ViewingWindow(simulation1,xmlFile,simName);
                myWindows.add(window5);
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


