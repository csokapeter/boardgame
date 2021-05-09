package launchgame;

import boardgame.BoardGameApplication;
import boardgame.BoardGameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        }
    }
}
