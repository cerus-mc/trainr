package xyz.trainr.trainr.stats;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerScoreboard {

    private final Player player;
    private final Scoreboard scoreboard;
    private UserStats stats;

    public PlayerScoreboard(UserProvider userProvider, Player player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;

        userProvider.getCachedUser(player.getUniqueId()).ifPresent(user -> {
            this.stats = user.getStats();
            setPlayerScoreboard();
        });
    }

    public void setPlayerScoreboard() {
        initScoreboard(stats);
        player.setScoreboard(scoreboard);
    }

    private void initScoreboard(UserStats stats) {
        // Re-register the objective
        Objective objective = scoreboard.getObjective("main_obj");
        if (objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("main_obj", "dummy");

        long bestTime = stats.getBestTime();
        String formattedBestTime;
        if (bestTime != -1) {
            long minutes = (bestTime / 1000) / 60;
            long seconds = (bestTime / 1000) % 60;
            long millis = bestTime % 1000;
            formattedBestTime = String.format("%02dm %02ds %02dms", minutes, seconds, millis);
        } else {
            formattedBestTime = "NaN";
        }

        // Set the scoreboard contents
        addWhitespace(objective, 11);
        addEntry(objective, "§eAccount:", 10);
        addEntry(objective, "§r §8» §d" + player.getDisplayName(), 9);
        addWhitespace(objective, 8);
        addEntry(objective, "§ePersonal best:", 7);
        addEntry(objective, "§r §8» §b" + formattedBestTime, 6);
        addWhitespace(objective, 5);
        addEntry(objective, "§eGlobal top 3:", 4);
        addEntry(objective, "§r §8» §7NaN§3", 3);
        addEntry(objective, "§r §8» §7NaN§2", 2);
        addEntry(objective, "§r §8» §7NaN§1", 1);
        addWhitespace(objective, 0);

        // Set the objective data
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lTrainr");
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

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
