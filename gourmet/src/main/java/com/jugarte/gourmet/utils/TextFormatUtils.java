package com.jugarte.gourmet.utils;

public class TextFormatUtils {

    private static final char SPACE = ' ';

    public static String formatCreditCardNumber(String cardNumber) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ");
            }
            result.append(cardNumber.charAt(i));
        }
        return result.toString();
    }

    public static String formatRemoveSpaces(String text) {
        return text.replaceAll(" ", "");
    }
}
