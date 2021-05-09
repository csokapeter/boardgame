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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopTenController {

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private TableView<Map> toptenTable;

    @FXML
    private TableColumn<Map, String> player;

    @FXML
    private TableColumn<Map, String> wins;

    private TopTenModel model = new TopTenModel();

    public void quit(ActionEvent actionEvent) {
        logger.info("Quitting the game.");
        Platform.exit();
        System.exit(0);
    }

    public void newGame(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LaunchGame.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        logger.info("Loading new game.");
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
}
