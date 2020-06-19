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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.Trainr;

/**
 * Registers some event listeners to control the building structure
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BuildingHooks implements Listener {

    // Define local variables
    private final Trainr plugin;
    private final BlockRegistry blockRegistry;

    /**
     * Creates a new building hooks object
     * @param blockRegistry The block registry to use
     */
    public BuildingHooks(BlockRegistry blockRegistry) {
        this.plugin = JavaPlugin.getPlugin(Trainr.class);
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        // Check if the player has the survival game mode
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        // Register the placed block and give him his item back
        blockRegistry.registerBlock(event.getBlock().getLocation());
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                player.getInventory().addItem(new ItemStack(Material.SANDSTONE)), 1);
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

        // Cancel the event if the type is RIGHT_CLICK_BLOCK
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerDamage(EntityDamageEvent event) {
        // Cancel the event if the entity is a player
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
