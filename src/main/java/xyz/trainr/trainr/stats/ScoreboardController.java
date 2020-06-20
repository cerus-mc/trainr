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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class ScoreboardController {

    private final UserProvider userProvider;

    public ScoreboardController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public void setScoreboard(Player player) {
        player.setScoreboard(getNewScoreboard(player));
    }

    private Scoreboard getNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("main_obj", "dummy");

        CompletableFuture<User> future = userProvider.getUser(player.getUniqueId());
        future.whenComplete((user, throwable) -> {
            if (throwable != null) {
                player.sendMessage("§cFailed to load your scoreboard");
                return;
            }
            UserStats stats = user.getStats();

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

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("§6§lTrainr");
        });

        return scoreboard;
    }

    private void addEntry(Objective objective, String text, int score) {
        Score objectiveScore = objective.getScore(text);
        objectiveScore.setScore(score);
    }

    private void addWhitespace(Objective objective, int score) {
        String[] arr = new String[]{"§a", "§e", "§d", "§c", "§b", "§1", "§2", "§3", "§4",
                "§5", "§6", "§7", "§8", "§9", "§0", "§m", "§n", "§l", "§o", "§r", "§f"};

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(arr[ThreadLocalRandom.current().nextInt(0, arr.length)]);
        }

        addEntry(objective, stringBuilder.toString(), score);
    }

}
