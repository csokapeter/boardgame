package boardgame;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import boardgame.model.BoardGameModel;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.tinylog.Logger;
import topten.MatchDao;
import topten.TopTenController;

import java.io.IOException;

public class BoardGameController {
    private ReadOnlyStringWrapper userName1 = new ReadOnlyStringWrapper();
    private ReadOnlyStringWrapper userName2 = new ReadOnlyStringWrapper();
    private ReadOnlyStringWrapper winner = new ReadOnlyStringWrapper();

    @FXML
    private Label usernameLabel1;

    @FXML
    private Label usernameLabel2;

    @FXML
    private GridPane board;

    private BoardGameModel model = new BoardGameModel();

    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }

    public ReadOnlyStringProperty winnerProperty() { return winner.getReadOnlyProperty(); }

    public String getWinner() { return winner.get(); }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Rectangle(60,60);
        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.squareProperty(i, j));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(i, j).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case PIROS -> Color.RED;
                            case KEK -> Color.BLUE;
                        };
                    }
                }
        );
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({},{})", row, col);
        model.move(row, col);
        if(model.isFinished()){
            if (model.getWinner().equals("draw")) {
                Logger.info("The game is a draw.");
                winner.set("draw");
                writeDatabase(userName1.get(), userName2.get(), "draw");
            } else if (model.getWinner().equals("player1")) {
                Logger.info("The winner is {}.", userName1.get());
                winner.set(userName1.get());
                writeDatabase(userName1.get(), userName2.get(), userName1.get());
            } else if (model.getWinner().equals("player2")) {
                Logger.info("The winner is {}.", userName2.get());
                winner.set(userName2.get());
                writeDatabase(userName1.get(), userName2.get(), userName2.get());
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/topten.fxml"));
                Parent root = fxmlLoader.load();
                fxmlLoader.<TopTenController>getController().setWinner(winner.get());
                Stage stage = (Stage) usernameLabel1.getScene().getWindow();
                stage.setTitle("Leaderboard");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
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
            dao.insertGames(name1, name2, winnerPlayer);
        }
    }

    public void initUsernames(String name1, String name2){
        userName1.set(name1);
        userName2.set(name2);
        usernameLabel1.setText("1. player (red): " + userName1.get());
        usernameLabel2.setText("2. player (blue): " + userName2.get());
    }
}
