package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.trainr.trainr.users.UserProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of the players scoreboard
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class ScoreboardController {

    private final Map<UUID, PlayerScoreboard> scoreboardMap = new HashMap<>();

    // Define the user provider
    private final UserProvider userProvider;

    /**
     * Creates a new scoreboard controller
     *
     * @param userProvider The user provider to use
     */
    public ScoreboardController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    /**
     * Sets the scoreboard to the given player
     *
     * @param player The player to set the scoreboard to
     */
    public void setScoreboard(Player player) {
        if (!scoreboardMap.containsKey(player.getUniqueId())) {
            scoreboardMap.put(player.getUniqueId(), new PlayerScoreboard(userProvider, player, Bukkit.getScoreboardManager().getNewScoreboard()));
        }

        scoreboardMap.get(player.getUniqueId()).setPlayerScoreboard();
    }

    /**
     * Removes and destroys the scoreboard of the given player
     *
     * @param player The scoreboard owner
     */
    public void removeScoreboard(Player player) {
        if (!scoreboardMap.containsKey(player.getUniqueId())) {
            return;
        }

        scoreboardMap.remove(player.getUniqueId()).destroy();
    }

}
