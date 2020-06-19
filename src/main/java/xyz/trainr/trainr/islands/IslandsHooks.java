package xyz.trainr.trainr.islands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.trainr.trainr.stats.ScoreboardController;

/**
 * Registers some event listeners to control the island structure
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class IslandsHooks implements Listener {

    // Define the span location controller
    private final SpawnLocationController spawnLocationController;
    private final ScoreboardController scoreboardController;

    /**
     * Creates a new island hooks object
     *
     * @param spawnLocationController The spawn location controller to use
     * @param scoreboardController The scoreboard controller to use
     */
    public IslandsHooks(SpawnLocationController spawnLocationController, ScoreboardController scoreboardController) {
        this.spawnLocationController = spawnLocationController;
        this.scoreboardController = scoreboardController;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        // Replace the join message and assign an island to the joining player
        event.setJoinMessage(null);
        spawnLocationController.handleJoin(event.getPlayer());

        scoreboardController.setScoreboard(event.getPlayer());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        // Replace the quit message and free the island of the quitting player
        event.setQuitMessage(null);
        spawnLocationController.handleLeave(event.getPlayer());
    }

}
