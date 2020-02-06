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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
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


    private double timestep = 1000;
    private Timeline timeline;
    private Text SimulationName;

    Fire fire = new Fire();
    GameOfLife gameOfLife = new GameOfLife();
    Segregation segregation = new Segregation();
    Percolation percolation = new Percolation();
    Wator wator = new Wator();
    TilePane uiGrid = new TilePane();
    private Properties prop = readPropertiesFile("Resources/English.properties");

    FileChooser fileChooser = new FileChooser();

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

        String segregationConfiguration = "./Resources/fire.xml";
        fire.loadSimulationContents(new File(segregationConfiguration), "segregation");

        wator.loadSimulationContents(new File(segregationConfiguration), "segregation");


        //Setting the title to Stage.
        primaryStage.setTitle(prop.getProperty("title"));
        primaryStage.setScene(makeScene());
        primaryStage.show();

        //createTimeline(timestep, Timeline.INDEFINITE);
        //timeline.stop();
        //Adding the scene to Stage
    }

    public static void setErrorBox(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Bad Input");
        alert.setHeaderText("Not an XML File");
        alert.setContentText("Please choose another file");
        alert.showAndWait();
    }

    private Node setComboBox(){
        ComboBox comboBox = new ComboBox();
        String[] choiceProperties = {"newSim", "percolation", "GameofLife", "wator", "segregation", "fire"};
        for(String choice: choiceProperties){
            comboBox.getItems().add(prop.getProperty(choice));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(PrimaryStage);
            String simulationChosen = (String) comboBox.getSelectionModel().getSelectedItem();
            if (timeline != null) {
                timeline.stop();
            }
            loadSimulationChoice(simulationChosen, selectedFile);
            root.setCenter(buildGrid());
            createTimeline(timestep,Timeline.INDEFINITE);
            SimulationName.setText(simulationChosen);
        });
        return comboBox;
    }

    private void loadSimulationChoice(String simulation, File xmlFile){
        String simName = "";
        switch (simulation){
            case "Game of Life":
                simulationchoice = gameOfLife;
                simName = "gameoflife";
                break;
            case "Fire":
                simulationchoice = fire;
                simName = "fire";
                break;
            case "Segregation":
                simulationchoice = segregation;
                simName = "segregation";
                break;
            case "Percolation":
                simulationchoice = percolation;
                simName = "percolation";
                break;
            case "Wator":
                simulationchoice = wator;
                simName = "wator";
                break;
        }
        simulationchoice.loadSimulationContents(xmlFile,simName);
    }


    private Scene makeScene() throws IOException {
        root.setTop(makeSimulationToolbar());
        root.setBottom(makeSimulationControls());
        root.setCenter(buildGrid());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        root.getStylesheets().add(getClass().getResource("styles.css").toString());
        scene.setFill(Color.WHITE);
        return scene;
    }


    private Node makeSimulationToolbar() throws FileNotFoundException {
        HBox toolbar = new HBox();

        SimulationName = new Text();
        SimulationName.setFont(new Font(22));
        SimulationName.setText(prop.getProperty("fire")); //default simulation
        SimulationName.setFill(Color.WHITE);
        toolbar.getChildren().add(setComboBox());
        toolbar.getChildren().add(SimulationName);
        toolbar.setSpacing(MARGIN);
        return toolbar;
    }

    private void createTimeline(double milliseconds, int cycleCount) {
        if (timeline!= null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            simulationchoice.updateGrid();
            root.setCenter(buildGrid());
        }));
        timeline.setCycleCount(cycleCount);
    }

    private Node makeSimulationControls() {
        Button playButton = new Button();
        playButton.setText(prop.getProperty("play"));
        playButton.setOnAction(e -> {
            createTimeline(timestep,Timeline.INDEFINITE);
            timeline.play();
        });
        Button stopButton = new Button();
        stopButton.setText(prop.getProperty("stop"));
        stopButton.setOnAction(e -> timeline.stop());
        Button nextButton = new Button();
        nextButton.setText(prop.getProperty("next"));
        nextButton.setOnAction(e -> {
           createTimeline(1,1);
           timeline.play();
        });
        stopButton.setAlignment(Pos.CENTER);
        playButton.setAlignment(Pos.CENTER);

        HBox controls = new HBox();
        controls.getChildren().add(playButton);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(nextButton);
        controls.getChildren().add(makeSlider());
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(MARGIN);
        return controls;
    }
    private Node makeSlider(){
        Slider slider = new Slider(MINTIMESTEP,MAXTIMESTEP, 100);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = DIVISONFACTOR/(double)newValue;
            createTimeline(value,Timeline.INDEFINITE);
            timeline.play();
        });
        return slider;
    }

    private Node buildGrid() {
        HBox wrapper = new HBox();
        uiGrid = new TilePane();
        Grid currentGrid = simulationchoice.getGrid();
        Map<Integer, Color> colorMap = simulationchoice.getCellColorMap();
        System.out.println(Arrays.deepToString(simulationchoice.getGrid().getGrid()));
        for (int i = 0; i < currentGrid.getSize(); i++) {
            for (int j = 0; j < currentGrid.getSize(); j++) {
                double tileSize = (VIEWING_WINDOW_SIZE /currentGrid.getSize()) - MARGIN;
                uiGrid.getChildren().add(new Rectangle(tileSize, tileSize, colorMap.get(simulationchoice.getGrid().getCurrentState(i, j))));
            }
        }
        uiGrid.setHgap(MARGIN);
        uiGrid.setVgap(MARGIN);
        uiGrid.setAlignment(Pos.CENTER);
        uiGrid.setPrefColumns(simulationchoice.getSimulationCols());
        uiGrid.setPadding(new Insets(100, 75, 20, 75));
        uiGrid.prefRowsProperty();
        wrapper.getChildren().add(uiGrid);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        Properties prop = new Properties();
        try {
            prop.load(fis);
        } catch(IOException fnfe) {
            fnfe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }
}


