package topten;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopTenController {

    @FXML
    private TableView<Map> toptenTable;

    @FXML
    private TableColumn<Map, String> player;

    @FXML
    private TableColumn<Map, String> wins;

    @FXML
    private Label winnerLabel;

    private TopTenModel model = new TopTenModel();

    public void quit(ActionEvent actionEvent) {
        Logger.error("Quitting the game.");
        Platform.exit();
        System.exit(0);
    }

    public void newGame(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LaunchGame.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Laoding new game.");
    }

    @FXML
    public void initialize(){
        List<String> currentTopten = model.getTopTen();

        player.setCellValueFactory(new MapValueFactory<>("player"));
        wins.setCellValueFactory(new MapValueFactory<>("wins"));

        ObservableList<Map<String, String>> items = FXCollections.<Map<String, String>>observableArrayList();

        for(int i = 0; i < currentTopten.size(); ++i){
            String[] values = currentTopten.get(i).split(" ");
            Map<String, String> item = new HashMap<>();
            item.put("player", values[0]);
            item.put("wins", values[1]);
            items.add(item);
        }

        toptenTable.getItems().addAll(items);
    }

    public void setWinner(String winnerUsername){
        if(winnerUsername.equals("draw")) {
            winnerLabel.setText("The game is a draw.");
        } else {
            winnerLabel.setText("The winner is: " + winnerUsername);
        }
    }
}
