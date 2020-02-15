package cellsociety.View;

import java.io.IOException;
import java.util.*;
import cellsociety.Controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.text.Text;
import java.io.File;
import java.util.List;

public class UI extends Application {
    private static final int HEIGHT = 500;
    private static final int WIDTH = 400;
    private static final int SPACING = 100;
    private static final int HGAP = 10;
    private static final int rowindex = 1;
    private static final int TOP_INSET =10;
    private static final int BOTTOM_INSET =20;
    private static final int RIGHT_INSET =10;
    private static final int LEFT_INSET =0;
    private static final int LEFTCOLUMN = 0;
    private static final int RIGHTCOLUMN = 1;
    private static final int FONTSIZE = 20;
    private static final int TILESIZE = 50;
    private static final int CENTERTILENUM = 11;

    private static final String RESOURCES = "cellsociety/View/Resources/";
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private static final String DESCRIPTION = "description";
    private static final String STYLESHEET = "styles.css";
    private static final String gameoflife = "GameofLife";
    private static final String GAMEOFLIFE = "Game of Life";
    private static final String FIRE = "Fire";
    private static final String PERCOLATION = "Percolation";
    private static final String WATOR = "Wator";
    private static final String SEGREGATION = "Segregation";
    private static final String RPS = "RPS";
    private static final String TITLE = "title";
    private static final String BADINPUT = "badinput";
    private static final String NOTXML = "notXML";
    private static final String NEWSIM = "newSim";
    private static final String CHOOSESHAPE = "chooseShape";
    private static final String TRIANGLE = "Triangle";
    private static final String SQUARE = "Square";
    private static final String BROWSE = "browse";
    private static final String LEFTPANELSTYLENAME = "leftpanel";
    private static final String COMBOBOXSTYLENAME = "combobox";
    private static final String  ENVIRONMENT = "environment";
    private static final String FINITE = "finite";
    private static final String TOROID = "toroid";
    private static final String SHAPE = "shape";




    private static final Map<String, Simulation> chooseSim = Map.of(GAMEOFLIFE,new GameOfLife(),FIRE, new Fire(), SEGREGATION, new Segregation(), PERCOLATION, new Percolation(), WATOR, new Wator(), RPS, new RPS());
    private static final Map<String, String> chooseSimName = Map.of(GAMEOFLIFE,"gameoflife",FIRE, "fire", SEGREGATION, "segregation", PERCOLATION, "percolation", WATOR, "wator", RPS, "rps");


    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
    private BorderPane root = new BorderPane();
    private Stage PrimaryStage;
    private TilePane myNeighbors;
    private FileChooser fileChooser = new FileChooser();
    private Map<Integer, String> allNeighbors = Map.ofEntries(Map.entry(0, "NW"),Map.entry(1, "N"),Map.entry(2, "NE"),Map.entry(10 , "W"),Map.entry(12, "E"),Map.entry(20, "SW"),Map.entry(21, "S"),Map.entry(22, "SE"), Map.entry( -21, "NWW"),Map.entry(-22, "NEE"),Map.entry(-20, "WW") ,Map.entry(-26, "EE"));
    private List<String> neighborstosend = new ArrayList<>();

    /*
    * Default parameters for new viewingwindow
    * */
    private boolean isRandom = false;
    private int NUMSIDES = 4;
    private String myShapeChosen = "Square";
    private String myNewSimulation = "Fire";
    private String environment = "finite";

    private Text chooseNeighborsText = new Text(myResources.getString("chooseNeighbors"));
    private Text chooseConFigText = new Text(myResources.getString("chooseFile"));
    private Text descriptionText = new Text(myResources.getString(DESCRIPTION));
    private Text chooseShapeText= new Text(myResources.getString(CHOOSESHAPE));
    private Text randomConfigurationText = new Text(myResources.getString("randomConfiguration"));

