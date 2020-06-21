package xyz.trainr.trainr.stats;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.trainr.trainr.islands.SpawnLocationController;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;
import xyz.trainr.trainr.util.StringFormatUtil;

/**
 * Registers some event listeners to provide the stats functionality
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class StatsHooks implements Listener {

    // Define variables
    private final SpawnLocationController spawnLocationController;
    private final UserProvider userProvider;
    private final Timer timer;

    /**
     * Creates a new stats hooks object
     *
     * @param spawnLocationController The spawn location controller to use
     * @param userProvider
     * @param timer
     */
    public StatsHooks(SpawnLocationController spawnLocationController, UserProvider userProvider, Timer timer) {
        this.spawnLocationController = spawnLocationController;
        this.userProvider = userProvider;
        this.timer = timer;
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

        long tryDuration = timer.stopTimer(player);
        userProvider.getCachedUser(player.getUniqueId()).ifPresent(user -> {
            UserStats stats = user.getStats();
            stats.setSucceededTries(stats.getSucceededTries() + 1);

            if (tryDuration != -1 && (tryDuration < stats.getBestTime() || stats.getBestTime() == -1)) {
                long previousBest = stats.getBestTime();
                stats.setBestTime(tryDuration);

                player.sendMessage("§8»");
                player.sendMessage("§8»   §a§lYou have a new personal best time!");
                player.sendMessage("§8»   §7Previous best time: §8" + StringFormatUtil.formatMillis(previousBest));
                player.sendMessage("§8»   §7New best time:        §6" + StringFormatUtil.formatMillis(tryDuration));
                player.sendMessage("§8»");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }

            userProvider.updateUser(user);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        timer.stopTimer(event.getPlayer());
    }

}
