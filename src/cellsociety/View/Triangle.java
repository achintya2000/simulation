package cellsociety.View;

import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Triangle {

    private Polygon myTriangle;
    private int myNumber;


    /**
     *
     * @param orientation - flipped or unflipped
     * @param row - the row number
     * @param col - the column number
     * @param tileSize - the size of the tile
     */
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

    /**
     *
     * @return triangle object
     */
    public Polygon getPolygon() {
        return myTriangle;
    }

    /**
     *
     * @return number asssigned to triangle (row * 10 + ol)
     */
    public int getMyNumber(){return myNumber;}
}
