package cellsociety.View;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {

    private Rectangle myShape;
    private int myNumber;
    private boolean clicked;

    /**
     *
     * @param row - the row number of the Sqaure
     * @param col - the col number of the Square
     * @param size - the size of the square
     * @param color - the color of the square
     */

    public Square(int row, int col, double size, Color color){
        myShape = new Rectangle(size,size,color);
        myNumber = (row*10) + col;
        clicked = false;
    }

    /**
     *
     * @return - the rectangle object
     */
    public Rectangle getShape(){
        return myShape;
    }

    /**
     *
     * @return the number assigned to the rectangle which is the row * 10 col
     */
    public int getMyNumber(){
        return myNumber;
    }

    /**
     *
     * @return returns if the square has been clicked or not
     */
    public boolean getClicked(){
        return clicked;
    }

    /**
     * changes a clicked square to an unclicked square
     */
    public void changeClicked(){
        clicked = !clicked;
    }
}
