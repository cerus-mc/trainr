package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;

public class ScoreboardUpdateTask implements Runnable {

    private final ScoreboardController scoreboardController;

    public ScoreboardUpdateTask(ScoreboardController scoreboardController) {
        this.scoreboardController = scoreboardController;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(scoreboardController::setScoreboard);
    }

}
