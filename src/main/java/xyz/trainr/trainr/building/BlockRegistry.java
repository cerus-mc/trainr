package xyz.trainr.trainr.building;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registers all placed blocks to provide a simple overview for the BlockRemovalTask
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockRegistry {

    // Define private variables
    private final Map<PlayerBlock, Long> blocks;

    /**
     * Creates a new block registry
     */
    public BlockRegistry() {
        this.blocks = new ConcurrentHashMap<>();
    }

    /**
     * Registers a new block
     *
     * @param block The block object
     */
    public void registerBlock(PlayerBlock block) {
        blocks.put(block, System.currentTimeMillis());
    }

    /**
     * Unregisters a block
     *
     * @param block The block object
     */
    public void unregisterBlock(PlayerBlock block) {
        blocks.remove(block);
    }

    /**
     * Unregisters all blocks of a player
     *
     * @param player The player
     */
    public void unregisterAll(Player player) {
        new HashSet<>(blocks.keySet()).stream()
                .filter(playerBlock -> playerBlock.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .forEach(playerBlock -> {
                    playerBlock.getBlock().setType(Material.AIR);
                    blocks.remove(playerBlock);
                });
    }

    /**
     * Unregisters all blocks
     */
    public void unregisterAll() {
        blocks.keySet().forEach(playerBlock -> playerBlock.getBlock().setType(Material.AIR));
        blocks.clear();
    }

    /**
     * @return An unmodifiable copy of the current block map
     */
    public Map<PlayerBlock, Long> getAllBlocks() {
        return Collections.unmodifiableMap(blocks);
    }

}
