package Checkers.Model;

import Checkers.Exceptions.InputException;
import Checkers.Util.Reference;
import Checkers.Util.Tuple;

public interface ModelFacade {
    Piece[][] getBoard();
    void newGame();
    Tuple<Piece[][], Boolean> canPlay(int l, int c, Reference<Boolean> eat) throws InputException;
    void setBoard(int [][] b);
    void changeTurn();
    Tuple<Integer, Integer> play(int l1, int l2, int c1, int c2) throws InputException;
    int getTurn();
    void calculateEatPieces();
    void updateQueens();
    int isGameOver() throws InputException;
}
