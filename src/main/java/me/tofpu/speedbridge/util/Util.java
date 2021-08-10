package me.tofpu.speedbridge.util;

import me.tofpu.speedbridge.data.file.config.Config;
import me.tofpu.speedbridge.data.file.config.path.Path;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class Util {
    public static Integer parseInt(final String s) {
        final int radix = 10;

        if (s == null) return null;

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+') return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) return null;
                if (result < multmin) return null;
                result *= radix;
                if (result < limit + digit) return null;
                result -= digit;
            }
        } else return null;
        return negative ? result : -result;
    }

    public static void message(final Player player, Path path){
        message(player, path, null);
    }

    public static void message(final Player player, Path path, final Map<String, ?> replaceMap){
        final String message = Config.TranslateOutput.toString(path);
        if (message == null) return;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Util.WordReplacer.replace(message, replaceMap)));
    }

    public static class WordReplacer {
        public static String replace(String message, final Map<String, ?> replaceMap){
            if (replaceMap == null) return message;
            for (final Map.Entry<String, ?> replace : replaceMap.entrySet()){
                message = message.replace(replace.getKey(), replace.getValue() + "");
            }
            return message;
        }
    }
}
