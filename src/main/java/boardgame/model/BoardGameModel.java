package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import topten.MatchDao;

/**
 * Class representing the state of the game.
 */
public class BoardGameModel {

    /**
     * The number of rows and columns the board has.
     */
    public static int BOARD_SIZE = 11;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private String player = "player1";

    /**
     * Contains the outcome of the match.
     */
    public String winner;

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
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    /**
     * Sets the state of the square with the coordinates ({@code i}{@code j})
     * on the board if its NONE.
     *
     * @param i the row number
     * @param j the column number
     */
    public void move(int i, int j) {
        if(board[i][j].get() == Square.NONE && player.equals("player1")){
            board[i][j].set(Square.PIROS);
            player = "player2";
        }
        else if(board[i][j].get() == Square.NONE && player.equals("player2")){
            board[i][j].set(Square.KEK);
            player = "player1";
        }
    }

    /**
     * Checks if the game has finished, and sets the winner to
     * either draw, player1 or player2.
     *
     * @return whether the game finished.
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
            winner = "draw";
            return true;
        }
        else if(player.equals("player2")){
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
                        winner = "player1";
                        return true;
                    }
                }
            }
        }else if(player.equals("player1")){
            for(int i = 0; i < BOARD_SIZE; i++){
                boolean won = true;
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[j][i].get() != Square.KEK) {
                        won = false;
                        break;
                    }
                }
                if(won){
                    winner = "player2";
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Writes the game details to the database.
     *
     * @param name1 username of player1
     * @param name2 username of player2
     * @param winnerPlayer username of the winner
     */
    public void writeDatabase(String name1, String name2, String winnerPlayer){
        Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
        jdbi.installPlugin(new SqlObjectPlugin());
        try (Handle handle = jdbi.open()) {
            MatchDao dao = handle.attach(MatchDao.class);
            dao.createTable();
            dao.insertGames(name1, name2, winnerPlayer);
        }
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
