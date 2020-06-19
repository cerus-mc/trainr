package xyz.trainr.trainr.stats;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.trainr.trainr.islands.SpawnLocationController;

public class StatsHooks implements Listener {

    private final SpawnLocationController spawnLocationController;

    public StatsHooks(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        if (clickedBlock.getType() != Material.STONE_PLATE) {
            return;
        }

        Player player = event.getPlayer();
        spawnLocationController.respawn(player);
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);

        //TODO: Save time
    }

}
