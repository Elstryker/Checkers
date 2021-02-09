package Checkers.View;

import Checkers.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private BoardPanel board;
    private Controller controller;
    private JPanel turn;
    private JLabel turnLabel;
    private JLabel errorMessageLabel;

    public GameFrame(Controller controller) {
        super();
        this.controller = controller;

        board = new BoardPanel(controller);
        turn = new JPanel();
        turn.setLayout(new BorderLayout());
        turnLabel = new JLabel("Turn: ");
        turnLabel.setVerticalAlignment(JLabel.CENTER);
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        turn.add(turnLabel,BorderLayout.CENTER);
        errorMessageLabel = new JLabel("Errors will display here");
        turn.add(errorMessageLabel,BorderLayout.EAST);

        setTitle("Checkers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        add(board,BorderLayout.CENTER);
        this.add(turn,BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    public void updateBoard(int[][] board, int turn) {
        this.board.updateBoard(board);
        String turnName = turn == 0 ? "Red" : "Blue";
        turnLabel.setText("Turn: " + turnName);
    }

    public void resetBackground(int line, int col) {
        this.board.resetBackground(line,col);
    }

    public void setSelected(int line, int col) {
        this.board.setSelected(line,col);
    }

    public void udpateErrorMessage(String message) {
        errorMessageLabel.setText("Error: "+message);
    }

    public void gameOver(int winner) {
        String wins = "";
        String[] options = {"Exit","New Game"};
        if(winner == 1)
            wins = "Blue";
        else if(winner == 2)
            wins = "Red";
        else if(winner == 3)
            wins = "No one, it's a draw!";
        controller.gameOverHandler(JOptionPane.showOptionDialog(this,"Game Over! Winner is: " + wins,"Game Over!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,0));
    }
}
