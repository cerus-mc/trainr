package xyz.trainr.trainr.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.trainr.trainr.util.StringFormatUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TimeDisplayTask implements Runnable {

    private final Timer timer;

    public TimeDisplayTask(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(timer::isTimmerRunning)
                .forEach(player -> sendActionbar(player, "§8» §b"
                        + StringFormatUtil.formatMillis(timer.peekTimer(player)) + " §8«"));
    }

    private void sendActionbar(Player player, String text) {
        try {
            Method method = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer")
                    .getDeclaredMethod("a", String.class);
            Object comp = method.invoke(null, "{\"text\": \"" + text + "\"}");
            Object packet = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutChat")
                    .getDeclaredConstructor(comp.getClass().getSuperclass().getInterfaces()[0], byte.class)
                    .newInstance(comp, (byte) 2);
            Object craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer").cast(player);
            Object handle = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);
            Object con = handle.getClass().getDeclaredField("playerConnection").get(handle);
            con.getClass().getDeclaredMethod("sendPacket", packet.getClass().getInterfaces()[0]).invoke(con, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
