package Checkers.Model;

public enum Piece {
    BLACK (-1),
    WHITE (1),
    QBLACK (0),
    QWHITE (0),
    EMPTY (0),
    SUGG (0)
    ;

    private int direction;

    Piece(int i) {
        this.direction = i;
    }

    public int getDirection() {
        return direction;
    }
}
