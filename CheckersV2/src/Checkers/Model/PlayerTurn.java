package Checkers.Model;

public enum PlayerTurn {
    WHITEPLAYER (0),
    BLACKPLAYER (1);

    private int turn;

    PlayerTurn(int i) {
        this.turn = i;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
