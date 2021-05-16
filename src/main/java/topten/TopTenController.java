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
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
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

    @FXML
    public void initialize(){
        List<String> currentTopten = getTopTen();

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

    public void quit(ActionEvent actionEvent) {
        Logger.debug("Quitting the game.");
        Platform.exit();
        System.exit(0);
    }

    public void newGame(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LaunchGame.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Loading the launcher.");
    }

    public void setWinner(String winnerUsername){
        if(winnerUsername.equals("draw")) {
            winnerLabel.setText("The game is a draw.");
        } else {
            winnerLabel.setText("The winner is: " + winnerUsername);
        }
    }

    public List<String> getTopTen() {
        Jdbi jdbi = Jdbi.create("jdbc:h2:./database");
        jdbi.installPlugin(new SqlObjectPlugin());
        try (Handle handle = jdbi.open()) {
            MatchDao dao = handle.attach(MatchDao.class);
            List<String> topten = dao.listMatch(10);
            return topten;
        }
    }
}
