package xyz.trainr.trainr.islands;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PlayerTeleportationTask implements Runnable {

    private final SpawnLocationController spawnLocationController;

    public PlayerTeleportationTask(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getLocation().getBlockY() <= spawnLocationController.getDeathHeight())
                .forEach(player -> {
                    Location location = spawnLocationController.getIslandLocation(player);
                    if (location == null) {
                        // Wtf
                        player.kickPlayer("Invalid");
                        return;
                    }
                    player.teleport(location);
                });
    }

}
