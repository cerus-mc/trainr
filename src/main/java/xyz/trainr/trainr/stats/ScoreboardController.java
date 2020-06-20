package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import xyz.trainr.trainr.Trainr;
import xyz.trainr.trainr.users.User;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Keeps track of the players scoreboard
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class ScoreboardController {

    // Define the user provider
    private final UserProvider userProvider;

    /**
     * Creates a new scoreboard controller
     *
     * @param userProvider The user provider to use
     */
    public ScoreboardController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    /**
     * Sets the scoreboard to the given player
     *
     * @param player The player to set the scoreboard to
     */
    public void setScoreboard(Player player) {
        getNewScoreboard(player).whenComplete((scoreboard, throwable) -> {
            if (throwable != null) {
                player.sendMessage("§cFailed to load scoreboard");
                return;
            }

            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Trainr.class), () -> player.setScoreboard(scoreboard));
        });
    }

    /**
     * Generates a new scoreboard
     *
     * @param player The player to generate to scoreboard for
     * @return The generated scoreboard
     */
    private CompletableFuture<Scoreboard> getNewScoreboard(Player player) {
        // Create the scoreboard and register a new objective
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("main_obj", "dummy");

        CompletableFuture<Scoreboard> scoreboardCompletableFuture = new CompletableFuture<>();

        // Retrieve the user object of the player
        userProvider.getCachedUser(player.getUniqueId()).ifPresent(user -> {
            // Define the stats of the player
            UserStats stats = user.getStats();

            // Set the scoreboard contents
            addWhitespace(objective, 11);
            addEntry(objective, "§eAccount:", 10);
            addEntry(objective, "§r §8» §d" + player.getDisplayName(), 9);
            addWhitespace(objective, 8);
            addEntry(objective, "§ePersonal best:", 7);
            addEntry(objective, "§r §8» §b" + stats.getBestTime(), 6);
            addWhitespace(objective, 5);
            addEntry(objective, "§eGlobal top 3:", 4);
            addEntry(objective, "§r §8» §7NaN§3", 3);
            addEntry(objective, "§r §8» §7NaN§2", 2);
            addEntry(objective, "§r §8» §7NaN§1", 1);
            addWhitespace(objective, 0);

            // Set the objective data
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("§6§lTrainr");

            scoreboardCompletableFuture.complete(scoreboard);
        });

        // Return the scoreboard future object
        return scoreboardCompletableFuture;
    }

    /**
     * Adds an entry to the scoreboard
     *
     * @param objective The objective to set the entry to
     * @param text      The text of the score
     * @param score     The score to set
     */
    private void addEntry(Objective objective, String text, int score) {
        Score objectiveScore = objective.getScore(text);
        objectiveScore.setScore(score);
    }

    /**
     * Adds a whitespace to the scoreboard
     *
     * @param objective The objective to set the whitespace to
     * @param score     The score to set
     */
    private void addWhitespace(Objective objective, int score) {
        // Define ann array of color and formatting codes
        String[] arr = new String[]{"§a", "§e", "§d", "§c", "§b", "§1", "§2", "§3", "§4",
                "§5", "§6", "§7", "§8", "§9", "§0", "§m", "§n", "§l", "§o", "§r", "§f"};

        // Generate a random string of formatting codes
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(arr[ThreadLocalRandom.current().nextInt(0, arr.length)]);
        }

        // Set the random generated string to the objective
        addEntry(objective, stringBuilder.toString(), score);
    }

}
