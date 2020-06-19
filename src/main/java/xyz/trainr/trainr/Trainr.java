package xyz.trainr.trainr;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.building.BlockPlaceListener;
import xyz.trainr.trainr.building.BlockRegistry;
import xyz.trainr.trainr.building.BlockRemovalTask;
import xyz.trainr.trainr.database.DatabaseController;
import xyz.trainr.trainr.islands.PlayerJoinListener;
import xyz.trainr.trainr.islands.PlayerLeaveListener;
import xyz.trainr.trainr.islands.PlayerTeleportationTask;
import xyz.trainr.trainr.islands.SpawnLocationController;
import xyz.trainr.trainr.users.User;
import xyz.trainr.trainr.users.UserProvider;

/**
 * Represents the loading class of this plugin
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class Trainr extends JavaPlugin {

    // Define local variables
    private DatabaseController databaseController;
    private UserProvider userProvider;
    private SpawnLocationController spawnLocationController;

    @Override
    public void onEnable() {
        // Save the default configuration file
        saveDefaultConfig();
        Configuration config = getConfig();

        // Connect to the configured MongoDB instance
        databaseController = new DatabaseController(
                config.getString("mongodb.host"),
                config.getInt("mongodb.port"),
                config.getString("mongodb.username"),
                config.getString("mongodb.password"),
                config.getString("mongodb.authDB"),
                config.getString("mongodb.dataDB")
        );
        databaseController.openConnection();

        // Initialize the block registry and schedule the block removal task
        BlockRegistry blockRegistry = new BlockRegistry();
        getServer().getScheduler().runTaskTimer(this, new BlockRemovalTask(blockRegistry), 0L, config.getLong("blockRemoval.interval"));

        // Initialize the user provider
        userProvider = new UserProvider(databaseController.getDatabase().getCollection("users", User.class));

        // Initialize the spawn location controller and start teleportation task
        spawnLocationController = new SpawnLocationController(this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerTeleportationTask(spawnLocationController),
                0L, config.getLong("playerTeleportation.interval"));

        // Register listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(spawnLocationController), this);
        pluginManager.registerEvents(new PlayerLeaveListener(spawnLocationController), this);
        pluginManager.registerEvents(new BlockPlaceListener(blockRegistry), this);
    }

    @Override
    public void onDisable() {
        // Close the current MongoDB connection
        databaseController.closeConnection();
    }

    /**
     * @return The user provider
     */
    public UserProvider getUserProvider() {
        return userProvider;
    }

}
