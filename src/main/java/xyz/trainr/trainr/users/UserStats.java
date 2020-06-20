package xyz.trainr.trainr.users;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Represents the stats of an user
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserStats {

    // Define local variables
    private int blocksPlaced;
    private int totalTries;
    private int succeededTries;
    private long bestTime;

    /**
     * Creates a new user stats object with default values
     */
    public UserStats() {
        this.blocksPlaced = 0;
        this.totalTries = 0;
        this.succeededTries = 0;
        this.bestTime = -1;
    }

    /**
     * Creates a new user stats object with all values
     *
     * @param blocksPlaced   The amount of placed blocks
     * @param totalTries     The total amount of tries
     * @param succeededTries The amount of succeeded tries
     * @param bestTime       The amount of milliseconds of the best try
     */
    @BsonCreator
    public UserStats(@BsonProperty("blocksPlaced") int blocksPlaced,
                     @BsonProperty("totalTries") int totalTries,
                     @BsonProperty("succeededTries") int succeededTries,
                     @BsonProperty("bestTime") long bestTime) {
        this.blocksPlaced = blocksPlaced;
        this.totalTries = totalTries;
        this.succeededTries = succeededTries;
        this.bestTime = bestTime;
    }

    /**
     * @return The amount of placed blocks
     */
    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    /**
     * Sets the amount of placed blocks
     *
     * @param blocksPlaced The amount of placed blocks
     */
    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    /**
     * @return The total amount of tries
     */
    public int getTotalTries() {
        return totalTries;
    }

    /**
     * Sets the total amount of tries
     *
     * @param totalTries The total amount of tries
     */
    public void setTotalTries(int totalTries) {
        this.totalTries = totalTries;
    }

    /**
     * @return The amount of succeeded tries
     */
    public int getSucceededTries() {
        return succeededTries;
    }

    /**
     * Sets the amount of succeeded tries
     *
     * @param succeededTries The amount of succeeded tries
     */
    public void setSucceededTries(int succeededTries) {
        this.succeededTries = succeededTries;
    }

    /**
     * @return The amount of milliseconds of the best try
     */
    public long getBestTime() {
        return bestTime;
    }

    /**
     * Sets the amount of milliseconds of the best try
     *
     * @param bestTime The amount of milliseconds of the best try
     */
    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

}
