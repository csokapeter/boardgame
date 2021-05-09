package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import topten.MatchDao;

public class BoardGameModel {

    public static int BOARD_SIZE = 11;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private String player = "player1";
    public String winner;

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

    public boolean canMove(int i, int j){
        return board[i][j].get() == Square.NONE;
    }

    public void move(int i, int j) {
        if(player.equals("player1")){
            board[i][j].set(Square.PIROS);
            player = "player2";
        }
        else if(player.equals("player2")){
            board[i][j].set(Square.KEK);
            player = "player1";
        }
    }

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

}
