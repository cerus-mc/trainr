package xyz.trainr.trainr.islands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class PlayerTeleportationTask implements Runnable {

    private final SpawnLocationController spawnLocationController;

    public PlayerTeleportationTask(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .filter(player -> player.getLocation().getBlockY() <= spawnLocationController.getDeathHeight())
                .forEach(spawnLocationController::respawn);
    }

}
