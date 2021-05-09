package topten;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO interface for the {@link Match} entity.
 */
@RegisterBeanMapper(Match.class)
public interface MatchDao {

    /**
     * Creates a table called games if it doesn't already exist in the database.
     * It contains the id of the game, the usernames of the two players, and the winner of the match.
     */
    @SqlUpdate("""
        CREATE TABLE IF NOT EXISTS games (
            id IDENTITY PRIMARY KEY,
            player1 VARCHAR NOT NULL,
            player2 VARCHAR NOT NULL,
            winner VARCHAR NOT NULL
            )
            """
    )
    void createTable();

    /**
     * Writes the result of a match to the database. Returns the auto-generated id for the match.
     *
     * @param player1 the username of the first player
     * @param player2 the username of the second player
     * @param winner the winner of the match
     * @return the id of the game
     */
    @SqlUpdate("INSERT INTO games (player1, player2, winner) VALUES (:player1, :player2, :winner)")
    @GetGeneratedKeys
    long insertGames(@Bind("player1") String player1, @Bind("player2") String player2, @Bind("winner") String winner);

    /**
     * Returns the list of the {@code n} best players and their number of wins
     * based on their number of wins.
     *
     * @param n the number of results to be returned
     * @return the list of the {@code n} best players and their number of wins
     * based on their number of wins.
     */
    @SqlQuery("SELECT winner || ' ' || count(*) FROM games GROUP BY winner HAVING winner != 'draw' ORDER BY count(*) DESC LIMIT :n")
    List<String> listMatch(@Bind("n") int n);
}
