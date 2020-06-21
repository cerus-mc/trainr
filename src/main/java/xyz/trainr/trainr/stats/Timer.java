package xyz.trainr.trainr.stats;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer {

    private final Map<UUID, Long> timerMap = new HashMap<>();

    public void startTimer(Player player) {
        timerMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public long stopTimer(Player player) {
        if(!isTimmerRunning(player)) {
            return -1;
        }
        return System.currentTimeMillis() - timerMap.remove(player.getUniqueId());
    }

    public boolean isTimmerRunning(Player player) {
        return timerMap.containsKey(player.getUniqueId());
    }

}
