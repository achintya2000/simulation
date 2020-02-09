package cellsociety.View;

import cellsociety.Controller.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Animation {
    private ViewingWindow myWindow;
    private Timeline myTimeline;
    private int timestep;

    public Animation(double milliseconds, int cycleCount, ViewingWindow window){
        myWindow = window;
        myTimeline = new javafx.animation.Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            window.updateView();
        }));
        myTimeline.setCycleCount(cycleCount);
    }

   public void play(){
        myTimeline.play();
   }

   public void stop(){
        myTimeline.stop();
   }



}

