package cellsociety.View;

import cellsociety.Controller.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewingWindow {

    private TilePane myGrid;
    private Simulation mySimulation;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int MARGIN = 10;
    private BorderPane root;
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    private static final String RESOURCES = "cellsociety/View/Resources/";
    // use Java's dot notation, like with import, for properties
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private static final String STYLESHEET = "styles.css";
    private double timestep = 1000;
    private Timeline timeline;
    private static final String PLAY = "play";
    private static final String STOP = "stop";
    private static final String NEXT = "next";
    private static final int VIEWING_WINDOW_SIZE = 400;
    private static final int MAXTIMESTEP = 1000;
    private static final int MINTIMESTEP = 100;
    private static final int DIVISONFACTOR = 100000; //used with slider so that 10000/100 = 1000(Max) and  10000/1000 = 100(Min). Divided to that the sim speeds up as slider goes to the right
    private Stage myStage;
    public ViewingWindow(Simulation simulation){
        mySimulation = simulation;
        myGrid = new TilePane();
        myStage = new Stage();
        root = new BorderPane();
        timeline = new Timeline();
        start();
    }

    private void start(){
        myStage.setScene(buildScene());
        myStage.setTitle("Viewing Window");
        myStage.show();
    }

    private Scene buildScene(){
        root.setCenter(buildGrid());
        root.setBottom(makeSimulationControls());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;
    }

    private void createTimeline(double milliseconds, int cycleCount) {
        if (timeline!= null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            mySimulation.updateGrid();
            root.setCenter(buildGrid());
        }));
        timeline.setCycleCount(cycleCount);
    }



    private Node buildGrid() {
        HBox wrapper = new HBox();
        myGrid = new TilePane();

        for (int i = 0; i < mySimulation.getSimulationCols(); i++) {
            for (int j = 0; j < mySimulation.getSimulationCols(); j++) {
                double tileSize = (VIEWING_WINDOW_SIZE / mySimulation.getSimulationCols()) - MARGIN;
                myGrid.getChildren().add(new Rectangle(tileSize, tileSize, mySimulation.getGridColor(i, j)));
            }
        }
        myGrid.setHgap(MARGIN);
        myGrid.setVgap(MARGIN);
        myGrid.setAlignment(Pos.CENTER);
        myGrid.setPrefColumns(mySimulation.getSimulationCols());
        myGrid.setPadding(new Insets(100, 75, 20, 75));
        myGrid.prefRowsProperty();
        wrapper.getChildren().add(myGrid);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

    private Node makeSimulationControls() {
        Button playButton = new Button();
        playButton.setText(myResources.getString(PLAY));
        playButton.setOnAction(e -> {
            createTimeline(timestep,Timeline.INDEFINITE);
            timeline.play();
        });
        Button stopButton = new Button();
        stopButton.setText(myResources.getString(STOP));
        stopButton.setOnAction(e -> timeline.stop());
        Button nextButton = new Button();
        nextButton.setText(myResources.getString(NEXT));
        nextButton.setOnAction(e -> {
            createTimeline(1,1); // (1,1) means to create a new timeline with timestep 1 and cyclecount 1
            timeline.play();                             // so that the next button makes grid only update once
        });
        stopButton.setAlignment(Pos.CENTER);
        playButton.setAlignment(Pos.CENTER);

        HBox controls = new HBox();
        controls.getChildren().add(playButton);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(nextButton);
        controls.getChildren().add(makeSlider());
        //controls.setAlignment(Pos.CENTER);
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
}
