package cellsociety.View;

import cellsociety.Controller.Simulation;
import cellsociety.Model.Grid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chart {
  final int WINDOW_SIZE = 10;
  final int WIDTH = 500;
  final int HEIGHT = 500;
  private XYChart.Series<String, Number> series1;
  private XYChart.Series<String, Number> series2;
  ScheduledExecutorService scheduledExecutorService;
  private BorderPane myRoot;
  private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English");
  private static final String RESOURCES = "cellsociety/View/Resources/";
  // use Java's dot notation, like with import, for properties
  private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES;
  private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
  private static final String STYLESHEET = "styles.css";

  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

  public Chart() {
    myRoot = new BorderPane();
    myRoot.setCenter(initializeChart());
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      start(new Stage());
  }

  public void updateChart() {
    // put data onto graph per second
    scheduledExecutorService.scheduleAtFixedRate(() -> {

      // Update the chart
      Platform.runLater(() -> {
        // get current time
        Date now = new Date();
        // put random number with current time
        Integer random = ThreadLocalRandom.current().nextInt(10);
        Integer random2 = ThreadLocalRandom.current().nextInt(10);

        series1.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random));
        series2.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random2));

        if (series1.getData().size() > WINDOW_SIZE) {
          series1.getData().remove(0);
        }
        if (series2.getData().size() > WINDOW_SIZE) {
          series2.getData().remove(0);
        }
      });
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void start(Stage primaryStage){
    primaryStage.setTitle("Graph");
    primaryStage.setScene(buildScene());
    primaryStage.show();
  }

  private Scene buildScene(){
    Scene scene = new Scene(myRoot ,WIDTH, HEIGHT);
    scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    scene.setFill(Color.WHITE);
    return scene;
  }

  private LineChart<String, Number> initializeChart() {
    final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
    final NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Time/s");
    xAxis.setAnimated(false); // axis animations are removed
    yAxis.setLabel("Value");
    yAxis.setAnimated(false); // axis animations are removed

    //creating the line chart with two axis created above
    final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Population");
    lineChart.setAnimated(false); // disable animations

    //defining a series to display data
    series1 = new XYChart.Series<>();
    series1.setName("Data Series 1");
    series2 = new XYChart.Series<>();
    series2.setName("Data Series 2");
    return lineChart;
  }

}
