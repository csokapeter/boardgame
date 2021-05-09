package topten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing a match between two players.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Match {
    private Long id;
    private String player1;
    private String player2;
    private String winner;
}
