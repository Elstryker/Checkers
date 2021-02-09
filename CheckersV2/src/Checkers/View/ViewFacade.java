package Checkers.View;

import Checkers.Controller.Controller;
import Checkers.Model.PlayerTurn;

public interface ViewFacade {
    void update(int[][] board, int turn);
    void resetBackground(int line,int col);
    void setSelected(int line, int col);
    void updateErrorMessage(String message);
    void gameOver(int winner);
}
