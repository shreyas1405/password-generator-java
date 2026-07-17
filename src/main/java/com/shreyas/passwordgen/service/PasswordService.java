package com.shreyas.passwordgen.service;

import com.shreyas.passwordgen.model.AnalyzeRequest;
import com.shreyas.passwordgen.model.GenerateRequest;
import com.shreyas.passwordgen.model.PasswordResponse;
import com.shreyas.passwordgen.util.CharsetLibrary;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordService {

    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResponse generatePassword(GenerateRequest request) {
        int length = request.getLength();

        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4.");
        }

        List<String> selectedPools = new ArrayList<>();
        List<String> selectedNames = new ArrayList<>();

        addPool(request.isIncludeLowercase(), "Lowercase", CharsetLibrary.LOWERCASE, request, selectedPools, selectedNames);
        addPool(request.isIncludeUppercase(), "Uppercase", CharsetLibrary.UPPERCASE, request, selectedPools, selectedNames);
        addPool(request.isIncludeDigits(), "Digits", CharsetLibrary.DIGITS, request, selectedPools, selectedNames);
        addPool(request.isIncludeSymbols(), "Symbols", CharsetLibrary.SYMBOLS, request, selectedPools, selectedNames);
        addPool(request.isIncludeSpace(), "Space", CharsetLibrary.SPACE, request, selectedPools, selectedNames);
        addPool(request.isIncludeGreek(), "Greek", CharsetLibrary.GREEK, request, selectedPools, selectedNames);
        addPool(request.isIncludeCyrillic(), "Cyrillic", CharsetLibrary.CYRILLIC, request, selectedPools, selectedNames);
        addPool(request.isIncludeArabic(), "Arabic", CharsetLibrary.ARABIC, request, selectedPools, selectedNames);
        addPool(request.isIncludeDevanagari(), "Devanagari", CharsetLibrary.DEVANAGARI, request, selectedPools, selectedNames);
        addPool(request.isIncludeJapaneseKana(), "Japanese Kana", CharsetLibrary.JAPANESE_KANA, request, selectedPools, selectedNames);

        if (selectedPools.isEmpty()) {
            throw new IllegalArgumentException("Select at least one character set.");
        }

        if (request.isRequireEachSelectedSet() && length < selectedPools.size()) {
            throw new IllegalArgumentException("Length must be at least the number of selected character sets.");
        }

        String combinedPool = CharsetLibrary.deduplicate(String.join("", selectedPools));
        int[] combinedCodePoints = CharsetLibrary.toCodePoints(combinedPool);

        List<String> passwordChars = new ArrayList<>();

        if (request.isRequireEachSelectedSet()) {
            for (String pool : selectedPools) {
                passwordChars.add(randomCharFromPool(pool));
            }
        }

        while (passwordChars.size() < length) {
            passwordChars.add(codePointToString(combinedCodePoints[secureRandom.nextInt(combinedCodePoints.length)]));
        }

        Collections.shuffle(passwordChars, secureRandom);

        String password = String.join("", passwordChars);
        return buildResponse(password, combinedPool, selectedNames, "Generated with cryptographically secure randomness.");
    }

    public PasswordResponse analyzePassword(AnalyzeRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        String password = request.getPassword();
        List<String> sets = detectSets(password);
        String pool = estimatePoolFromPassword(password);
        return buildResponse(password, pool, sets, "Analysis is an estimate based on detected character categories.");
    }

    private PasswordResponse buildResponse(String password, String combinedPool, List<String> selectedNames, String note) {
        int length = password.codePointCount(0, password.length());
        int poolSize = combinedPool.codePointCount(0, combinedPool.length());
        double entropyBits = length * (Math.log(poolSize) / Math.log(2));
        String strengthLevel = classifyStrength(entropyBits);

        String crackTimeOnline = formatCrackTime(entropyBits, 100);
        String crackTimeOfflineFast = formatCrackTime(entropyBits, 10_000_000_000L);
        String crackTimeOfflineSlow = formatCrackTime(entropyBits, 10_000L);

        return new PasswordResponse(
                password,
                length,
                poolSize,
                round(entropyBits),
                strengthLevel,
                crackTimeOnline,
                crackTimeOfflineFast,
                crackTimeOfflineSlow,
                selectedNames,
                note
        );
    }

    private void addPool(boolean enabled, String name, String chars, GenerateRequest request,
                         List<String> selectedPools, List<String> selectedNames) {
        if (!enabled) return;
        String pool = chars;
        if (request.isExcludeAmbiguous()) {
            pool = CharsetLibrary.removeAmbiguous(pool);
        }
        pool = CharsetLibrary.deduplicate(pool);
        if (!pool.isEmpty()) {
            selectedPools.add(pool);
            selectedNames.add(name);
        }
    }

    private String randomCharFromPool(String pool) {
        int[] codePoints = CharsetLibrary.toCodePoints(pool);
        return codePointToString(codePoints[secureRandom.nextInt(codePoints.length)]);
    }

    private String codePointToString(int codePoint) {
        return new String(Character.toChars(codePoint));
    }

    private List<String> detectSets(String password) {
        List<String> sets = new ArrayList<>();
        if (containsFrom(password, CharsetLibrary.LOWERCASE)) sets.add("Lowercase");
        if (containsFrom(password, CharsetLibrary.UPPERCASE)) sets.add("Uppercase");
        if (containsFrom(password, CharsetLibrary.DIGITS)) sets.add("Digits");
        if (containsFrom(password, CharsetLibrary.SYMBOLS)) sets.add("Symbols");
        if (containsFrom(password, CharsetLibrary.SPACE)) sets.add("Space");
        if (containsFrom(password, CharsetLibrary.GREEK)) sets.add("Greek");
        if (containsFrom(password, CharsetLibrary.CYRILLIC)) sets.add("Cyrillic");
        if (containsFrom(password, CharsetLibrary.ARABIC)) sets.add("Arabic");
        if (containsFrom(password, CharsetLibrary.DEVANAGARI)) sets.add("Devanagari");
        if (containsFrom(password, CharsetLibrary.JAPANESE_KANA)) sets.add("Japanese Kana");
        if (sets.isEmpty()) sets.add("Unclassified");
        return sets;
    }

    private String estimatePoolFromPassword(String password) {
        StringBuilder pool = new StringBuilder();
        if (containsFrom(password, CharsetLibrary.LOWERCASE)) pool.append(CharsetLibrary.LOWERCASE);
        if (containsFrom(password, CharsetLibrary.UPPERCASE)) pool.append(CharsetLibrary.UPPERCASE);
        if (containsFrom(password, CharsetLibrary.DIGITS)) pool.append(CharsetLibrary.DIGITS);
        if (containsFrom(password, CharsetLibrary.SYMBOLS)) pool.append(CharsetLibrary.SYMBOLS);
        if (containsFrom(password, CharsetLibrary.SPACE)) pool.append(CharsetLibrary.SPACE);
        if (containsFrom(password, CharsetLibrary.GREEK)) pool.append(CharsetLibrary.GREEK);
        if (containsFrom(password, CharsetLibrary.CYRILLIC)) pool.append(CharsetLibrary.CYRILLIC);
        if (containsFrom(password, CharsetLibrary.ARABIC)) pool.append(CharsetLibrary.ARABIC);
        if (containsFrom(password, CharsetLibrary.DEVANAGARI)) pool.append(CharsetLibrary.DEVANAGARI);
        if (containsFrom(password, CharsetLibrary.JAPANESE_KANA)) pool.append(CharsetLibrary.JAPANESE_KANA);

        String result = CharsetLibrary.deduplicate(pool.toString());
        if (result.isEmpty()) {
            result = CharsetLibrary.deduplicate(password);
        }
        return result;
    }

    private boolean containsFrom(String text, String pool) {
        return text.codePoints().anyMatch(cp -> pool.codePoints().anyMatch(p -> p == cp));
    }

    private String classifyStrength(double entropyBits) {
        if (entropyBits < 40) return "Weak";
        if (entropyBits < 60) return "Moderate";
        if (entropyBits < 80) return "Strong";
        return "Very Strong";
    }

    private String formatCrackTime(double entropyBits, long guessesPerSecond) {
        double combinations = Math.pow(2, entropyBits);
        double averageSeconds = combinations / 2.0 / guessesPerSecond;

        if (averageSeconds < 1) return "Less than 1 second";
        if (averageSeconds < 60) return ((long) averageSeconds) + " seconds";
        if (averageSeconds < 3600) return ((long) (averageSeconds / 60)) + " minutes";
        if (averageSeconds < 86400) return ((long) (averageSeconds / 3600)) + " hours";
        if (averageSeconds < 31536000) return ((long) (averageSeconds / 86400)) + " days";
        if (averageSeconds < 31536000L * 1000) return ((long) (averageSeconds / 31536000)) + " years";
        if (averageSeconds < 31536000L * 1_000_000) return ((long) (averageSeconds / (31536000L * 1000))) + " thousand years";
        if (averageSeconds < 31536000L * 1_000_000_000L) return ((long) (averageSeconds / (31536000L * 1_000_000))) + " million years";
        return "Billions of years";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
