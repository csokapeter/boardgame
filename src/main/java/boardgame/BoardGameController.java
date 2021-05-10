package boardgame;

import javafx.beans.binding.ObjectBinding;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BoardGameController {
    public static final Logger logger = LogManager.getLogger();
    private String userName1;
    private String userName2;

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
        logger.info("Click on square ({},{})", row, col);
        model.move(row, col);
        if(model.isFinished()){
            switch (model.winner) {
                case "draw" -> {
                    logger.info("The game is a draw.");
                    model.writeDatabase(this.userName1, this.userName2, "draw");
                }
                case "player1" -> {
                    logger.info("The winner is {}.", this.userName1);
                    model.writeDatabase(this.userName1, this.userName2, this.userName1);
                }
                case "player2" -> {
                    logger.info("The winner is {}.", this.userName2);
                    model.writeDatabase(this.userName1, this.userName2, this.userName2);
                }
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/topten.fxml"));
                Stage stage = (Stage) usernameLabel1.getScene().getWindow();
                stage.setTitle("Results");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }


    public void initUsernames(String name1, String name2){
        this.userName1 = name1;
        this.userName2 = name2;
        usernameLabel1.setText("1. player (red): " + this.userName1);
        usernameLabel2.setText("2. player (blue): " + this.userName2);
    }
}
