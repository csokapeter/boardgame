package topten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing a game between two players.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Match {

    private Long id;

    /**
     * The username of the first player.
     */
    private String player1;

    /**
     * The username of the second player.
     */
    private String player2;

    /**
     * The username of the winner.
     */
    private String winner;
}
