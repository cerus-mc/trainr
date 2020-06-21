package xyz.trainr.trainr.building;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.trainr.trainr.Trainr;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.util.PacketUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the repeating automatic block removal task
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockRemovalTask implements Runnable {

    private final Set<Location> blocks = new HashSet<>();

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
                        craftBlock.getWorld().playEffect(craftBlock.getLocation(), Effect.STEP_SOUND, user.getSettings().getBlockType().getId());
                        blocks.remove(craftBlock.getLocation());
                    } else if (currentTimeMillis - created >= blockLifetime - config.getInt("blockRemoval.warningStateDuration") / 20 * 1000
                            && !blocks.contains(craftBlock.getLocation())) {
                        blocks.add(craftBlock.getLocation());
                        startAnimationTask(craftBlock);
                    }
                })
        );
    }

    private void startAnimationTask(Block block) {
        Trainr plugin = JavaPlugin.getPlugin(Trainr.class);
        new BukkitRunnable() {
            private int stage = 0;

            @Override
            public void run() {
                if (stage == 10) {
                    cancel();
                    sendBlockBreakPacket(block, -1);
                    return;
                }

                sendBlockBreakPacket(block, stage++);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 2);
    }

    private void sendBlockBreakPacket(Block block, int stage) {
        try {
            Class<?> blockPosClass = Class.forName("net.minecraft.server.v1_8_R3.BlockPosition");
            Class<?> packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation");
            Object packet = packetClass.getDeclaredConstructor(int.class, blockPosClass, int.class).newInstance(getBlockEntityId(block),
                    new BlockPosition(block.getX(), block.getY(), block.getZ()), stage);

            PacketUtil.broadcastPacket(packet);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int getBlockEntityId(Block block) {
        return ((block.getX() & 0xFFF) << 20)
                | ((block.getZ() & 0xFFF) << 8)
                | (block.getY() & 0xFF);
    }

}
