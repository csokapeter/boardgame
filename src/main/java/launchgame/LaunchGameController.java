package launchgame;

import boardgame.BoardGameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.tinylog.Logger;
import topten.MatchDao;

import java.io.IOException;

public class LaunchGameController {

    @FXML
    private TextField firstUsernameTextfield;

    @FXML
    private TextField secondUsernameTextfield;

    @FXML
    private Label errorLabel;

    public LaunchGameController(){
        Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
        jdbi.installPlugin(new SqlObjectPlugin());
        try (Handle handle = jdbi.open()) {
            MatchDao dao = handle.attach(MatchDao.class);
            dao.createTable();
        }
    }

    public void startAction(javafx.event.ActionEvent actionEvent) throws IOException{
        if(firstUsernameTextfield.getText().isEmpty() || secondUsernameTextfield.getText().isEmpty()){
            errorLabel.setText("Username cannot be empty!");
            errorLabel.setAlignment(Pos.CENTER);
        } else if(firstUsernameTextfield.getText().equals("draw") || secondUsernameTextfield.getText().equals("draw")){
            errorLabel.setText("Username cannot be 'draw'!");
            errorLabel.setAlignment(Pos.CENTER);
        } else if(firstUsernameTextfield.getText().equals(secondUsernameTextfield.getText())){
            errorLabel.setText("Usernames cannot be the same!");
            errorLabel.setAlignment(Pos.CENTER);
        } else if(firstUsernameTextfield.getText().contains(" ") || secondUsernameTextfield.getText().contains(" ")) {
            errorLabel.setText("Usernames cannot contain spaces!");
            errorLabel.setAlignment(Pos.CENTER);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<BoardGameController>getController().initUsernames(firstUsernameTextfield.getText(), secondUsernameTextfield.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Board game");
            stage.setScene(new Scene(root));
            stage.show();
            Logger.debug("Usernames are set to {} and {}, loading the game.", firstUsernameTextfield.getText(), secondUsernameTextfield.getText());
        }
    }

    public void RulesAction(ActionEvent actionEvent) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        Logger.debug("Showing rules page.");
        about.setTitle("Rules");
        about.setHeaderText("Board game rules");
        about.setContentText("""
                This is a two player board game.
                The goal of the first player is to connect the left and right
                side of the board with a red row.
                The goal of the second player is to connect the top and bottom
                of the board with a blue column.
                The players take turns coloring a white square to their color.
                The player who first connects the correct sides of the board wins.
                """);
        about.showAndWait();
    }

    public void LeaderboardAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/topten.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Leaderboard");
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Loading the leaderboard");
    }

    public void QuitAction(ActionEvent actionEvent) {
        Logger.debug("Quitting the game.");
        Platform.exit();
        System.exit(0);
    }
}
