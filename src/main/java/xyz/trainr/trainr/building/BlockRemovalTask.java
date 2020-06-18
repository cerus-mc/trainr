package xyz.trainr.trainr.building;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.Trainr;

/**
 * Handles the repeating automatic block removal task
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockRemovalTask implements Runnable {

    // Define local variables
    private final Trainr plugin;
    private final BlockRegistry blockRegistry;

    /**
     * Creates a new block removal task
     * @param blockRegistry The block registry to use
     */
    public BlockRemovalTask(BlockRegistry blockRegistry) {
        this.plugin = JavaPlugin.getPlugin(Trainr.class);
        this.blockRegistry = blockRegistry;
    }

    @Override
    public void run() {
        // Define useful variables
        Configuration config = plugin.getConfig();
        long currentTimeMillis = System.currentTimeMillis();

        // Run through all registered blocks whose whose offset is big enough
        blockRegistry.getAllBlocks().entrySet().stream()
                .filter(entry -> currentTimeMillis - entry.getValue() >= config.getLong("blockRemoval.warningOffset"))
                .forEach(entry -> {
                    // Define useful variables
                    Location location = entry.getKey();
                    Block block = location.getBlock();

                    // Destroy the block if its offset is big enough or set its material to the configured warning type
                    if (currentTimeMillis - entry.getValue() >= config.getLong("blockRemoval.deletionOffset")) {
                        block.setType(Material.AIR);
                        location.getWorld().playEffect(location, Effect.STEP_SOUND, 1, block.getTypeId());
                        return;
                    }
                    block.setType(Material.valueOf(config.getString("blockRemoval.warningMaterial").toUpperCase()));
                });
    }

}
