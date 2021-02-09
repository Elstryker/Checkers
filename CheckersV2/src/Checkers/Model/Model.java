package Checkers.Model;

import Checkers.Exceptions.InputException;
import Checkers.Util.Reference;
import Checkers.Util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Model implements ModelFacade {
    private Piece[][] board;
    private PlayerTurn turn;
    private List<Tuple<Integer,Integer>> moveablePieces;
    private int nPlaysLeft;
    private final int MAXPLAYS = 30;
    private Tuple<Integer,Integer> lastQueenPosition;

    public Model() {
        board = new Piece[8][8];
        for(int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = Piece.EMPTY;
        turn = PlayerTurn.BLACKPLAYER;
        moveablePieces = new ArrayList<>();
        nPlaysLeft = 0;
        lastQueenPosition = null;
    }

    public void newGame() {
        turn = PlayerTurn.BLACKPLAYER;
        int i,j = 1;
        for(i = 0; i < 8; i++) {
            if(i < 3) {
                while(j < 8) {
                    board[i][j] = Piece.WHITE;
                    j += 2;
                }
                if(j == 9) j = 0;
                else j = 1;
            }
            else if(i > 4) {
                while(j < 8) {
                    board[i][j] = Piece.BLACK;
                    j += 2;
                }
                if(j == 9) j = 0;
                else j = 1;
            }
        }
    }

    public Piece[][] getBoard() {
        Piece[][] res = new Piece[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(board[i], 0, res[i], 0, 8);
        return res;
    }

    public Tuple<Piece[][], Boolean> canPlay(int l, int c, Reference<Boolean> eat) throws InputException {
        if(l > 7 || l < 0 || c > 8 || c < 0)
            throw new InputException("Out of Bounds!");
        if (this.board[l][c] == Piece.EMPTY || !matchTurn(l,c))
            throw new InputException("Invalid Position!");
        Tuple<Piece[][],Boolean> res;
        Piece current = board[l][c];
        if(current == Piece.QWHITE || current == Piece.QBLACK)
            res = canPlayQueen(l,c,eat);
        else res = canPlayNotQueen(l,c,eat);
        return res;

    }

    private Tuple<Piece[][],Boolean> canPlayNotQueen(int l, int c, Reference<Boolean> eat) {
        Piece[][] res = getBoard();
        Piece current = board[l][c];
        boolean eaten = false;
        int direction = current.getDirection();
        Piece nextPosition;
        boolean canPlay = false;
        if (c < 7) {
            // can eat to the right
            try {
                nextPosition = board[l + direction][c + 1];
                if (nextPosition == Piece.EMPTY) {
                    res[l + direction][c + 1] = Piece.SUGG;
                    canPlay = true;
                }
                else if (c < 6 && isEnemy(current, nextPosition))
                    if (board[l + (2 * direction)][c + 2] == Piece.EMPTY) {
                        canPlay = true;
                        res[l + (2 * direction)][c + 2] = Piece.SUGG;
                        eaten = true;
                    }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        if (c > 0) {
            // can eat to the left
            try {
                nextPosition = board[l + direction][c - 1];
                if (nextPosition == Piece.EMPTY && !eaten) {
                    res[l + direction][c - 1] = Piece.SUGG;
                    canPlay = true;
                }
                else if (isEnemy(current, nextPosition))
                    if (board[l + (2 * direction)][c - 2] == Piece.EMPTY) {
                        canPlay = true;
                        res[l + (2 * direction)][c - 2] = Piece.SUGG;
                        eaten = true;
                        if (res[l + direction][c + 1] == Piece.SUGG)
                            res[l + direction][c + 1] = Piece.EMPTY;
                    }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        eat.setValue(eaten);
        return new Tuple<>(res,canPlay);
    }

    private Tuple<Piece[][], Boolean> canPlayQueen(int l, int c, Reference<Boolean> eat) {
        Piece currentPiece = board[l][c];
        Piece[][] res = getBoard();
        List<Tuple<Integer,Integer>> eatPlaces = new ArrayList<>();
        List<Tuple<Integer,Integer>> moveablePlaces = new ArrayList<>();
        boolean pastEnemy;
        int notPermittedlvet, notPermittedcvet;
        eat.setValue(false);
        if(lastQueenPosition == null) {
            notPermittedcvet = notPermittedlvet = 0;
        } else {
            notPermittedlvet = lastQueenPosition.getFirst() < l ? -1 : 1;
            notPermittedcvet = lastQueenPosition.getSecond() < c ? -1 : 1;
        }
        int currentl, currentc;
        for(int lvet = -1; lvet <= 1; lvet += 2)
            for (int cvet = -1; cvet <= 1; cvet += 2) {
                if(notPermittedcvet == 0 || (lvet != notPermittedlvet || cvet != notPermittedcvet)) {
                    pastEnemy = false;
                    currentl = l + lvet;
                    currentc = c + cvet;
                    while (currentc >= 0 && currentc < 8 && currentl >= 0 && currentl < 8) {
                        if (isEnemy(currentPiece, board[currentl][currentc]) && !pastEnemy)
                            pastEnemy = true;
                        else if (board[currentl][currentc] == Piece.EMPTY) {
                            if (pastEnemy)
                                eatPlaces.add(new Tuple<>(currentl, currentc));
                            else moveablePlaces.add(new Tuple<>(currentl, currentc));
                        } else break;
                        currentc += cvet;
                        currentl += lvet;
                    }
                }
            }
        if(eatPlaces.size() > 0) {
            moveablePlaces = eatPlaces;
            eat.setValue(true);
        }
        for(Tuple<Integer,Integer> place : moveablePlaces)
            res[place.getFirst()][place.getSecond()] = Piece.SUGG;
        return new Tuple<>(res,moveablePlaces.size() > 0);
    }

    private boolean isEnemy(Piece first, Piece second) {
        boolean res = false;
        if((first == Piece.WHITE || first == Piece.QWHITE) && (second == Piece.BLACK || second == Piece.QBLACK))
            res = true;
        else if((first == Piece.BLACK || first == Piece.QBLACK) && (second == Piece.WHITE || second == Piece.QWHITE))
            res = true;
        return res;
    }


    private boolean matchTurn(int line, int column) {
        boolean res = false;
        if (turn == PlayerTurn.WHITEPLAYER && (board[line][column] == Piece.WHITE || board[line][column] == Piece.QWHITE))
            res = true;
        else if(turn == PlayerTurn.BLACKPLAYER && (board[line][column] == Piece.BLACK || board[line][column] == Piece.QBLACK))
            res = true;
        return res;
    }

    @Override
    public void setBoard(int[][] b) {
        for (int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                switch (b[i][j]) {
                    case 1 -> board[i][j] = Piece.WHITE;
                    case 2 -> board[i][j] = Piece.BLACK;
                    case 3 -> board[i][j] = Piece.QWHITE;
                    case 4 -> board[i][j] = Piece.QBLACK;
                    default -> board[i][j] = Piece.EMPTY;
                }
    }

    @Override
    public void changeTurn() {
        if(this.turn == PlayerTurn.WHITEPLAYER)
            turn = PlayerTurn.BLACKPLAYER;
        else turn = PlayerTurn.WHITEPLAYER;
    }

    @Override
    public Tuple<Integer, Integer> play(int l1, int c1, int l2, int c2) throws InputException {
        Tuple<Integer,Integer> eatinPiece = null;
        Reference<Boolean> eat = new Reference<>(false);
        Piece[][] sugg = canPlay(l1,c1,eat).getFirst();
        boolean firsteat;
        if(sugg[l2][c2] != Piece.SUGG)
            throw new InputException("Jogada Impossivel!");
        if(moveablePieces.size() > 0) {
            nPlaysLeft = 0;
            if(!moveablePieces.contains(new Tuple<>(l1,c1)))
                throw new InputException("É obrigatório comer!");
        }
        else nPlaysLeft++;
        board[l2][c2] = board[l1][c1];
        int lvet = l2 < l1 ? -1 : 1;
        int cvet = c2 < c1 ? -1 : 1;
        for(int l = l1,c = c1; l != l2; l += lvet, c += cvet) {
            this.board[l][c] = Piece.EMPTY;
        }
        Piece current = board[l2][c2];
        firsteat = eat.getValue();
        if(firsteat && (current == Piece.QBLACK || current == Piece.QWHITE)) {
            lastQueenPosition = new Tuple<>(l1,c1);
        }
        else lastQueenPosition = null;
        canPlay(l2,c2,eat);
        if(!(eat.getValue() && firsteat)) {
            changeTurn();
            lastQueenPosition = null;
        }
        else
            eatinPiece = new Tuple<>(l2,c2);
        return eatinPiece;
    }

    public int getTurn() {
        return turn.getTurn();
    }

    public void calculateEatPieces() {
        moveablePieces.clear();
        Reference<Boolean> eat = new Reference<>(false);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                eat.setValue(false);
                if(matchTurn(i,j) && board[i][j] != Piece.EMPTY)
                    try {
                        canPlay(i, j, eat);
                        if(eat.getValue())
                            moveablePieces.add(new Tuple<>(i,j));
                    } catch (Exception ignored) {}
            }
    }

    public void updateQueens() {
        for(int i = 0; i < 8; i++) {
            if(board[0][i] == Piece.BLACK)
                board[0][i] = Piece.QBLACK;
        }
        for(int i = 0; i < 8; i++) {
            if(board[7][i] == Piece.WHITE)
                board[7][i] = Piece.QWHITE;
        }
    }

    // Returns 0 - Not Over / 1 - Black Wins / 2 - White Wins / 3 - Draw
    public int isGameOver() throws InputException {
        int winner;
        Piece current;
        boolean isPlayable = false;
        int bcounter = 0, wcounter = 0;
        for(int i = 0; i < 8 && !isPlayable; i++) {
            for (int j = 0; j < 8 && !isPlayable; j++) {
                current = board[i][j];
                if (current == Piece.WHITE || current == Piece.QWHITE)
                    wcounter++;
                else if (current == Piece.BLACK || current == Piece.QBLACK)
                    bcounter++;
                if(matchTurn(i,j))
                    if(canPlay(i,j,new Reference<>(false)).getSecond())
                        isPlayable = true;
            }
        }
        if(nPlaysLeft == MAXPLAYS)
            winner = 3;
        else if(!isPlayable) {
            if(wcounter == 0)
                winner = 1;
            else if(bcounter == 0)
                winner = 2;
            else if (turn == PlayerTurn.WHITEPLAYER)
                winner = 1;
            else winner = 2;
        }
        else winner = 0;
        return winner;
    }
}
