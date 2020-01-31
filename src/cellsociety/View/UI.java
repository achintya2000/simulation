package cellsociety.View;

import cellsociety.Controller.XMLParser;
import cellsociety.Model.ArrayGrid;
import cellsociety.Controller.Simulation;
import java.sql.Time;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int MARGIN = 20;
    private static final int SIMULATIONWINDOWSIZE = 300;
    private static Group group;
    private static Rectangle SimulationWINDOW = new Rectangle(MARGIN * 2, 200 +MARGIN, SIMULATIONWINDOWSIZE, SIMULATIONWINDOWSIZE);
    private static Rectangle mySimulationsMenuBar = new Rectangle(WIDTH - 200,0,200 , HEIGHT);
    private static Rectangle myOpenMenuButton;
    private static List<Node> mySimulationsMenu;
    private static String gameOfLifeConfiguration = "./Resources/gameoflife.xml";

    private Timeline timeline;
    private Text testing;

    //Simulation simulationManager = new Simulation(gameOfLifeConfiguration);

    public static void main (String[] args) {
        XMLParser gameoflife = new XMLParser("config");
        System.out.println(gameoflife.getInfo(new File(gameOfLifeConfiguration)));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();


        //Setting the title to Stage.
        primaryStage.setTitle("Simulation");

        //addNodeToGroup(SimulationWINDOW);
        //displayGrid();
        //showOpenMenuButton();
        //setSimulationsMenu();

        //Displaying the contents of the stage
        timeline = new Timeline(new KeyFrame(
            Duration.millis(20), event -> {
            testing.setText(String.valueOf(Math.random()));
        }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        //Adding the scene to Stage
        primaryStage.setScene(makeScene());
        primaryStage.show();
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
        BorderPane root = new BorderPane();
        root.setTop(makeSimulationToolbar());
        root.setBottom(makeSimulationControls());
        //root.setCenter(setSimulationsMenu());

        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.setFill(Color.WHITE);
        SimulationWINDOW.setFill(Color.BLACK);
//        scene.setOnMouseClicked(e -> {
//            try {
//                handleMouseInput(e.getX(), e.getY());
//            } catch (FileNotFoundException ex) {
//                ex.printStackTrace();
//            }
//        });
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
        toolbar.getChildren().add(comboBox);
        toolbar.getChildren().add(testing);
        return toolbar;
    }

    private Node makeSimulationControls() {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        Button playButton = new Button();
        playButton.setText("Play");
        playButton.setOnAction(e -> timeline.play());
        Button stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setOnAction(e -> timeline.stop());
        HBox controls = new HBox();
        controls.getChildren().add(playButton);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(slider);
        return controls;
    }


//    private static void setSimulationsMenu() throws FileNotFoundException {
//        mySimulationsMenu = new ArrayList<>();
//        mySimulationsMenuBar.setFill(Color.LIGHTGRAY);
//        mySimulationsMenu.add(mySimulationsMenuBar);
//        List<String> simulationsText = readText("Resources/SimulationMenuText.txt");
//        for(int i = 0; i < simulationsText.size(); i ++){
//            Text text = new Text(simulationsText.get(i));
//            text.setX(mySimulationsMenuBar.getBoundsInParent().getCenterX() - mySimulationsMenuBar.getBoundsInParent().getWidth()/2 +MARGIN);
//            text.setY(MARGIN + i * HEIGHT/8);
//            mySimulationsMenu.add(text);
//        }
//    }

//    private static void showOpenMenuButton(){
//        myOpenMenuButton = new Rectangle(WIDTH- MARGIN - 50, MARGIN, 50, 50);
//        myOpenMenuButton.setFill(new ImagePattern(new Image("open-menu.gif")));
//        addNodeToGroup(myOpenMenuButton);
//    }
//
//    private static void removeOpenMenuButton(){
//        removeNodeFromGroup(myOpenMenuButton);
//        myOpenMenuButton = new Rectangle();
//    }


//    private static void displayGrid(){
//        ArrayGrid grid = new ArrayGrid(10);
//        int cellSize = SIMULATIONWINDOWSIZE/(ArrayGrid.myArray.length);
//        for(int r = 0; r < ArrayGrid.myArray.length; r ++){
//            for(int c= 0; c < ArrayGrid.myArray[0].length; c ++){
//                Rectangle cell = new Rectangle( SimulationWINDOW.getX()+ cellSize*c, SimulationWINDOW.getY()+ cellSize*r, cellSize, cellSize);
//                int num = ArrayGrid.myArray[r][c];
//                cell.setFill(getColor(num));
//                addNodeToGroup(cell);
//            }
//        }
//    }

    private static Color getColor(int i){
        int color = i % 5;
        switch (color){
            case 0:
                return Color.RED;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.PURPLE;
        }
        return Color.INDIGO;
    }

//    private static void handleMouseInput(double x, double y) throws FileNotFoundException {
//        if(myOpenMenuButton.contains(x,y) && ! group.getChildren().contains(mySimulationsMenu)){
//            addCollecitontoGroup(mySimulationsMenu);
//            removeOpenMenuButton();
//
//        }
//        if(!mySimulationsMenuBar.contains(x,y)){
//            removeCollectionFromGroup(mySimulationsMenu);
//            showOpenMenuButton();
//        }
//    }


    private static void addNodeToGroup(Node node){
        if(!group.getChildren().contains(node)){
            group.getChildren().add(node);
        }
    }
    private static void addCollectiontoGroup(Collection<Node> nodes){
        if(!group.getChildren().contains(nodes)){
            group.getChildren().addAll(nodes);
        }
    }
    private static void removeNodeFromGroup(Node node){
        group.getChildren().remove(node);
    }
    private static void removeCollectionFromGroup(Collection<Node> nodes){
        group.getChildren().removeAll(nodes);
    }




    private static void addText() throws FileNotFoundException {
        int i = 0;
        List<String> strings = readText("Resources/Start.txt");
       for(String string : strings ){
           Text text = new Text(string);
           text.setFill(Color.BLACK);
           text.setX(WIDTH/2 - text.getBoundsInLocal().getWidth()/2);
           text.setY(i * HEIGHT/8);
           addNodeToGroup(text);
           i++;
       }
    }
}
