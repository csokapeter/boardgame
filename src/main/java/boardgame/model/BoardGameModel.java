package boardgame.model;

import javafx.beans.property.*;

/**
 * Class representing the state of the game.
 */
public class BoardGameModel {

    /**
     * Class representing the players.
     */
    public enum Player{
        PLAYER1, PLAYER2;

        /**
         * Alternates between the two players.
         *
         * @return the next player.
         */
        public Player alter() {
            return switch (this) {
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    /**
     * The {@code Player} that moves next.
     */
    private ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<Player>();

    /**
     * The number of rows and columns the board has.
     */
    public static int BOARD_SIZE = 11;

    /**
     * The array storing the current configuration of the board.
     */
    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    /**
     * Contains the outcome of the game.
     */
    private ReadOnlyStringWrapper winner = new ReadOnlyStringWrapper();

    /**
     * Creates a {@code BoardGameModel} object representing the initial state of the game.
     */
    public BoardGameModel() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
                if(i % 2 == 1 && j % 2 == 0){
                    board[i][j].set(Square.PIROS);
                }else if(i % 2 == 0 && j % 2 == 1){
                    board[i][j].set(Square.KEK);
                }
            }
        }
        nextPlayer.set(Player.PLAYER1);
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * {@return the state of the square of the position specified on the board}
     *
     * @param i the row of a position
     * @param j the column of a position
     */
    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public ReadOnlyStringProperty winnerProperty() { return winner.getReadOnlyProperty(); }

    /**
     * {@return which player won the game}
     */
    public String getWinner() { return winner.get(); }

    /**
     * Sets the state of the square of the position specified
     * on the board if its current state is NONE.
     *
     * @param i the row number
     * @param j the column number
     */
    public void move(int i, int j) {
        if(board[i][j].get() == Square.NONE && nextPlayer.get().equals(Player.PLAYER1)){
            board[i][j].set(Square.PIROS);
            nextPlayer.set(nextPlayer.get().alter());
        }
        else if(board[i][j].get() == Square.NONE && nextPlayer.get().equals(Player.PLAYER2)){
            board[i][j].set(Square.KEK);
            nextPlayer.set(nextPlayer.get().alter());
        }
    }

    /**
     * Checks if the game is over, and sets the winner to
     * either draw, player1 or player2.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean isFinished(){
        int completedRows = 0;
        for(int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[i][j].get() == Square.KEK) {
                    completedRows++;
                    break;
                }
            }
        }
        for(int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[j][i].get() == Square.PIROS) {
                    completedRows++;
                    break;
                }
            }
        }
        if(completedRows == 22){
            winner.set("draw");
            return true;
        }
        else if(nextPlayer.get().equals(Player.PLAYER2)){
            for(int i = 0; i < BOARD_SIZE; i++){
                if(i % 2 == 1) {
                    boolean won = true;
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        if (board[i][j].get() != Square.PIROS) {
                            won = false;
                            break;
                        }
                    }
                    if(won){
                        winner.set("player1");
                        return true;
                    }
                }
            }
        }else if(nextPlayer.get().equals(Player.PLAYER1)){
            for(int i = 0; i < BOARD_SIZE; i++){
                boolean won = true;
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[j][i].get() != Square.KEK) {
                        won = false;
                        break;
                    }
                }
                if(won){
                    winner.set("player2");
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        BoardGameModel model = new BoardGameModel();
        System.out.println(model);
    }

}
