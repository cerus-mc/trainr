package xyz.trainr.trainr.users;

import org.bukkit.Material;
import org.bukkit.WeatherType;

/**
 * Represents the settings of an user
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserSettings {

    // Define local variables
    private int blockLifetime;
    private Material blockType;
    private boolean ranked;
    private WeatherType weatherType;
    private int time;

    /**
     * Creates a new user settings object with default values
     */
    public UserSettings() {
        this.blockLifetime = 100;
        this.blockType = Material.SANDSTONE;
        this.ranked = true;
        this.weatherType = WeatherType.CLEAR;
        this.time = 1000;
    }

    /**
     * Creates a new user settings object with all values
     *
     * @param blockLifetime The amount of seconds until a block is going into the 'warning' state
     * @param blockType     The material type of the building block
     * @param ranked        Whether or not the user plays ranked
     * @param weatherType   The type of the players weather
     * @param time          The time of the player
     */
    UserSettings(int blockLifetime, Material blockType, boolean ranked, WeatherType weatherType, int time) {
        this.blockLifetime = blockLifetime;
        this.blockType = blockType;
        this.ranked = ranked;
        this.weatherType = weatherType;
        this.time = time;
    }

    /**
     * @return The amount of seconds until a block is going into the 'warning' state
     */
    public int getBlockLifetime() {
        return blockLifetime;
    }

    /**
     * Sets the amount of seconds until a block is going into the 'warning' state
     *
     * @param blockLifetime The amount of seconds until a block is going into the 'warning' state
     */
    public void setBlockLifetime(int blockLifetime) {
        this.blockLifetime = blockLifetime;
    }

    /**
     * @return The material type of the building block
     */
    public Material getBlockType() {
        return blockType;
    }

    /**
     * Sets the material type of the building block
     *
     * @param blockType The material type of the building block
     */
    public void setBlockType(Material blockType) {
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
     *
     * @param ranked Whether or not the user plays ranked
     */
    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    /**
     * @return The type of the players weather
     */
    public WeatherType getWeatherType() {
        return weatherType;
    }

    /**
     * Sets the type of the players weather
     *
     * @param weatherType The type of the players weather
     */
    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    /**
     * @return The time of the player
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the time of the player
     *
     * @param time The time of the player
     */
    public void setTime(int time) {
        this.time = time;
    }

}
