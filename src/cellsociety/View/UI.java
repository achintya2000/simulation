package cellsociety.View;

import cellsociety.Controller.*;
import cellsociety.Model.Grid;

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
    private static final int NUMBUTTONS = 3;
    private static final int MAXTIMESTEP = 1000;
    private static final int MINTIMESTEP = 100;
    private static final int DIVISONFACTOR = 100000; //used with slider so that 10000/100 = 1000(Max) and  10000/1000 = 100(Min). Divided to that the sim speeds up as slider goes to the right
    private static String segregationConfiguration = "./Resources/fire.xml";
    private double timestep = 1000;
    private Timeline timeline;
    private Text SimulationName;

    Fire fire = new Fire();
    GameOfLife gameOfLife = new GameOfLife();
    Segregation segregation = new Segregation();
    Percolation percolation = new Percolation();
    Wator wator = new Wator();

    FileChooser fileChooser = new FileChooser();

    private Simulation simulationchoice = fire;
    BorderPane root = new BorderPane();
    Stage PrimaryStage;


    public static void main (String[] args) {
        launch(args);
    }

    /**
     * start() loads the default simulation, creates the inital timeline, calls makeScene which creates a new scene
     * **/

    @Override
    public void start(Stage primaryStage) throws Exception {
        PrimaryStage = primaryStage;
        fire.loadSimulationContents(new File(segregationConfiguration));

        //Setting the title to Stage.
        primaryStage.setTitle("Simulation");
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

    private Node setComboBox() throws FileNotFoundException {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(readText("Resources/SimulationMenuText.txt"));
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(PrimaryStage);
            String simulationChosen = (String) comboBox.getSelectionModel().getSelectedItem();
            if (timeline != null) {
                timeline.stop();
            }
            loadSimulationChoice(simulationChosen, selectedFile);
            createTimeline(timestep,Timeline.INDEFINITE);
            SimulationName.setText(simulationChosen);
            buildGrid();
        });
        return comboBox;
    }

    private void loadSimulationChoice(String simulation, File xmlFile){
        switch (simulation){
            case "Game of Life":
                simulationchoice = gameOfLife;
                break;
            case "Fire":
                simulationchoice = fire;
                break;
            case "Segregation":
                simulationchoice = segregation;
                break;
            case "Percolation":
                simulationchoice = percolation;
                break;
            case "Wator":
                simulationchoice = wator;
                break;
        }
        simulationchoice.loadSimulationContents(xmlFile);
    }

    private static List<String> readText(String fname) throws FileNotFoundException {
        List<String> ret = new ArrayList<>();
        Scanner s = new Scanner(new File(fname));
        while (s.hasNext()){
            ret.add(s.nextLine());
        }
        return ret;
    }

    private Scene makeScene() throws FileNotFoundException {
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
        SimulationName.setText("Segregation"); //default simulation
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
        //timeline.play();
    }

    private Node makeSimulationControls() throws FileNotFoundException {
        Scanner s = new Scanner(new File("Resources/Start.txt"));
        String[] buttonText = new String[NUMBUTTONS];
        int i = 0;
        while (s.hasNext()){
            buttonText[i] = s.nextLine();
            i++;
        }
        Button playButton = new Button();
        playButton.setText(buttonText[0]);
        playButton.setOnAction(e -> {
            createTimeline(timestep,Timeline.INDEFINITE);
        });
        Button stopButton = new Button();
        stopButton.setText(buttonText[1]);
        stopButton.setOnAction(e -> timeline.stop());
        Button nextButton = new Button();
        nextButton.setText(buttonText[2]);
        nextButton.setOnAction(e -> {
           createTimeline(1,1);
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
        });
        return slider;
    }

    private Node buildGrid() {
        HBox wrapper = new HBox();
        Grid currentGrid = simulationchoice.getGrid();
        TilePane uiGrid = new TilePane();
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

}
