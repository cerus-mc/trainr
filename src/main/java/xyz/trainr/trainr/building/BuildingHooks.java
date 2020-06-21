package xyz.trainr.trainr.building;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.Trainr;
import xyz.trainr.trainr.stats.Timer;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;

/**
 * Registers some event listeners to control the building structure
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BuildingHooks implements Listener {

    // Define local variables
    private final Trainr plugin;
    private final BlockRegistry blockRegistry;
    private final UserProvider userProvider;
    private final Timer timer;

    /**
     * Creates a new building hooks object
     *
     * @param blockRegistry The block registry to use
     * @param userProvider
     * @param timer
     */
    public BuildingHooks(BlockRegistry blockRegistry, UserProvider userProvider, Timer timer) {
        this.timer = timer;
        this.plugin = JavaPlugin.getPlugin(Trainr.class);
        this.blockRegistry = blockRegistry;
        this.userProvider = userProvider;
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        // Check if the player has the survival game mode
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        // Register the placed block and give him his item back
        final Material[] material = {Material.SANDSTONE};
        userProvider.getCachedUser(player.getUniqueId()).ifPresent(user ->
                material[0] = user.getSettings().getBlockType());

        blockRegistry.registerBlock(new PlayerBlock(event.getBlock(), player));
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                player.getInventory().addItem(new ItemStack(material[0])), 1);

        // Update stats
        userProvider.getCachedUser(player.getUniqueId()).ifPresent(user -> {
            UserStats stats = user.getStats();
            stats.setBlocksPlaced(stats.getBlocksPlaced() + 1);
            userProvider.updateUser(user);
        });

        // Start timer if no timer is running
        if (!timer.isTimmerRunning(player)) {
            timer.startTimer(player);

            userProvider.getCachedUser(player.getUniqueId()).ifPresent(user -> {
                UserStats stats = user.getStats();
                stats.setTotalTries(stats.getTotalTries() + 1);
                userProvider.updateUser(user);
            });
        }
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        // Cancel the event if the player is in survival mode
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        // Check if the player is not in survival mode
        if (!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            return;
        }

        // Cancel the event if the type is PHYSICAL
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() != Material.STONE_PLATE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerDamage(EntityDamageEvent event) {
        // Cancel the event if the entity is a player
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
