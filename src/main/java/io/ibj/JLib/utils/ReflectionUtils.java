package io.ibj.JLib.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * io.ibj.MattShops.utils
 * Created by Joe
 * Project: MattShops
 */
public class ReflectionUtils {
    private static String mcVersion = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]+".";
    private static String cbVersion = "org.bukkit.craftbukkit."+ Bukkit.getServer().getClass().getPackage().getName().replace(".",",").split(",")[3]+".";
    private static String spigotVersion = "org.spigotmc.";

    public static Class getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName(mcVersion+className);
    }

    public static Class getCBClass(String className) throws ClassNotFoundException {
        return Class.forName(cbVersion+className);
    }

    public static Class getSpigotClass(String className) throws ClassNotFoundException
    {
        return Class.forName(spigotVersion+className);
    }

    private static Method getHandle = null;
    private static Field playerConn = null;
    private static Method sendPacket = null;

    public static void sendPacket(Player p, Object packet){
        try{
            if(getHandle == null){
                getHandle = p.getClass().getDeclaredMethod("getHandle");
            }
            Object nmsplayer = getHandle.invoke(p);
            if(playerConn == null) {
                playerConn = nmsplayer.getClass().getField("playerConnection");
            }
            Object conn = playerConn.get(nmsplayer);
            if(sendPacket == null){
                sendPacket = conn.getClass().getDeclaredMethod("sendPacket",getNMSClass("Packet"));
            }

            sendPacket.invoke(conn,packet);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
