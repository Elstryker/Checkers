package Checkers.View;

import Checkers.Controller.Controller;

import javax.swing.*;

public class GUI implements ViewFacade {
    private Controller controller;
    private GameFrame gameFrame;

    public GUI(Controller cont) {
        this.controller = cont;
        gameFrame = new GameFrame(controller);
    }

    @Override
    public void update(int[][] board, int turn) {
        gameFrame.updateBoard(board,turn);
    }

    @Override
    public void resetBackground(int line, int col) {
        gameFrame.resetBackground(line,col);
    }

    @Override
    public void setSelected(int line, int col) {
        gameFrame.setSelected(line,col);
    }

    public void updateErrorMessage(String message) {
        this.gameFrame.udpateErrorMessage(message);
    }

    public void gameOver(int winner) {
        this.gameFrame.gameOver(winner);
    }

}
