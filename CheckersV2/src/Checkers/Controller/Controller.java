package Checkers.Controller;

import Checkers.Exceptions.InputException;
import Checkers.Model.Model;
import Checkers.Model.ModelFacade;
import Checkers.Model.Piece;
import Checkers.Util.Reference;
import Checkers.Util.Tuple;
import Checkers.View.ViewFacade;

import java.util.Scanner;

public class Controller {
    private ModelFacade model;
    private ViewFacade view;
    private Reference<Boolean> eat;
    private Tuple<Integer,Integer> lastEatin;
    private Tuple<Integer,Integer> selectedPosition;
    private boolean selectLocked;
    // 1 - White / 2 - Black / 3 - QWhite / 4 - QBlack / 0 - Empty
    int[][] test = {
            {0,0,0,0,0,4,0,0},
            {0,0,0,0,3,0,3,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,3,0,3,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}
    };

    public Controller() {
        this.eat = new Reference<>(false);
        this.lastEatin = null;
        this.selectedPosition = null;
        this.selectLocked = false;
    }

    public void run() {
        model.newGame();
        model.calculateEatPieces();
        int [][] board = convertBoard(model.getBoard());
        view.update(board,model.getTurn());
    }

    public void cancel() {
        if(selectedPosition != null && !selectLocked) {
            view.resetBackground(selectedPosition.getFirst(), selectedPosition.getSecond());
            selectedPosition = null;
        }
    }

    public void play(int line, int col) {
        if(selectedPosition == null) {
            selectedPosition = new Tuple<>(line,col);
            view.setSelected(line,col);
        }
        else {
            int [][] board = convertBoard(model.getBoard());
            Tuple<Integer,Integer> source = selectedPosition;
            selectLocked = false;
            cancel();
            try {
                lastEatin = model.play(source.getFirst(),source.getSecond(),line,col);
                selectLocked = lastEatin != null;
                model.updateQueens();
                board = convertBoard(model.getBoard());
                view.updateErrorMessage("No error!");
            } catch (InputException e) {
                view.updateErrorMessage(e.getMessage());
            }
            model.calculateEatPieces();
            if(lastEatin != null) {
                selectedPosition = lastEatin;
                view.setSelected(selectedPosition.getFirst(), selectedPosition.getSecond());
            }
            view.update(board,model.getTurn());
            try {
                int winner = model.isGameOver();
                if (winner != 0)
                    view.gameOver(winner);
            } catch (InputException ignored) {}
        }
    }

    public void gameOverHandler(int answer) {
        if(answer < 1) {
            System.exit(0);
        }
        else {
            model.newGame();
            model.calculateEatPieces();
            int [][] board = convertBoard(model.getBoard());
            view.update(board,model.getTurn());
        }
    }

    public void runBack() {
        int winner = 0;
        model.newGame();
        //model.setBoard(test);
        //model.changeTurn();
        Scanner sc = new Scanner(System.in);
        int first, second;
        int third, fourth;
        Piece[][] p = model.getBoard();
        //view.showBoard(convertBoard(p));
        while(winner == 0) {
            model.calculateEatPieces();
            System.out.println("Turn: " + model.getTurn());
            if(lastEatin == null) {
                System.out.print("Linha: ");
                first = sc.nextInt() - 1;
                if (first == -1) break;
                System.out.print("Coluna: ");
                second = sc.nextInt() - 1;
            }
            else {
                first = lastEatin.getFirst();
                second = lastEatin.getSecond();
            }
            eat.setValue(false);
            try {
                p = model.canPlay(first, second, eat).getFirst();
                //view.showBoard(convertBoard(p));
                System.out.print("Linha: ");
                third = sc.nextInt() - 1;
                System.out.print("Coluna: ");
                fourth = sc.nextInt() - 1;
                lastEatin = model.play(first,third,second,fourth);
                winner = model.isGameOver();
                p = model.getBoard();
                //view.showBoard(convertBoard(p));
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Winner: " + winner);
    }

    private int[][] convertBoard(Piece[][] p) {
        int[][] res = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (p[i][j]) {
                    case WHITE -> res[i][j] = 1;
                    case BLACK -> res[i][j] = 2;
                    case QWHITE -> res[i][j] = 3;
                    case QBLACK -> res[i][j] = 4;
                    case SUGG -> res[i][j] = 5;
                    case EMPTY -> res[i][j] = 0;
                }
            }
        }
        return res;
    }

    public void setComponents(ViewFacade vw, Model model) {
        this.view = vw;
        this.model = model;
    }
}
