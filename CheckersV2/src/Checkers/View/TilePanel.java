package Checkers.View;

import Checkers.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TilePanel extends JPanel {
    private Controller controller;
    private int line;
    private int col;
    private Color color;
    private ImageIcon piece;

    public TilePanel(Controller cont, int line, int col, Color color) {
        super();
        this.controller = cont;
        this.col = col;
        this.line = line;
        this.piece = null;
        this.color = color;
        this.setBackground(color);
        this.setLayout(new BorderLayout());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    controller.cancel();
                }
                else if(SwingUtilities.isLeftMouseButton(e)) {
                    TilePanel tp = (TilePanel) e.getSource();
                    controller.play(tp.getLine() - 1,tp.getCol() - 1);
                }
            }
        });
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public void setSelected() {this.setBackground(Color.YELLOW);}

    public void resetBackground() {
        this.setBackground(color);
    }

    public void updateTilePiece(int i) {
        this.removeAll();
        this.piece = new ImageIcon("src/Checkers/Img/Piece"+i+".gif");
        JLabel label = new JLabel(piece);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label,BorderLayout.CENTER);
        revalidate();
    }
}
