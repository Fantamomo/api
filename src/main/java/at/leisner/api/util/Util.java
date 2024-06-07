package at.leisner.api.util;

public class Util {
    public static String removeAfterLastChar(String str, char ch) {
        // Findet den Index des letzten Vorkommens des Zeichens
        int lastIndex = str.lastIndexOf(ch);

        // Wenn das Zeichen nicht gefunden wurde, gebe den originalen String zurück
        if (lastIndex == -1) {
            return str;
        }

        // Schneidet den String bis zum letzten Vorkommen des Zeichens
        return str.substring(0, lastIndex);
    }
}
