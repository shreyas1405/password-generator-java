package com.shreyas.passwordgen.util;

import java.util.LinkedHashSet;
import java.util.Set;

public class CharsetLibrary {

    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?/~`\"'\\";
    public static final String SPACE = " ";

    public static final String GREEK =
            "αβγδεζηθικλμνξοπρστυφχψω" +
            "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";

    public static final String CYRILLIC =
            "абвгдежзийклмнопрстуфхцчшщъыьэюя" +
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    public static final String ARABIC =
            "ابتثجحخدذرزسشصضطظعغفقكلمنهوي";

    public static final String DEVANAGARI =
            "अआइईउऊऋएऐओऔकखगघचछजझटठडढतथदधनपफबभमयरलवशषसह";

    public static final String JAPANESE_KANA =
            "あいうえおかきくけこさしすせそたちつてとなにぬねの" +
            "はひふへほまみむめもやゆよらりるれろわをん" +
            "アイウエオカキクケコサシスセソタチツテトナニヌネノ" +
            "ハヒフヘホマミムメモヤユヨラリルレロワヲン";

    public static final String AMBIGUOUS = "0O1lI|`'\"";

    private CharsetLibrary() {
    }

    public static String removeAmbiguous(String input) {
        return input.codePoints()
                .filter(cp -> AMBIGUOUS.indexOf(cp) == -1)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static int[] toCodePoints(String input) {
        return input.codePoints().toArray();
    }

    public static String deduplicate(String input) {
        Set<Integer> set = new LinkedHashSet<>();
        input.codePoints().forEach(set::add);
        StringBuilder sb = new StringBuilder();
        for (Integer cp : set) {
            sb.appendCodePoint(cp);
        }
        return sb.toString();
    }
}
