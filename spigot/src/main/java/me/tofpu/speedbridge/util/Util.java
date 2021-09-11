package me.tofpu.speedbridge.util;

import co.aikar.commands.RegisteredCommand;
import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tofpu.speedbridge.api.util.Identifier;
import me.tofpu.speedbridge.data.file.path.Path;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Util {
    public static boolean isPlaceholderHooked = false;

    public static boolean isEqual(final Location a, final Location b) {
        return ((a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockY() == b.getBlockY()));
    }

    public static double toSeconds(final long beginning, final long end) {
        final long total = end - beginning;
        return (double) total / 1000;
    }

    public static double toSeconds(final long beginning) {
        return toSeconds(beginning, System.currentTimeMillis());
    }

    public static String string(final List<String> list){
        final StringBuilder builder = new StringBuilder();

        for (final String s : list){
            if (builder.capacity() != 1) builder.append("\n");
            builder.append(s);
        }
        return builder.toString();
    }

    public static List<String> toString(final List<? extends Identifier> identifiers) {
        final List<String> toIdentifiers = new ArrayList<>();
        for (final Identifier identifier : identifiers) {
            toIdentifiers.add(identifier.identifier());
        }
        return toIdentifiers;
    }

    public static List<String> toString(final Enum<?>[] enums) {
        final List<String> toIdentifiers = new ArrayList<>();
        for (final Enum<?> identifier : enums) {
            toIdentifiers.add(identifier.name().toLowerCase(Locale.ROOT).replace("_", ""));
        }
        return toIdentifiers;
    }

    public static String format(final RegisteredCommand<?> command) {
        final String format = " &6&l&m*&r &e/%command% &6%syntax% &6&l&m-&r &e%description%";

        final Map<String, String> map = Maps.newHashMap();
        map.put("%command%", command.getCommand());
        map.put("%syntax%", command.getSyntaxText());
        map.put("%description%", command.getHelpText());
        return Util.colorize(Util.WordReplacer.replace(format, map));
    }

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

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void message(final CommandSender sender, Path.Value<?> path) {
        message(sender, path, null);
    }

    public static void message(final CommandSender sender, Path.Value<?> path, final String[] replaceArray, final String... replaceWith) {
        final Object value = path.getValue();

        String message = null;
        if (value instanceof String){
            message = (String) path.getValue();
        } else if (value instanceof List){
            message = string((List<String>) value);
        }
        if (message == null || message.isEmpty()) return;

        message(sender, message, replaceArray, true, replaceWith);
    }

    public static void message(final CommandSender sender, String message, final String[] replaceArray, boolean usePlaceholder, final String... replaceWith) {
        if (Util.isPlaceholderHooked && usePlaceholder && sender instanceof Player)
            message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        sender.sendMessage(colorize(WordReplacer.replace(message, replaceArray, replaceWith)));
    }

    public static class WordReplacer {
        public static String replace(String message, final String[] replaceArray, final String... replaceWith) {
            if (replaceArray == null) return message;
            for (int i = 0; i < replaceArray.length; i++) {
                message = message.replace(replaceArray[i], replaceWith[i]);
            }
            return message;
        }

        public static String replace(String message, final Map<String, ?> replaceMap) {
            if (replaceMap == null) return message;
            for (final Map.Entry<String, ?> replace : replaceMap.entrySet()) {
                message = message.replace(replace.getKey(), replace.getValue() + "");
            }
            return message;
        }
    }
}
