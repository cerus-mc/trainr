package xyz.trainr.trainr;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.database.DatabaseController;

/**
 * Represents the loading class of this plugin
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class Trainr extends JavaPlugin {

    // Define local variables
    private DatabaseController databaseController;

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
    }

    @Override
    public void onDisable() {
        // Close the current MongoDB connection
        databaseController.closeConnection();
    }

}
