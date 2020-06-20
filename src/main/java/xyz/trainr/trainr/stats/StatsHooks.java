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

/**
 * Registers some event listeners to provide the stats functionality
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class StatsHooks implements Listener {

    // Define the spawn location controller
    private final SpawnLocationController spawnLocationController;

    /**
     * Creates a new stats hooks object
     *
     * @param spawnLocationController The spawn location controller to use
     */
    public StatsHooks(SpawnLocationController spawnLocationController) {
        this.spawnLocationController = spawnLocationController;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the action is physical
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        // Check if the clicked block is a stone pressure plate
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.STONE_PLATE) {
            return;
        }

        // Respawn the player
        Player player = event.getPlayer();
        spawnLocationController.respawn(player);
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);

        // TODO: Save time
    }

}
