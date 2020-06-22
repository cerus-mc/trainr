package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.trainr.trainr.users.User;
import xyz.trainr.trainr.users.UserProvider;
import xyz.trainr.trainr.users.UserStats;
import xyz.trainr.trainr.util.StringFormatUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PlayerScoreboard {

    private final Map<Integer, TeamEntryPair> teamMap = new HashMap<>();

    private Team normalTeam;
    private Team staffTeam;
    private Team firstTeam;
    private Team secondTeam;
    private Team thirdTeam;

    private final Player player;
    private final Scoreboard scoreboard;
    private final UserProvider userProvider;
    private UserStats stats;

    public PlayerScoreboard(UserProvider userProvider, Player player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        this.userProvider = userProvider;

        initialSetup();

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
        // Obtain the objective
        Objective objective = scoreboard.getObjective("main_obj");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("main_obj", "dummy");
        }

        long bestTime = stats.getBestTime();
        String formattedBestTime = StringFormatUtil.formatMillis(bestTime);

        float succeededTries = stats.getSucceededTries();
        float totalTries = stats.getTotalTries();

        // Set the scoreboard contents
        setWhitespace(objective, 11);
        setEntry(objective, "§eSuccesses/Fails:", 10);
        setEntry(objective, "§r §8» §d" + String.format("%.2f", succeededTries / (totalTries - succeededTries)), 9);
        setWhitespace(objective, 8);
        setEntry(objective, "§ePersonal best:", 7);
        setEntry(objective, "§r §8» §b" + formattedBestTime, 6);
        setWhitespace(objective, 5);
        setEntry(objective, "§eGlobal top 3:", 4);

        User[] userArr = userProvider.getCachedUsers().stream()
                .filter(user -> user.getStats().getBestTime() > -1)
                .sorted(Comparator.comparingLong(value -> value.getStats().getBestTime()))
                .limit(3)
                .toArray(User[]::new);

        setEntry(objective, "§r §8» §7" + (userArr.length < 1 ? "N/A" : formatUser(userArr[0])) + "§3", 3);
        setEntry(objective, "§r §8» §7" + (userArr.length < 2 ? "N/A" : formatUser(userArr[1])) + "§2", 2);
        setEntry(objective, "§r §8» §7" + (userArr.length < 3 ? "N/A" : formatUser(userArr[2])) + "§1", 1);
        setWhitespace(objective, 0);

        Set<UUID> uuids = Arrays.stream(userArr).map(User::getUuid).collect(Collectors.toSet());
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(plr -> !uuids.contains(plr.getUniqueId()))
                .forEach(plr -> {
                    if (plr.hasPermission("staff")) {
                        staffTeam.addEntry(plr.getName());
                    } else {
                        normalTeam.addEntry(plr.getName());
                    }
                });
        if (userArr.length > 1) {
            firstTeam.addEntry(Bukkit.getOfflinePlayer(userArr[0].getUuid()).getName());
        }
        if (userArr.length > 2) {
            firstTeam.addEntry(Bukkit.getOfflinePlayer(userArr[1].getUuid()).getName());
        }
        if (userArr.length > 3) {
            firstTeam.addEntry(Bukkit.getOfflinePlayer(userArr[2].getUuid()).getName());
        }

        // Set the objective data
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§8•● §6§lTrainr §8●•");
    }

    private void initialSetup() {
        normalTeam = scoreboard.registerNewTeam("_normal");
        normalTeam.setPrefix("§7");

        staffTeam = scoreboard.registerNewTeam("_staff");
        staffTeam.setPrefix("§cStaff §8• §7");

        firstTeam = scoreboard.registerNewTeam("_1st");
        firstTeam.setPrefix("§61st §8• §7");

        secondTeam = scoreboard.registerNewTeam("_2nd");
        secondTeam.setPrefix("§e2nd §8• §7");

        thirdTeam = scoreboard.registerNewTeam("_3rd");
        thirdTeam.setPrefix("§b3rd §8• §7");
    }

    private String formatUser(User user) {
        return Bukkit.getOfflinePlayer(user.getUuid()).getName() + " ["
                + StringFormatUtil.formatMillisShort(user.getStats().getBestTime()) + "]";
    }

    private void setWhitespace(Objective objective, int score) {
        setEntry(objective, generateRandomString(), score);
    }

    public void setEntry(Objective objective, String text, int score) {
        TeamEntryPair pair = teamMap.getOrDefault(score, new TeamEntryPair(scoreboard.registerNewTeam(generateRandomString()), null));
        Team team = pair.team;

        String str = (pair.entry == null ? generateRandomString() + "§r" : pair.entry);
        pair.entry = str;

        if (text.length() <= 16) {
            team.setPrefix(text);
        } else if (text.length() <= 32) {
            String part1 = text.substring(0, 16);
            String part2 = findLastColorCode(part1) + text.substring(16);
            String part3 = part2.length() > 16 ? part2.substring(16) : null;

            team.setPrefix(part1);
            if (part3 != null) {
                str = part2;
                team.setSuffix(part3);
            } else {
                team.setSuffix(part2);
            }
        } else {
            String part1 = text.substring(0, 16);
            String part2 = findLastColorCode(part1) + text.substring(16);
            String part3 = findLastColorCode(part2) + text.substring(16, 32);

            team.setPrefix(part1);
            str = part2;
            team.setSuffix(part3.length() > 16 ? part3.substring(0, 16) : part3);
        }

        if (!team.hasEntry(str)) {
            team.addEntry(str);
        }

        objective.getScore(str).setScore(score);

        if (!teamMap.containsKey(score)) {
            teamMap.put(score, pair);
        }
    }

    private String findLastColorCode(String s) {
        if (s.length() < 2) {
            return "";
        }

        char c = '\0';
        char[] chars = s.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            char curr = chars[i];
            if (curr == '§' && i < chars.length - 1) {
                c = chars[i + 1];
                break;
            }
        }
        return c == '\0' ? "" : "§" + c;
    }

    private String generateRandomString() {
        // Define an array of color and formatting codes
        String[] arr = new String[]{"§a", "§e", "§d", "§c", "§b", "§1", "§2", "§3", "§4",
                "§5", "§6", "§7", "§8", "§9", "§0", "§m", "§n", "§l", "§o", "§r", "§f"};

        // Generate a random string of formatting codes
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(arr[ThreadLocalRandom.current().nextInt(0, arr.length)]);
        }

        return stringBuilder.toString();
    }

    public void destroy() {
        teamMap.values().forEach(teamEntryPair -> teamEntryPair.team.unregister());
        normalTeam.unregister();
        staffTeam.unregister();
        firstTeam.unregister();
        secondTeam.unregister();
        thirdTeam.unregister();

        scoreboard.getEntries().forEach(scoreboard::resetScores);

        teamMap.clear();
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    static class TeamEntryPair {
        public Team team;
        public String entry;

        public TeamEntryPair(Team team, String entry) {
            this.team = team;
            this.entry = entry;
        }
    }
}
