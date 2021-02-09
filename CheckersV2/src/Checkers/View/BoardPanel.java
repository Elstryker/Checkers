package Checkers.View;

import Checkers.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private Controller controller;
    private TilePanel[][] tiles;

    public BoardPanel(Controller controller) {
        super();
        this.controller = controller;
        setPreferredSize(new Dimension(700,700));
        setLayout(new GridLayout(9,9));
        tiles = new TilePanel[9][9];
        TilePanel temp;
        JLabel label;
        int i,j;
        for(i = 0; i < 9; i++) {
            temp = new TilePanel(controller,0,i,Color.lightGray);
            if(i > 0) {
                label = new JLabel("" + i);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                temp.add(label,BorderLayout.CENTER);
            }
            tiles[0][i] = temp;
            this.add(temp);
        }
        for(i = 1; i < 9; i++) {
            for(j = 0; j < 9; j++) {
                if(j == 0) {
                    temp = new TilePanel(controller,i,j,Color.lightGray);
                    label = new JLabel(i + "");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setVerticalAlignment(JLabel.CENTER);
                    temp.add(label,BorderLayout.CENTER);
                }
                else {
                    temp = new TilePanel(controller,i,j,(i+j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                }
                tiles[i][j] = temp;
                this.add(temp);
            }
        }
    }

    public void updateBoard(int [][] board) {
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                this.tiles[i+1][j+1].updateTilePiece(board[i][j]);
    }

    public void resetBackground(int line, int col) {
        this.tiles[line + 1][col + 1].resetBackground();
    }

    public void setSelected(int line, int col) {
        this.tiles[line + 1][col + 1].setSelected();
    }
}