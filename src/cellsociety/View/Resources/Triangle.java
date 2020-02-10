package cellsociety.View.Resources;

import javafx.scene.shape.Polygon;

public class Triangle {

    private Polygon myTriangle;

    public Triangle(String orientation, double row, double col, int tileSize) {
        myTriangle = new Polygon();
        if (orientation == "pointDown") {
            myTriangle.getPoints().addAll(row, col, row, col+tileSize, row+tileSize, col+(tileSize/2));
        } else {
            myTriangle.getPoints().addAll(row+tileSize, col, row, col+(tileSize/2), row+tileSize, col+tileSize);
        }
    }

    public Polygon getPolygon() {
        return myTriangle;
    }
}
