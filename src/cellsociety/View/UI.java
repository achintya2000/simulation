package cellsociety.View;

import cellsociety.Controller.*;
import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;
import java.sql.Time;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class UI extends Application {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private static String gameOfLifeConfiguration = "./Resources/gameoflife.xml";
    private static String fireConfiguration = "./Resources/fire.xml";
    private static String segregationConfiguration = "./Resources/segregation.xml";
    private static String percolationConfiguration = "./Resources/percolation.xml";
    private double timestep = 1000;

    private Timeline timeline;
    private Text testing;
//    private static final List<Simulation> PossibleSimulations = List.of(
//            new Fire(), new Segregation(), new Percolation(), new GameOfLife() {
//    });
    Fire fires = new Fire();
    GameOfLife gameOfLife = new GameOfLife();
    Segregation segregation = new Segregation();
    Percolation percolation = new Percolation();
    Simulation simulationchoice = gameOfLife;
    BorderPane root = new BorderPane();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameOfLife.loadSimulationContents(gameOfLifeConfiguration);
        segregation.loadSimulationContents(segregationConfiguration);
        //Setting the title to Stage.
        primaryStage.setTitle("Simulation");;
        createTimeline(timestep, Timeline.INDEFINITE);
        timeline.stop();
        //Adding the scene to Stage
        primaryStage.setScene(makeScene());
        primaryStage.show();
    }

    private void loadSimulationChoice(String simulation){
        switch (simulation){
            case "Game of Life":
                simulationchoice = gameOfLife;
                gameOfLife.loadSimulationContents(gameOfLifeConfiguration);
                break;
            case "Fire":
                simulationchoice = fires;
                fires.loadSimulationContents(fireConfiguration);
                break;
            case "Segregation":
                simulationchoice = segregation;
                segregation.loadSimulationContents(segregationConfiguration);
                break;
            case "Percolation":
                simulationchoice = percolation;
                percolation.loadSimulationContents(percolationConfiguration);
        }
        System.out.println(simulationchoice);
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
        ComboBox comboBox = new ComboBox();
        testing = new Text();
        testing.setFont(new Font(22));
        testing.setText("yo");
        comboBox.getItems().addAll(readText("Resources/SimulationMenuText.txt"));
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e -> {
            timeline.stop();
            loadSimulationChoice((String)comboBox.getSelectionModel().getSelectedItem());
            createTimeline(timestep,Timeline.INDEFINITE);
        });
        toolbar.getChildren().add(comboBox);
        toolbar.getChildren().add(testing);
        return toolbar;
    }

    private void createTimeline(double milliseconds, int cycleCount) {
        if (timeline!= null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            testing.setText(String.valueOf(Math.random()));
            simulationchoice.updateGrid();
            root.setCenter(buildGrid());
        }));
        timeline.setCycleCount(cycleCount);
        timeline.play();
    }

    private Node makeSimulationControls() throws FileNotFoundException {
        Scanner s = new Scanner(new File("Resources/Start.txt"));
        String[] buttonText = new String[3];
        int i = 0;
        while (s.hasNext()){
            buttonText[i] = s.nextLine();
            i++;
        }
        Slider slider = new Slider(100,1000, 100);
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

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = 100000/(double)newValue;
        createTimeline(value,Timeline.INDEFINITE);
        });
        HBox controls = new HBox();
        controls.getChildren().add(playButton);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(nextButton);
        controls.getChildren().add(slider);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        return controls;
    }

    private Node buildGrid() {
        Simulation currentSimulation = simulationchoice;
        double sizeFactor = 500;
        HBox wrapper = new HBox();
        Grid currentGrid = simulationchoice.getGrid();
        TilePane uiGrid = new TilePane();
        Map<Integer, Color> colorMap = simulationchoice.getCellColorMap();

        for (int i = 0; i < currentGrid.getSize(); i++) {
            for (int j = 0; j < currentGrid.getSize(); j++) {
                double tileSize = (sizeFactor/currentGrid.getSize()) - 10;
                uiGrid.getChildren().add(new Rectangle(tileSize, tileSize, colorMap.get(simulationchoice.getGrid().getCurrentState(i, j))));
            }
        }
        uiGrid.setHgap(10);
        uiGrid.setVgap(10);
        uiGrid.setAlignment(Pos.CENTER);
        uiGrid.setPrefColumns(simulationchoice.getSimulationCols());
        uiGrid.setPadding(new Insets(100, 75, 20, 75));
        uiGrid.prefRowsProperty();
        wrapper.getChildren().add(uiGrid);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

}
