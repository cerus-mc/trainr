package xyz.trainr.trainr.islands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import xyz.trainr.trainr.stats.ScoreboardController;
import xyz.trainr.trainr.users.UserProvider;

/**
 * Registers some event listeners to control the island structure
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class IslandsHooks implements Listener {

    // Define the span location controller
    private final UserProvider userProvider;
    private final SpawnLocationController spawnLocationController;
    private final ScoreboardController scoreboardController;

    /**
     * Creates a new island hooks object
     *
     * @param userProvider            The user provider to use
     * @param spawnLocationController The spawn location controller to use
     * @param scoreboardController    The scoreboard controller to use
     */
    public IslandsHooks(UserProvider userProvider, SpawnLocationController spawnLocationController, ScoreboardController scoreboardController) {
        this.userProvider = userProvider;
        this.spawnLocationController = spawnLocationController;
        this.scoreboardController = scoreboardController;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        // Replace the join message
        event.setJoinMessage(null);

        // Initialize the user object for this player
        userProvider.initializeUser(event.getPlayer().getUniqueId()).whenComplete((ignored, throwable) -> {
            // Check if the operation succeeded
            if (throwable != null) {
                event.getPlayer().kickPlayer("Can't create your user object.");
                throwable.printStackTrace();
                return;
            }

            // Handle the player join
            spawnLocationController.handleJoin(event.getPlayer());
            scoreboardController.setScoreboard(event.getPlayer());
        });
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        // Replace the quit message and free the island of the quitting player
        event.setQuitMessage(null);
        spawnLocationController.handleLeave(event.getPlayer());
    }

    @EventHandler
    public void handleWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
