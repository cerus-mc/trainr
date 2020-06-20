package xyz.trainr.trainr.users;

import org.bson.types.ObjectId;

import java.util.UUID;

/**
 * Represents an user
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class User {

    // Define local variables
    private ObjectId id;
    private UUID uuid;
    private UserSettings settings;
    private UserStats stats;

    /**
     * Creates a new user object with default values
     *
     * @param uuid The UUID of the user
     */
    public User(UUID uuid) {
        this.uuid = uuid;
        this.settings = new UserSettings();
        this.stats = new UserStats();
    }

    /**
     * Creates a new user object with all values
     *
     * @param id       The ID of the MongoDB object
     * @param uuid     The UUID of the user
     * @param settings The settings of the user
     * @param stats    The stats of the user
     */
    public User(ObjectId id, UUID uuid, UserSettings settings, UserStats stats) {
        this.id = id;
        this.uuid = uuid;
        this.settings = settings;
        this.stats = stats;
    }

    /**
     * @return The ID of the MongoDB object
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Sets the ID of the MongoDB object
     *
     * @param id The ID of the MongoDB object
     */
    void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * @return The UUID of the user
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of the user
     *
     * @param uuid The UUID of the user
     */
    void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return The settings of the user
     */
    public UserSettings getSettings() {
        return settings;
    }

    /**
     * Sets the settings of the user
     *
     * @param settings The settings of the user
     */
    void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    /**
     * @return The stats of the user
     */
    public UserStats getStats() {
        return stats;
    }

    /**
     * Sets the stats of the user
     *
     * @param stats The stats of the user
     */
    void setStats(UserStats stats) {
        this.stats = stats;
    }

}
