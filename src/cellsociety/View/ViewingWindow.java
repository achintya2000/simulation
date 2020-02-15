package cellsociety.View;

import cellsociety.Controller.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

public class ViewingWindow {

    /**
     * This is the class the displays the simulation to the user. I think that this class is well designed because
     * each it is well encapsulated. The class can run without any dependencies other than the simulations class.
     * THe viewing window contains everything the user needs to view the simulation. With further development the user will also
     * be able to interact with the simulation dynamically.
     */

    private TilePane myGrid;
    private Simulation mySimulation;
    private Timeline myAnimation;
    private BorderPane myRoot;
    private Button myPlayButton;
    private Button myStopButton;
    private Button myNextButton;
    private Button mySaveButton;
    private Slider mySlider;

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static final int MARGIN = 10;
    private static final int VIEWING_WINDOW_SIZE = 500;
    private static final int MAXTIMESTEP = 1000;
    private static final int MINTIMESTEP = 100;
    private static final int DIVISONFACTOR = 100000; //used with slider so that 10000/100 = 1000(Max) and  10000/1000 = 100(Min). Divided to that the sim speeds up as slider goes to the right
    private double timestep = 1000;

    private static final String RESOURCES = "cellsociety/View/Resources/";
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private static final String STYLESHEET = "styles.css";
    private static final String PLAY = "play";
    private static final String STOP = "stop";
    private static final String NEXT = "next";
    private static final String RECTANGLESTYLE = "Rectangle";
    private static final String TITLE = "Viewing Window";

    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    private List<String> Neighbors;
    private CheckBox viewGraph;
    private int NUMSIDES ;
    private Chart myChart;
    private Text viewGraphText = new Text (myResources.getString("viewgraph"));


    /**
     * Constructor for viewing window which will create a new window for a simulation.
     * @param simulation -  Simulation: the type of simulation that is running
     * @param xml - File:the xml file
     * @param simname - String: the simulation name
     * @param random -  booleen: if the configuration is random or not
     * @param neighbors - List: a list of neighbors being checked
     * @param environ - String: Toroidal vs Finite
     * @param numsides int: number of sides ie type of tile
     */
    public ViewingWindow(Simulation simulation, File xml, String simname, boolean random, List<String> neighbors, String environ, int numsides){
        mySimulation = simulation;
        Neighbors = neighbors;
        mySimulation.loadSimulationContents(xml, simname,random);
        mySimulation.setSimulationParameters(Neighbors,numsides,environ);
        NUMSIDES = numsides;
        myGrid = new TilePane();
        myRoot = new BorderPane();
        mySlider = new Slider(MINTIMESTEP,MAXTIMESTEP, MINTIMESTEP);
        setGraphButton();
        this.myGrid = new TilePane();
        this.myRoot = new BorderPane();
        this.setAnimation(timestep,Timeline.INDEFINITE);
        this.myPlayButton = new Button();
        this.myNextButton = new Button();
        this.myStopButton = new Button();
        this.mySaveButton = new Button();
        this.mySlider = new Slider(MINTIMESTEP,MAXTIMESTEP,MINTIMESTEP);
        this.makeSimulationControls();
        start(new Stage());
    }


    private void setAnimation(double timestep, int cyclecount){
        this.myAnimation = createTimeline(timestep, cyclecount);
    }

    private void start(Stage primaryStage){
        primaryStage.setTitle(TITLE);
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
        boolean pointsdown = true;
        for (int i = 0; i < mySimulation.getSimulationCols(); i++) {
            for (int j = 0; j < mySimulation.getSimulationCols(); j++) {
                double tileSize = (VIEWING_WINDOW_SIZE / mySimulation.getSimulationCols());
                if (NUMSIDES == 3) {
                    Triangle triangle = new Triangle(pointsdown, i, j, tileSize);
                    Polygon shape = triangle.getPolygon();
                    shape.setFill(mySimulation.getGridColor(i,j));
                    myGrid.getChildren().add(shape);
                    pointsdown = !pointsdown;
                }
                else if(NUMSIDES == 4){
                    Rectangle rect = new Rectangle(tileSize, tileSize, mySimulation.getGridColor(i, j));
                    rect.getStyleClass().add(RECTANGLESTYLE);
                    myGrid.getChildren().add(rect);
                }
            }
        }
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
        myStopButton.setText(myResources.getString(STOP));
        myStopButton.setOnAction(e -> myAnimation.pause());
        myNextButton.setText(myResources.getString(NEXT));
        myNextButton.setOnAction(e -> {
            myAnimation.setCycleCount(1); // means to create a new timeline with timestep 1 and cyclecount 1
            myAnimation.play();                           // so that the next button makes grid only update once
        });
        myStopButton.setAlignment(Pos.CENTER);
        myPlayButton.setAlignment(Pos.CENTER);
        mySaveButton.setText(myResources.getString("save"));
        mySaveButton.setOnAction(e -> mySimulation.saveCurrentState());
        return getHBox();
    }

    private HBox getHBox() {
        HBox controls = new HBox();
        controls.getChildren().add(myPlayButton);
        controls.getChildren().add(myStopButton);
        controls.getChildren().add(myNextButton);
        controls.getChildren().add(mySaveButton);
        controls.getChildren().add(viewGraphText);
        controls.getChildren().add(viewGraph);
        controls.getChildren().add(makeSlider());
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(MARGIN);
        return controls;
    }

    private Node makeSlider(){
        this.mySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = DIVISONFACTOR/(double)newValue;
            myAnimation = createTimeline(value,Timeline.INDEFINITE);
            myAnimation.play();
        });
        return this.mySlider;
    }

    private Timeline createTimeline(double timestep, int cyclecount){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(timestep), actionEvent -> {
            this.mySimulation.updateGrid();
            this.myRoot.setCenter(buildGrid());
        }));
        timeline.setCycleCount(cyclecount);
        return timeline;
    }

    private void setGraphButton(){
        viewGraph = new CheckBox();
        viewGraph.setOnMousePressed(e->{
                myChart = new Chart();
        });
    }
}
