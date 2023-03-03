package GamePlay;

import GamePlay.Block;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import Shape.Jshape;
import Shape.Ishape;
import Shape.Lshape;
import Shape.Tshape;
import Shape.Zshape;

public class GamePanek  extends JPanel implements ActionListener {
    final static  int UNIT_SIZE=25;
    final static  int WIDTH=UNIT_SIZE*12;
    final static  int HEIGHT=UNIT_SIZE*24;
    private int DEALAY=300;
    Timer tm;
    Random rd;
    public int block[][]={{1,0},{1,0},{1,1}};
    Block bl;
    private Point currentPieceOrigin;
    private int currentPieceIndex;
    private int[][] gameBoard;
    private boolean isPaused;
    private boolean isGameOver=false;
    char direction;
    private Color [][] background;
    int score=0;
    public GamePanek(){
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        this.addKeyListener(new mykeyadapter());
        rd= new Random();
        currentPieceOrigin= new Point(WIDTH/2,0);
        gameBoard= new int[WIDTH][HEIGHT];
        background=new Color[HEIGHT/UNIT_SIZE][WIDTH/UNIT_SIZE];
        Color[] Color= new Color[7];

        startGame();

    }
    // Spawn method using to create new block with different shapes
    public void spawnBlock(){
        int x= rd.nextInt(0,5);
        switch (x) {
            case 0:
                bl = new Jshape();
                break;
            case 1:
                bl= new Ishape();
                break;
            case 2:
                bl= new Tshape();
                break;
            case 3:
                bl= new Lshape();
                break;
            case 4:
                bl= new Zshape();
                break;
        }
        bl.spawn(WIDTH/UNIT_SIZE);
    }
    // start game: using to start game and start Timer
    public void startGame(){
        tm= new Timer(DEALAY,this);
        tm.start();
        spawnBlock();
    }
    //paint component using to call other draw methods
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBackGround(g);
        drawPiece(g);
        draw(g);
    }
    // draw method using to draw unit square in Frame, and other things like score
    public void draw(Graphics g){
        for (int i = 0; i < HEIGHT/UNIT_SIZE; i++) {
            g.setColor(Color.WHITE);
            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,HEIGHT);
            g.drawLine(0,i*UNIT_SIZE,WIDTH,i*UNIT_SIZE);
        }
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,20));
        FontMetrics fm= getFontMetrics(g.getFont());
        g.drawString("Score: " +score,(WIDTH-fm.stringWidth("Score: \"+score"))/10,HEIGHT/15);

    }

    // draw piece: using to draw block
    public void drawPiece(Graphics g){
        int x= bl.getWidth();
        int y= bl.getHeight();
        int [][] shape= bl.getShape();
        Color c= bl.getColor();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                if(shape[i][j]==1) {
                    int x0=(bl.getX()+j)*UNIT_SIZE;
                    int y0=(bl.getY()+i)*UNIT_SIZE;
                    drawGrid(g,c,x0,y0);
                }
            }
        }
    }
    // draw the block at the bottom of game area
    public void drawBackGround(Graphics g){
        Color color= bl.getColor();
        for (int i = 0; i < HEIGHT/UNIT_SIZE; i++) {
            for (int j = 0; j < WIDTH/UNIT_SIZE; j++) {
                color= background[i][j];
                if(color!=null){
                    int x= j*UNIT_SIZE;
                    int y= i*UNIT_SIZE;
                    drawGrid(g,color,x,y);
                }
            }
        }
    }
    // draw unit square
    public void drawGrid(Graphics g, Color color,int x, int y){
        g.setColor(color);
        g.fillRect(x,y,UNIT_SIZE,UNIT_SIZE);
        g.setColor(Color.black);
        g.drawRect(x,y,UNIT_SIZE,UNIT_SIZE);
    }
    // Get the position of each block of the shape and store to a new 2D array
    public void movetoBackGround(){
        int shape[][]=bl.getShape();
        int h= bl.getHeight();
        int w=bl.getWidth();
        int xPos= bl.getX();
        int yPos= bl.getY();
        Color color= bl.getColor();
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if(shape[r][c]==1){
                    background[r+yPos][c+xPos]=color;
                }
            }

        }
    }
    // Move: using to control the direction of block

    public void move(){
        switch (direction){
            case 'D':
                moveBlockdown();
                break;
            case 'R':
                moveRight();
                break;
            case 'L':
                moveLeft();
                break;
            case 'U':
                rotateBlock();
                break;
        }

    }
    // move block down and check if it at the bottom
    public void moveBlockdown(){
        stroreBackground();
        bl.movedown();
    }
    //move block right
    public void moveRight(){
        if(checkAtright()==false) return;
        stroreBackground();
        bl.moveright();
    }
    //move block left
    public  void moveLeft(){
        if(checkAtleft()==false) return;
        stroreBackground();
        bl.moveleft();
    }
    // rotate the block to change the shape
    public void rotateBlock(){
        stroreBackground();
        bl.rotateBlock();
    }
    // store the backgound
    public void stroreBackground(){
        if(checkAtBottom()==false){
            movetoBackGround();
            clearLine();
            spawnBlock();
        }

    }
    // clear the full line of block
    public void clearLine(){
        boolean lineFilled;
        int x= HEIGHT/UNIT_SIZE;
        int y= WIDTH/UNIT_SIZE;
        for (int i = x-1; i >=0 ; i--) {
            lineFilled=true;
            for (int j = 0; j < y; j++) {
                if(background[i][j]==null){
                    lineFilled=false;
                    break;
                }
            }
            if(lineFilled==true){
                clear(i);
                shiftDown(i);
                clear(0);
                i++;
                score=(score+10);
            }
        }
    }
    //clear function
    public void clear(int r){
        int y= WIDTH/UNIT_SIZE;
        for (int j = 0; j < y; j++) {
            background[r][j]=null;
        }
    }
    // Shift down the block
    public void shiftDown(int r){
        int y= WIDTH/UNIT_SIZE;
        for (int i = r; i >0 ; i--) {
            for (int j = 0; j <y ; j++) {
                background[i][j]=background[i-1][j];
            }

        }
    }
    // check if the block at the bottom
    public boolean checkAtBottom(){
        if(bl.bottomEdge()==24) {
            return false;
        }
        int [][] shape= bl.getShape();
        int w= bl.getWidth();
        int h= bl.getHeight();
        for (int col = 0; col < w; col++) {
            for (int row = h-1; row >=0 ; row--) {
                if(shape[row][col]!=0){
                    int x=col+bl.getX();
                    int y= row+bl.getY()+1;
                    if(y<0) break;
                    if(background[y][x]!=null){
                        return  false;
                    }
                    break;
                }
            }
        }
        return true;
    }
    // check if the block at the left boundary
    public boolean checkAtleft(){
        if(bl.getleftedge()==0) {
            return false;
        }
        int [][] shape= bl.getShape();
        int w= bl.getWidth();
        int h= bl.getHeight();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                if (shape[row][col] != 0) {
                    int x = col + bl.getX()-1;
                    int y = row + bl.getY();
                    if (y < 0) break;
                    if (background[y][x] != null) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }
    // check block at right boundary
    public  boolean checkAtright(){
        if(bl.getRightEdge()==WIDTH/UNIT_SIZE) return  false;
        int [][] shape= bl.getShape();
        int w= bl.getWidth();
        int h= bl.getHeight();
        for (int row = 0; row < h; row++) {
            for (int col = w-1; col >= 0; col--) {
                if (shape[row][col] != 0) {
                    int x = col + bl.getX()+1;
                    int y = row + bl.getY();
                    if (y < 0) break;
                    if (background[y][x] != null) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }
    // check game if the height of block out of the game area
    public void checkGame(){
        if(bl.getY()<0){
            isGameOver= true;
        }
        isGameOver=false;
    }
    // repaint and run the game
    @Override
    public void actionPerformed(ActionEvent e) {
        if(isGameOver==false) {
            move();
            while (checkAtBottom() == false) {
                if (checkAtBottom() == false) {
                    return;
                }

            }
            repaint();
        }else  System.out.println("Game over");
    }
    // get the event from user keyboard
    public class  mykeyadapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    direction='D';
                    break;
                case KeyEvent.VK_RIGHT:
                    direction='R';
                    break;
                case KeyEvent.VK_LEFT:
                    direction='L';
                    break;
                case KeyEvent.VK_UP:
                    direction='U';
                    break;
            }
        }
    }


}
