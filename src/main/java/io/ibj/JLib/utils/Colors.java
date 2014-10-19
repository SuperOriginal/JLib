package io.ibj.JLib.utils;

import org.bukkit.ChatColor;

/**
 * io.ibj.MattShops.utils
 * Created by Joe
 * Project: MattShops
 */
public class Colors {
    public static String colorify(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String decolorify(String input) {
        return input.replace(ChatColor.COLOR_CHAR, '&');
    }

    public static String stripSpecialCharacters(String input) {
        return input.replaceAll("[&" + ChatColor.COLOR_CHAR + "].", "");
    }
}
