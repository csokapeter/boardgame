package launchgame;

import boardgame.BoardGameApplication;
import boardgame.BoardGameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class LaunchGameController {

    @FXML
    private TextField firstUsernameTextfield;

    @FXML
    private TextField secondUsernameTextfield;

    @FXML
    private Label errorLabel;

    public void startAction(javafx.event.ActionEvent actionEvent) throws IOException{
        if(firstUsernameTextfield.getText().isEmpty() || secondUsernameTextfield.getText().isEmpty()){
            errorLabel.setText("Username can not be empty!");
        }
        else if(firstUsernameTextfield.getText().equalsIgnoreCase("draw") || secondUsernameTextfield.getText().equalsIgnoreCase("draw")){
            errorLabel.setText("Username cannot be 'draw'!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<BoardGameController>getController().initUsernames(firstUsernameTextfield.getText(), secondUsernameTextfield.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Board game");
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Usernames are set to {} and {}, loading the game.", firstUsernameTextfield.getText(), secondUsernameTextfield.getText());
        }
    }

    public void AboutAction(ActionEvent actionEvent) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        Logger.info("Showing about page.");
        about.setTitle("About");
        about.setHeaderText("Board game");
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
        Logger.info("Loading the leaderboard");
    }

    public void QuitAction(ActionEvent actionEvent) {
        Logger.info("Quitting the game.");
        Platform.exit();
        System.exit(0);
    }
}
