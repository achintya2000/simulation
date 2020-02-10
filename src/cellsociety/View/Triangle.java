package cellsociety.View;

import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Triangle {

    private Polygon myTriangle;
    private int myNumber;


    /**
     *
     *
     ***/

    public Triangle(boolean orientation, double row, double col, double tileSize) {
        myTriangle = new Polygon();
        int r = (int) row;
        int c = (int) col;
        myNumber = r*10 + c;
        if (orientation) {
            myTriangle.getPoints().addAll(row, col, row, col+tileSize, row+tileSize, col+(tileSize/2));
        } else {
            myTriangle.getPoints().addAll(row+tileSize, col, row, col+(tileSize/2), row+tileSize, col+tileSize);
        }
        myTriangle.getTransforms().add(new Rotate(90));
    }

    public Polygon getPolygon() {
        return myTriangle;
    }
    public int getMyNumber(){return myNumber;}
}
