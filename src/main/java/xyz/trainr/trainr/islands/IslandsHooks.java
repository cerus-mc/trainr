package xyz.trainr.trainr.islands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Registers some event listeners to control the island structure
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class IslandsHooks implements Listener {

    // Define the span location controller
    private final SpawnLocationController spawnLocationController;

    /**
     * Creates a new island hooks object
     * @param spawnLocationController The spawn location controller to use
     */
    public IslandsHooks(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        // Replace the join message and assign an island to the joining player
        event.setJoinMessage(null);
        spawnLocationController.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        // Replace the quit message and free the island of the quitting player
        event.setQuitMessage(null);
        spawnLocationController.handleLeave(event.getPlayer());
    }

}
