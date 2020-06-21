package xyz.trainr.trainr.islands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import xyz.trainr.trainr.stats.Timer;

/**
 * Handles the automatic player teleportation task when a player falls down
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class PlayerTeleportationTask implements Runnable {

    // Define variables
    private final SpawnLocationController spawnLocationController;
    private final Timer timer;

    /**
     * Creates a new player teleportation task
     *
     * @param spawnLocationController The spawn location controller to use
     * @param timer
     */
    public PlayerTeleportationTask(SpawnLocationController spawnLocationController, Timer timer) {
        this.spawnLocationController = spawnLocationController;
        this.timer = timer;
    }

    @Override
    public void run() {
        // Respawn all players that are beneath a certain y-level
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .filter(player -> player.getLocation().getBlockY() <= spawnLocationController.getDeathHeight())
                .forEach(player -> {
                    spawnLocationController.respawn(player);
                    if (timer.isTimmerRunning(player)) {
                        timer.stopTimer(player);
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
                    }
                });
    }

}
