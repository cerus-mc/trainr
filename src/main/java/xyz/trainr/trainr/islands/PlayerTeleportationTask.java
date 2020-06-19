package xyz.trainr.trainr.islands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

/**
 * Handles the automatic player teleportation task when a player falls down
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class PlayerTeleportationTask implements Runnable {

    // Define the spawn location controller
    private final SpawnLocationController spawnLocationController;

    /**
     * Creates a new player teleportation task
     * @param spawnLocationController The spawn location controller to use
     */
    public PlayerTeleportationTask(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @Override
    public void run() {
        // Respawn all players that are beneath a certain y-level
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .filter(player -> player.getLocation().getBlockY() <= spawnLocationController.getDeathHeight())
                .forEach(spawnLocationController::respawn);
    }

}
