package cellsociety;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static javafx.application.Application.launch;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class UI extends Application {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 1000;
    private static Group group;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();
        Scene scene = new Scene(group ,WIDTH, HEIGHT);
        scene.setFill(Color.WHITE);

        //Setting the title to Stage.
        primaryStage.setTitle("Sample Application");

        //Adding the scene to Stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }


    private static void addNodeToGroup(Node node){
        group.getChildren().add(node);
    }
    private static void addCollecitontoGroup(Collection<Node> nodes){
        group.getChildren().addAll(nodes);
    }
    private static void removeNodeFromGroup(Node node){
        group.getChildren().remove(node);
    }
    private static void removeCollectionFromGroup(Collection<Node> nodes){
        group.getChildren().removeAll(nodes);
    }


    private static List<String> readText(String fname)  throws FileNotFoundException{
        List<String> ret = new ArrayList<>();
        Scanner s = new Scanner(new File(fname));
        while (s.hasNext()){
            ret.add(s.next());
        }
        return ret;
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