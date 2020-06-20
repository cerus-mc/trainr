package xyz.trainr.trainr.building;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.Trainr;
import xyz.trainr.trainr.users.UserProvider;

/**
 * Handles the repeating automatic block removal task
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockRemovalTask implements Runnable {

    // Define local variables
    private final Trainr plugin;
    private final BlockRegistry blockRegistry;
    private final UserProvider userProvider;

    /**
     * Creates a new block removal task
     *
     * @param blockRegistry The block registry to use
     * @param userProvider  The user provider to use
     */
    public BlockRemovalTask(BlockRegistry blockRegistry, UserProvider userProvider) {
        this.plugin = JavaPlugin.getPlugin(Trainr.class);
        this.blockRegistry = blockRegistry;
        this.userProvider = userProvider;
    }

    @Override
    public void run() {
        // Define useful variables
        Configuration config = plugin.getConfig();
        long currentTimeMillis = System.currentTimeMillis();

        // Loop through all registered blocks
        blockRegistry.getAllBlocks().forEach((block, created) ->
            userProvider.getCachedUser(block.getPlayer().getUniqueId()).ifPresent(user -> {
                // Define the craftBlock
                Block craftBlock = block.getBlock();

                // Define the state of the block
                int blockLifetime = user.getSettings().getBlockLifetime() / 20 * 1000;
                if (currentTimeMillis - created >= blockLifetime) {
                    craftBlock.setType(Material.AIR);
                    blockRegistry.unregisterBlock(block);
                } else if (currentTimeMillis - created >= blockLifetime - config.getInt("blockRemoval.warningStateDuration") / 20 * 1000) {
                    craftBlock.setType(Material.valueOf(config.getString("blockRemoval.warningMaterial").toUpperCase()));
                }
            })
        );
    }

}
