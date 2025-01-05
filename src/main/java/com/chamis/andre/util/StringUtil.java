package com.chamis.andre.util;

/**
 * Utility class for string operations.
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public class StringUtil {
    private StringUtil(){
    }

    /**
     * Pads the given string with spaces on the right to ensure it reaches the specified length.
     * If the input string is {@code null}, it is treated as an empty string.
     *
     * @param s       the input string to be padded
     * @param padding the total length of the resulting string, including the input string and padding
     * @return the padded string, with spaces added to the right if necessary
     * @since 1.0.0
     */
    public static String padRight(String s, int padding) {
        if (s == null) {
            s = "";
        }
        return String.format("%-" + padding + "s", s);
    }
}
