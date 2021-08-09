package me.tofpu.speedbridge.util;

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
}
