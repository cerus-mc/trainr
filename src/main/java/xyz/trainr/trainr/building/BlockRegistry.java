package xyz.trainr.trainr.building;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Registers all placed blocks to provide a simple overview for the BlockRemovalTask
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockRegistry {

    // Define private variables
    private Map<Location, Long> blocks;

    /**
     * Creates a new block registry
     */
    public BlockRegistry() {
        this.blocks = new ConcurrentHashMap<>();
    }

    /**
     * Registers a new block
     * @param location the location of the Block
     */
    public void registerBlock(Location location) {
        blocks.put(location, System.currentTimeMillis());
    }

    /**
     * Unregisters a block
     * @param location The location of the Block
     */
    public void unregisterBlock(Location location) {
        blocks.remove(location);
    }

    /**
     * @return An unmodifiable copy of the current block map
     */
    public Map<Location, Long> getAllBlocks() {
        return Collections.unmodifiableMap(blocks);
    }

}
