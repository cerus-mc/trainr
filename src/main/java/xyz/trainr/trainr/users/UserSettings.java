package xyz.trainr.trainr.users;

/**
 * Represents the settings of an user
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserSettings {

    // Define local variables
    private int blockLifetime;
    private String blockType;
    private boolean ranked;

    /**
     * Creates a new user settings object with default values
     */
    public UserSettings() {
        this.blockLifetime = 100;
        this.blockType = "SANDSTONE";
        this.ranked = true;
    }

    /**
     * Creates a new user settings object with all values
     * @param blockLifetime The amount of seconds until a block is going into the 'warning' state
     * @param blockType The material type of the building block
     * @param ranked Whether or not the user plays ranked
     */
    UserSettings(int blockLifetime, String blockType, boolean ranked) {
        this.blockLifetime = blockLifetime;
        this.blockType = blockType;
        this.ranked = ranked;
    }

    /**
     * @return The amount of seconds until a block is going into the 'warning' state
     */
    public int getBlockLifetime() {
        return blockLifetime;
    }

    /**
     * Sets the amount of seconds until a block is going into the 'warning' state
     * @param blockLifetime The amount of seconds until a block is going into the 'warning' state
     */
    public void setBlockLifetime(int blockLifetime) {
        this.blockLifetime = blockLifetime;
    }

    /**
     * @return The material type of the building block
     */
    public String getBlockType() {
        return blockType;
    }

    /**
     * Sets the material type of the building block
     * @param blockType The material type of the building block
     */
    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    /**
     * @return Whether or not the user plays ranked
     */
    public boolean isRanked() {
        return ranked;
    }

    /**
     * Sets whether or not the user plays ranked
     * @param ranked Whether or not the user plays ranked
     */
    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

}
