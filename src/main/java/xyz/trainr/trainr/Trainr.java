package xyz.trainr.trainr;

import org.bukkit.configuration.Configuration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.building.BlockRegistry;
import xyz.trainr.trainr.building.BlockRemovalTask;
import xyz.trainr.trainr.building.BuildingHooks;
import xyz.trainr.trainr.database.DatabaseController;
import xyz.trainr.trainr.islands.IslandsHooks;
import xyz.trainr.trainr.islands.PlayerTeleportationTask;
import xyz.trainr.trainr.islands.SpawnLocationController;
import xyz.trainr.trainr.stats.*;
import xyz.trainr.trainr.users.User;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.worldgen.TrainrChunkGenerator;

/**
 * Represents the loading class of this plugin
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class Trainr extends JavaPlugin {

    // Define the database controller
    private DatabaseController databaseController;

    @Override
    public void onEnable() {
        // Save the default configuration file
        saveDefaultConfig();

        // Initialize the database system
        initializeDatabaseSystem();

        // Initialize the user system
        UserProvider userProvider = initializeUserSystem();

        // Initialize a new timer
        Timer timer = new Timer();

        // Initialize new spawn location controller
        SpawnLocationController spawnLocationController = new SpawnLocationController(userProvider);

        // Initialize the building system
        initializeBuildingSystem(userProvider, timer, spawnLocationController);

        // Initialize new scoreboard controller
        ScoreboardController scoreboardController = new ScoreboardController(userProvider);

        // Initialize the island system
        initializeIslandSystem(userProvider, spawnLocationController, scoreboardController, timer);

        // Initialize the stats system
        initializeStatsSystem(spawnLocationController, scoreboardController, userProvider, timer);
    }

    @Override
    public void onDisable() {
        // Close the current MongoDB connection
        databaseController.closeConnection();
    }

    /**
     * Initializes the MongoDB connection
     */
    private void initializeDatabaseSystem() {
        // Connect to the configured MongoDB instance
        Configuration config = getConfig();
        databaseController = new DatabaseController(
                config.getString("mongodb.connectionURI"),
                config.getString("mongodb.database")
        );
        databaseController.openConnection();
    }

    /**
     * Initializes the user system
     *
     * @return The user provider
     */
    private UserProvider initializeUserSystem() {
        UserProvider userProvider = new UserProvider(databaseController.getDatabase().getCollection("players", User.class));
        userProvider.loadAllUsers();
        return userProvider;
    }

    /**
     * Initializes the building system
     */
    private void initializeBuildingSystem(UserProvider userProvider, Timer timer, SpawnLocationController spawnLocationController) {
        // Initialize the block registry and schedule the block removal task
        BlockRegistry blockRegistry = new BlockRegistry();
        getServer().getScheduler().runTaskTimer(this, new BlockRemovalTask(blockRegistry, userProvider), 0L, getConfig().getLong("blockRemoval.interval"));

        // Register the building hooks
        getServer().getPluginManager().registerEvents(new BuildingHooks(blockRegistry, userProvider, spawnLocationController, timer), this);
    }

    /**
     * Initializes the island system
     */
    private void initializeIslandSystem(UserProvider userProvider, SpawnLocationController spawnLocationController, ScoreboardController scoreboardController, Timer timer) {
        // Start the teleportation task
        getServer().getScheduler().runTaskTimer(this, new PlayerTeleportationTask(spawnLocationController, timer),
                0L, getConfig().getLong("playerTeleportation.interval"));

        // Register the island hooks
        getServer().getPluginManager().registerEvents(new IslandsHooks(userProvider, spawnLocationController, scoreboardController), this);

        // Register all online players
        getServer().getOnlinePlayers().forEach(spawnLocationController::handleJoin);
    }

    /**
     * Initializes the stats system
     */
    private void initializeStatsSystem(SpawnLocationController spawnLocationController, ScoreboardController scoreboardController, UserProvider userProvider, Timer timer) {
        // Start the scoreboard update task
        getServer().getScheduler().runTaskTimer(this, new ScoreboardUpdateTask(scoreboardController), 0L,
                getConfig().getLong("scoreboard.updateInterval"));
        getServer().getScheduler().runTaskTimerAsynchronously(this, new TimeDisplayTask(timer), 0L, 2);

        getServer().getPluginManager().registerEvents(new StatsHooks(spawnLocationController, userProvider, timer), this);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if (worldName.equals("world")) {
            return new TrainrChunkGenerator();
        }
        return super.getDefaultWorldGenerator(worldName, id);
    }

}
