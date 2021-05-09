package topten;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.List;

/**
 * Class that gets information from the database.
 */
public class TopTenModel {

    /**
     * Returns a list of the top ten best players and their number of wins
     * based on their number of wins.
     *
     * @return Returns a list of the top ten best players and their number of wins
     * based on their number of wins.
     */
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
