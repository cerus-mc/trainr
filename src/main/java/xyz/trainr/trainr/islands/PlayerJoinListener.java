package xyz.trainr.trainr.islands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SpawnLocationController spawnLocationController;

    public PlayerJoinListener(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        spawnLocationController.handleJoin(event.getPlayer());
    }

}
