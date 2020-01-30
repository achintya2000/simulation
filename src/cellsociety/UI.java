package cellsociety;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;


/**
 * Feel free to completely change this code or delete it entirely.
 */
public class UI extends Application {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 1000;
    private static final int MARGIN = 20;
    private static final int SIMULATIONWINDOWSIZE = 500;
    private static Group group;
    private static Rectangle SimulationWINDOW = new Rectangle(MARGIN * 2, 200 +MARGIN, SIMULATIONWINDOWSIZE, SIMULATIONWINDOWSIZE);
    private static Rectangle mySimulationsMenuBar = new Rectangle(WIDTH - 200,0,200 , HEIGHT);
    private static Rectangle myOpenMenuButton;
    private static List<Node> mySimulationsMenu;



/**
 * Feel free to completely change this code or delete it entirely. 
 */


    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        group = new Group();
        Scene scene = new Scene(group ,WIDTH, HEIGHT);
        scene.setFill(Color.WHITE);
        SimulationWINDOW.setFill(Color.BLACK);
        scene.setOnMouseClicked(e -> {
            try {
                handleMouseInput(e.getX(), e.getY());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        //Setting the title to Stage.
        primaryStage.setTitle("Simulation");

        //Adding the scene to Stage
        primaryStage.setScene(scene);
        addNodeToGroup(SimulationWINDOW);
        displayGrid();
        showOpenMenuButton();
        setSimulationsMenu();

        //Displaying the contents of the stage
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

    private static void setSimulationsMenu() throws FileNotFoundException {
        mySimulationsMenu = new ArrayList<>();
        mySimulationsMenuBar.setFill(Color.LIGHTGRAY);
        mySimulationsMenu.add(mySimulationsMenuBar);
        List<String> simulationsText = readText("Resources/SimulationMenuText.txt");
        for(int i = 0; i < simulationsText.size(); i ++){
            Text text = new Text(simulationsText.get(i));
            text.setX(mySimulationsMenuBar.getBoundsInParent().getCenterX() - mySimulationsMenuBar.getBoundsInParent().getWidth()/2 +MARGIN);
            text.setY(MARGIN + i * HEIGHT/8);
            mySimulationsMenu.add(text);
        }
    }

    private static void showOpenMenuButton(){
        myOpenMenuButton = new Rectangle(WIDTH- MARGIN - 50, MARGIN, 50, 50);
        myOpenMenuButton.setFill(new ImagePattern(new Image("open-menu.gif")));
        addNodeToGroup(myOpenMenuButton);
    }

    private static void removeOpenMenuButton(){
        removeNodeFromGroup(myOpenMenuButton);
        myOpenMenuButton = new Rectangle();
    }

    
    private static void displayGrid(){
        ArrayGrid grid = new ArrayGrid(10);
        int cellSize = SIMULATIONWINDOWSIZE/(ArrayGrid.myArray.length);
        for(int r = 0; r < ArrayGrid.myArray.length; r ++){
            for(int c= 0; c < ArrayGrid.myArray[0].length; c ++){
                Rectangle cell = new Rectangle( SimulationWINDOW.getX()+ cellSize*c, SimulationWINDOW.getY()+ cellSize*r, cellSize, cellSize);
                int num = ArrayGrid.myArray[r][c];
                cell.setFill(getColor(num));
                addNodeToGroup(cell);
            }
        }
    }

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

    private static void handleMouseInput(double x, double y) throws FileNotFoundException {
        if(myOpenMenuButton.contains(x,y) && ! group.getChildren().contains(mySimulationsMenu)){
            addCollecitontoGroup(mySimulationsMenu);
            removeOpenMenuButton();

        }
        if(!mySimulationsMenuBar.contains(x,y)){
            removeCollectionFromGroup(mySimulationsMenu);
            showOpenMenuButton();
        }
    }

    private static void addNodeToGroup(Node node){
        if(!group.getChildren().contains(node)){
            group.getChildren().add(node);
        }
    }
    private static void addCollecitontoGroup(Collection<Node> nodes){
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
}