    /**
     *
     * @throws IOException
     */

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
        primaryStage.setTitle(myResources.getString(TITLE));
        primaryStage.setScene(makeScene());
        primaryStage.show();
    }

    private Scene makeScene() throws IOException {
        root.setTop(setToolBox());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;

    }

    private void loadSimulationChoice(String simulation, File xmlFile) {
        try {
            ViewingWindow window = new ViewingWindow(chooseSim.get(simulation), xmlFile, chooseSimName.get(simulation), isRandom, neighborstosend, environment,NUMSIDES );
        }
        catch(XMLException e){
            setErrorBox(chooseSim.get(simulation).getERROR_MESSAGE());
        }
    }

    private Node setToolBox(){
        VBox topBox = new VBox(SPACING);
        GridPane topPanel = new GridPane();
        topPanel.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
        topPanel.getStyleClass().add(LEFTPANELSTYLENAME);
        descriptionText.setFont(Font.font(FONTSIZE));
        topPanel.add(descriptionText,LEFTCOLUMN,rowindex*0);
        topPanel.add(setComboBox(),LEFTCOLUMN,rowindex);
        topPanel.add(chooseShapeText, LEFTCOLUMN, rowindex *2);
        topPanel.add(setShapeComboBox(),RIGHTCOLUMN,rowindex*2);
        topPanel.add(randomConfigurationText,LEFTCOLUMN,rowindex*3);
        topPanel.add(setramdomConfigBox(),RIGHTCOLUMN,rowindex*3);
        topPanel.add(setToroidComboBox(),LEFTCOLUMN,rowindex*4);
        topPanel.add(chooseConFigText,LEFTCOLUMN,rowindex*5);
        topPanel.add(setBrowseButton(),RIGHTCOLUMN,rowindex*6);
        topPanel.add(chooseNeighborsText, LEFTCOLUMN, rowindex*7);
        topPanel.setPadding(new Insets(TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET));
        topPanel.setHgap(HGAP);
        topBox.getChildren().add(topPanel);
        root.setBottom(setChooseNeighborsTilePane());
        return topBox;
    }

    private Node setBrowseButton(){
        Button browse = new Button();
        browse.setText(myResources.getString(BROWSE));
        browse.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(PrimaryStage);
            if(selectedFile != null) loadSimulationChoice(myNewSimulation, selectedFile);
        });
        return browse;
    }

    private Node setComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add(COMBOBOXSTYLENAME);
        String[] choiceProperties = {NEWSIM, PERCOLATION, gameoflife, WATOR, SEGREGATION, FIRE, RPS};
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


    private void setErrorBox(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(myResources.getString(BADINPUT));
        alert.setHeaderText(myResources.getString(NOTXML));
        alert.setContentText(message);
        alert.showAndWait();
    }


    private CheckBox setramdomConfigBox(){
        CheckBox box = new CheckBox();
        box.setOnMousePressed(e->{
            isRandom = !isRandom;
        });
        return box;
    }

    public Node setChooseNeighborsTilePane(){
        myNeighbors = new TilePane();
        HBox box = new HBox();
   for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                    Square tile = new Square(i,j,TILESIZE,Color.WHITE);
                    if(i == 1 && j == 1){
                        tile.getShape().setFill(Color.RED);
                    }
                    tile.getShape().setOnMousePressed(e-> {
                        int number = tile.getMyNumber();
                        String direction = allNeighbors.get(number);
                        if(tile.getMyNumber() != CENTERTILENUM) {
                            tile.getShape().setFill(Color.BLUE);
                            neighborstosend.add(direction);
                        }
                    });
                    myNeighbors.getChildren().add(tile.getShape());
                }
            }
        myNeighbors.setHgap(HGAP/2);
        myNeighbors.setVgap(5);
        myNeighbors.setAlignment(Pos.CENTER);
        myNeighbors.setPrefColumns(3);
        myNeighbors.setPadding(new Insets(TOP_INSET *  RIGHT_INSET, 75, 20, 75));
        myNeighbors.prefRowsProperty();
        box.getChildren().add(myNeighbors);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Color getColor(boolean clicked){
        if(clicked) return Color.BLUE;
        return Color.WHITE;
    }


    private Node setToroidComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add(COMBOBOXSTYLENAME);
        String[] environProp = {ENVIRONMENT, FINITE, TOROID};
        for(String s : environProp){
            comboBox.getItems().add(myResources.getString(s));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e->{
            environment = comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase();
        });
        return comboBox;
    }
    private Node setShapeComboBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add(COMBOBOXSTYLENAME);
        String[] shapeProperties = {SHAPE, SQUARE, TRIANGLE};
        for (String shape : shapeProperties){
            comboBox.getItems().add(myResources.getString(shape));
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(e-> {
            myShapeChosen = (String) comboBox.getSelectionModel().getSelectedItem();
            switch (myShapeChosen){
                case (SQUARE):
                    NUMSIDES = 4;
                    break;
                case (TRIANGLE):
                    NUMSIDES = 3;
                    break;
                }
            root.setBottom(setChooseNeighborsTilePane());
        });
        return comboBox;
    }

}


