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
    private int moves = 0;
    private int maxMove = BOARD_SIZE*BOARD_SIZE-60;

    public BoardGameModel() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
                if(i % 2 == 1 && j % 2 == 0){
                    board[i][j].set(Square.KEK);
                }else if(i % 2 == 0 && j % 2 == 1){
                    board[i][j].set(Square.PIROS);
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

    public void move(int i, int j) {
        if(board[i][j].get() == Square.NONE && player == "player1"){
            board[i][j].set(Square.PIROS);
            moves++;
            player = "player2";
        }
        else if(board[i][j].get() == Square.NONE && player == "player2"){
            board[i][j].set(Square.KEK);
            moves++;
            player = "player1";
        }
        /*
        board[i][j].set(
                switch (board[i][j].get()) {
                    case NONE -> Square.PIROS;
                    case PIROS -> Square.KEK;
                    case KEK -> Square.NONE;
                }
        );*/
    }

    public boolean isFinished(int i, int j, String name1, String name2){
        if(moves == maxMove){
            System.out.println("This game is a draw.");
            Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
            jdbi.installPlugin(new SqlObjectPlugin());
            try (Handle handle = jdbi.open()) {
                MatchDao dao = handle.attach(MatchDao.class);
                dao.createTable();
                dao.insertGames(name1, name2, "draw");
            }
            return true;
            //TODO: A DÖNTETLENT MEGCSINÁLNI, hogy hamarabb is detektálja, hogy döntetlen.
        }
        else if(player == "player2"){
            boolean won = true;
            for(int k = 0; k < BOARD_SIZE; k++){
                if(board[i][k].get() != Square.PIROS){
                    won = false;
                }
            }
            if(won) {
                System.out.println(name1 + " won!");
                Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
                jdbi.installPlugin(new SqlObjectPlugin());
                try (Handle handle = jdbi.open()) {
                    MatchDao dao = handle.attach(MatchDao.class);
                    dao.createTable();
                    dao.insertGames(name1, name2, name1);
                }
                return true;
            }
        }else if(player == "player1"){
            boolean won = true;
            for(int k = 0; k < BOARD_SIZE; k++){
                if(board[k][j].get() != Square.KEK) {
                    won = false;
                }
            }
            if(won) {
                System.out.println(name2 + " won!");
                Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
                jdbi.installPlugin(new SqlObjectPlugin());
                try (Handle handle = jdbi.open()) {
                    MatchDao dao = handle.attach(MatchDao.class);
                    dao.createTable();
                    dao.insertGames(name1, name2, name2);
                }
                return true;
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
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
