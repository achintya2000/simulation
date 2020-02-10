package cellsociety.View;

import cellsociety.Controller.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ResourceBundle;

public class ViewingWindow {

    private TilePane myGrid;
    private Simulation mySimulation;
    private Timeline myAnimation;
    private BorderPane myRoot;
    private Button myPlayButton;
    private Button myStopButton;
    private Button myNextButton;
    private Button mySaveButton;
    private Slider mySlider;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int MARGIN = 10;
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    private static final String RESOURCES = "cellsociety/View/Resources/";
    // use Java's dot notation, like with import, for properties
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private static final String STYLESHEET = "styles.css";
    private double timestep = 1000;
    private static final String PLAY = "play";
    private static final String STOP = "stop";
    private static final String NEXT = "next";
    private static final int VIEWING_WINDOW_SIZE = 400;
    private static final int MAXTIMESTEP = 1000;
    private static final int MINTIMESTEP = 100;
    private static final int DIVISONFACTOR = 100000; //used with slider so that 10000/100 = 1000(Max) and  10000/1000 = 100(Min). Divided to that the sim speeds up as slider goes to the right


    public ViewingWindow(Simulation simulation, File xml, String simname){
        mySimulation = simulation;
        mySimulation.loadSimulationContents(xml, simname,false);
        myGrid = new TilePane();
        myRoot = new BorderPane();
        myAnimation = createTimeline(timestep,Timeline.INDEFINITE);
        myPlayButton = new Button();
        myNextButton = new Button();
        myStopButton = new Button();
        mySaveButton = new Button();
        mySlider = new Slider(MINTIMESTEP,MAXTIMESTEP, 100);
        start(new Stage());
    }

    private void start(Stage primaryStage){
        primaryStage.setTitle("Viewing Window");
        primaryStage.setScene(buildScene());
        primaryStage.show();
    }

    private Scene buildScene(){
        myRoot.setCenter(buildGrid());
        myRoot.setBottom(makeSimulationControls());
        Scene scene = new Scene(myRoot ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;
    }


    private Node buildGrid() {
        HBox wrapper = new HBox();
        myGrid = new TilePane();

        for (int i = 0; i < mySimulation.getSimulationCols(); i++) {
            for (int j = 0; j < mySimulation.getSimulationCols(); j++) {
                double tileSize = (VIEWING_WINDOW_SIZE / mySimulation.getSimulationCols()) - MARGIN;
                Rectangle rect = new Rectangle(tileSize, tileSize, mySimulation.getGridColor(i, j));
                rect.getStyleClass().add("Rectangle");
                myGrid.getChildren().add(rect);

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
        myPlayButton.setText(myResources.getString(PLAY));
        myPlayButton.setOnAction(e -> myAnimation.play());
        myStopButton = new Button();
        myStopButton.setText(myResources.getString(STOP));
        myStopButton.setOnAction(e -> myAnimation.pause());
        myNextButton.setText(myResources.getString(NEXT));
        myNextButton.setOnAction(e -> {
            myAnimation = createTimeline (1,1); //
            // means to create a new timeline with timestep 1 and cyclecount 1
            myAnimation.play();                           // so that the next button makes grid only update once
        });
        myStopButton.setAlignment(Pos.CENTER);
        myPlayButton.setAlignment(Pos.CENTER);

        mySaveButton.setText("Save");
        mySaveButton.setOnAction(e -> mySimulation.saveCurrentState());
        return getHBox();
    }

    private HBox getHBox() {
        HBox controls = new HBox();
        controls.getChildren().add(myPlayButton);
        controls.getChildren().add(myStopButton);
        controls.getChildren().add(myNextButton);
        controls.getChildren().add(mySaveButton);
        controls.getChildren().add(makeSlider());
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(MARGIN);
        return controls;
    }

    private Node makeSlider(){
        mySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = DIVISONFACTOR/(double)newValue;
            myAnimation = createTimeline(value,Timeline.INDEFINITE);
            myAnimation.play();
        });
        return mySlider;
    }

    private Timeline createTimeline(double timestep, int cyclecount){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(timestep), actionEvent -> {
            mySimulation.updateGrid();
            myRoot.setCenter(buildGrid());
        }));
        timeline.setCycleCount(cyclecount);
        return timeline;
    }
}
