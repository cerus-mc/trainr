package xyz.trainr.trainr.building;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Represents a block placed by a player
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class PlayerBlock {

    // Define local variables
    private Block block;
    private Player player;

    /**
     * Creates a new player block
     *
     * @param block  The block that was placed
     * @param player The player who placed the block
     */
    public PlayerBlock(Block block, Player player) {
        this.block = block;
        this.player = player;
    }

    /**
     * @return The block that was placed
     */
    public Block getBlock() {
        return block;
    }

    /**
     * @return The player who placed the block
     */
    public Player getPlayer() {
        return player;
    }

}
