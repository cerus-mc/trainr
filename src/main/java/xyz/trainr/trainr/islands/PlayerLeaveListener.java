package xyz.trainr.trainr.islands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final SpawnLocationController spawnLocationController;

    public PlayerLeaveListener(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        spawnLocationController.handleLeave(event.getPlayer());
    }

}
