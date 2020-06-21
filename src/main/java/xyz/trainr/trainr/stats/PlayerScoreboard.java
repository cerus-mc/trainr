package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import xyz.trainr.trainr.users.User;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;
import xyz.trainr.trainr.util.StringFormatUtil;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerScoreboard {

    private final Player player;
    private final Scoreboard scoreboard;
    private final UserProvider userProvider;
    private UserStats stats;

    public PlayerScoreboard(UserProvider userProvider, Player player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        this.userProvider = userProvider;

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
        String formattedBestTime = StringFormatUtil.formatMillis(bestTime);

        float succeededTries = stats.getSucceededTries();
        float totalTries = stats.getTotalTries();
        player.sendMessage("" + (succeededTries / (totalTries - succeededTries)));

        // Set the scoreboard contents
        addWhitespace(objective, 11);
        addEntry(objective, "§eSuccesses/Fails:", 10);
        addEntry(objective, "§r §8» §d" + String.format("%.2f", succeededTries / (totalTries - succeededTries)), 9);
        addWhitespace(objective, 8);
        addEntry(objective, "§ePersonal best:", 7);
        addEntry(objective, "§r §8» §b" + formattedBestTime, 6);
        addWhitespace(objective, 5);
        addEntry(objective, "§eGlobal top 3:", 4);

        User[] userArr = userProvider.getCachedUsers().stream()
                .filter(user -> user.getStats().getBestTime() > -1)
                .sorted(Comparator.comparingLong(value -> value.getStats().getBestTime()))
                .limit(3)
                .toArray(User[]::new);

        addEntry(objective, "§r §8» §7" + (userArr.length < 1 ? "N/A" : formatUser(userArr[0])) + "§3", 3);
        addEntry(objective, "§r §8» §7" + (userArr.length < 2 ? "N/A" : formatUser(userArr[1])) + "§2", 2);
        addEntry(objective, "§r §8» §7" + (userArr.length < 3 ? "N/A" : formatUser(userArr[2])) + "§1", 1);
        addWhitespace(objective, 0);

        // Set the objective data
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lTrainr");
    }

    private String formatUser(User user) {
        return Bukkit.getOfflinePlayer(user.getUuid()).getName() + " ["
                + StringFormatUtil.formatMillisShort(user.getStats().getBestTime()) + "]";
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
