package GamePlay;

import javax.swing.*;

public class Gameframe extends JFrame {
    GamePanek gp;
    public Gameframe(){
        GamePanek gp=new GamePanek();
        add(gp);
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);

    }



}
