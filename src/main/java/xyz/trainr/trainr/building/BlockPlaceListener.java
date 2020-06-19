package xyz.trainr.trainr.building;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlaceListener implements Listener {

    private final JavaPlugin plugin;
    private final BlockRegistry blockRegistry;

    public BlockPlaceListener(JavaPlugin plugin, BlockRegistry blockRegistry) {
        this.plugin = plugin;
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        blockRegistry.registerBlock(event.getBlock().getLocation());
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                player.getInventory().addItem(new ItemStack(Material.SANDSTONE)), 1);
    }

}
