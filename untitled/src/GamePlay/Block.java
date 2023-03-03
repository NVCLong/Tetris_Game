package GamePlay;

import java.awt.*;

public class Block {
    private  int[][] shape;
    private Color color;
    int x, y;
    private int [][][] shapes;
    int currentRotate;
    public Block(int [][] shape,Color cl){
        color= cl;
        this.shape= shape;
        shapes= new int[4][][];
        initShapes();
        currentRotate=0;
    }
    public void initShapes(){
        for (int i = 0; i < 4; i++) {
            int r= shape[0].length;
            int c= shape.length;
            shapes[i]= new int[r][c];
            for (int j = 0; j < r; j++) {
                for (int k = 0; k < c; k++) {
                    shapes[i][j][k]=shape[c-k-1][j];
                }
            }
            shape=shapes[i];
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void spawn(int u){
        currentRotate=0;
        shape=shapes[currentRotate];
        y= 0-getHeight();
        x=(u-getWidth())/2;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
    public int getHeight(){
        return shape.length;
    }
    public int getWidth(){
        return shape[0].length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int moveleft(){
        return  x--;
    }
    public int moveright(){
        return  x++;
    }
    public int movedown(){
        return  y++;
    }
    public int bottomEdge(){return y+getHeight();}
    public  int getleftedge(){return x;}
    public int getRightEdge(){return x+getWidth();}

    public void rotateBlock(){
        currentRotate++;
        if(currentRotate>3) currentRotate=0;
        shape= shapes[currentRotate];

    }
}

