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
    private static final int WIDTH = 900;

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



    private double timestep = 1000;
    private Timeline timeline;
    private String myCurrentSimulation = "Fire";
    Fire fire = new Fire();
    GameOfLife gameOfLife = new GameOfLife();
    Segregation segregation = new Segregation();
    Percolation percolation = new Percolation();
    Wator wator = new Wator();
    TilePane uiGrid = new TilePane();
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");

    FileChooser fileChooser = new FileChooser();
    private String myNewSimulation = "Fire";
    private Simulation simulationchoice = fire;
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
        //fire.loadSimulationContents(new File(segregationConfiguration), FIRE.toLowerCase());
        //Setting the title to Stage.
        primaryStage.setTitle(myResources.getString(TITLE));
        primaryStage.setScene(makeScene());
        primaryStage.show();

    }

    public void setErrorBox(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(myResources.getString(BADINPUT));
        alert.setHeaderText(myResources.getString(NOTXML));
        alert.setContentText(myResources.getString(CHOOSEANOTHERFILE));
        alert.showAndWait();
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
            if (timeline != null) {
                timeline.stop();
            }
            loadSimulationChoice(myNewSimulation, selectedFile);
            myCurrentSimulation = myNewSimulation;
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
        String simName = "";
        switch (simulation){
            case GAMEOFLIFE:
                simulationchoice = gameOfLife;
                simName = "gameoflife";
                break;
            case FIRE:
                simulationchoice = fire;
                simName = "fire";
                break;
            case SEGREGATION:
                simulationchoice = segregation;
                simName = "segregation";
                break;
            case PERCOLATION:
                simulationchoice = percolation;
                simName = "percolation";
                break;
            case WATOR:
                simulationchoice = wator;
                simName = "wator";
                break;
        }

        try{
            simulationchoice.loadSimulationContents(xmlFile,simName);
            ViewingWindow window =  new ViewingWindow(simulationchoice);

        }
        catch(XMLException e){
            setErrorBox();
        }
    }


    private Scene makeScene() throws IOException {
        root.setLeft(setToolBox());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;
    }



//    private void createTimeline(double milliseconds, int cycleCount) {
//        if (timeline!= null) {
//            timeline.stop();
//        }
//        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
//            simulationchoice.updateGrid();
//            root.setCenter(buildGrid());
//        }));
//        timeline.setCycleCount(cycleCount);
//    }

//    private List<Node> makeSimulationControls() {
//        List<Node> list = new ArrayList<>();
//        Button playButton = new Button();
//        playButton.setText(myResources.getString(PLAY));
//        playButton.setOnAction(e -> {
//            createTimeline(timestep,Timeline.INDEFINITE);
//            timeline.play();
//        });
//        Button stopButton = new Button();
//        stopButton.setText(myResources.getString(STOP));
//        stopButton.setOnAction(e -> timeline.stop());
//        Button nextButton = new Button();
//        nextButton.setText(myResources.getString(NEXT));
//        nextButton.setOnAction(e -> {
//           createTimeline(1,1); // (1,1) means to create a new timeline with timestep 1 and cyclecount 1
//           timeline.play();                             // so that the next button makes grid only update once
//        });
//        stopButton.setAlignment(Pos.CENTER);
//        playButton.setAlignment(Pos.CENTER);
//
//        HBox controls = new HBox();
//        HBox sliderBox = new HBox();
//        controls.getChildren().add(playButton);
//        controls.getChildren().add(stopButton);
//        controls.getChildren().add(nextButton);
//        sliderBox.getChildren().add(makeSlider());
//        list.add(controls);
//        list.add(sliderBox);
//        //controls.setAlignment(Pos.CENTER);
//        controls.setSpacing(MARGIN);
//        return list;
//    }
//    private Node makeSlider(){
//        Slider slider = new Slider(MINTIMESTEP,MAXTIMESTEP, 100);
//        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            double value = DIVISONFACTOR/(double)newValue;
//            createTimeline(value,Timeline.INDEFINITE);
//            timeline.play();
//        });
//        return slider;
//    }

//    private Node buildGrid() {
//        HBox wrapper = new HBox();
//        uiGrid = new TilePane();
//
//        for (int i = 0; i < simulationchoice.getSimulationCols(); i++) {
//            for (int j = 0; j < simulationchoice.getSimulationCols(); j++) {
//                double tileSize = (VIEWING_WINDOW_SIZE / simulationchoice.getSimulationCols()) - MARGIN;
//                uiGrid.getChildren().add(new Rectangle(tileSize, tileSize, simulationchoice.getGridColor(i, j)));
//            }
//        }
//        uiGrid.setHgap(MARGIN);
//        uiGrid.setVgap(MARGIN);
//        uiGrid.setAlignment(Pos.CENTER);
//        uiGrid.setPrefColumns(simulationchoice.getSimulationCols());
//        uiGrid.setPadding(new Insets(100, 75, 20, 75));
//        uiGrid.prefRowsProperty();
//        wrapper.getChildren().add(uiGrid);
//        wrapper.setAlignment(Pos.CENTER);
//        return wrapper;
//    }


}


