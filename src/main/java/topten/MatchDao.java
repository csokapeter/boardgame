package topten;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Match.class)
public interface MatchDao {

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

    @SqlUpdate("INSERT INTO games (player1, player2, winner) VALUES (:player1, :player2, :winner)")
    @GetGeneratedKeys
    long insertGames(@Bind("player1") String player1, @Bind("player2") String player2, @Bind("winner") String winner);

    @SqlQuery("SELECT winner || ' ' || count(*) FROM games GROUP BY winner HAVING winner != 'draw' ORDER BY count(*) DESC LIMIT :n")
    List<String> listMatch(@Bind("n") int n);
}
