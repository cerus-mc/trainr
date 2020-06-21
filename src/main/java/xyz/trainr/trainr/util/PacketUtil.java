package xyz.trainr.trainr.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PacketUtil {

    private PacketUtil() {
    }

    public static void broadcastPacket(Object packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer").cast(player);
            Object handle = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);
            Object con = handle.getClass().getDeclaredField("playerConnection").get(handle);
            con.getClass().getDeclaredMethod("sendPacket", packet.getClass().getInterfaces()[0]).invoke(con, packet);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
