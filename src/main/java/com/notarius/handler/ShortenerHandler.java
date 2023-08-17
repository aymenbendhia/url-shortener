package com.notarius.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ShortenerHandler {

    final static Map<String, String> FAKE_ASCII_NUMERIC_MAP;
    final static Map<String, String> FAKE_ASCII_CHAR_MAP;

    public static final String SPLIT_BY_TWO_REGEX = "(?<=\\G.{" + 2 + "})";
    public static final String COMPLETE_TWO_DIGITS_REGEX = "%02d";

    static {
        // Creating 2 maps used as "1 or 2 digits customised ascii code" referential for a maximum of printable characters to be used
        // to compose the shortened url handled part
        Map<String, String> tmpNumericMap = new HashMap<String, String>();
        Map<String, String> tmpCharMap = new HashMap<String, String>();
        String allChoices = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ=+?@#$&()-_{}<>&%£¥µ±ÆÙÚÛÜÝÞßàáâãäåæÕ/";
        for (int i = 0; i < allChoices.length(); i++) {
            String charEntry = "" + allChoices.charAt(i);
            String numericRange = String.format(COMPLETE_TWO_DIGITS_REGEX, i);
            tmpNumericMap.put(numericRange, charEntry);
            tmpCharMap.put(charEntry, numericRange);
        }
        // One digit possibilities
        tmpNumericMap.put("0", "ÿ");
        tmpNumericMap.put("1", "û");
        tmpNumericMap.put("2", "ú");
        tmpNumericMap.put("3", "ù");
        tmpNumericMap.put("4", "ø");
        tmpNumericMap.put("5", "ö");
        tmpNumericMap.put("6", "õ");
        tmpNumericMap.put("7", "ô");
        tmpNumericMap.put("8", "ò");
        tmpNumericMap.put("9", "ï");

        tmpCharMap.put("ÿ", "0");
        tmpCharMap.put("û", "1");
        tmpCharMap.put("ú", "2");
        tmpCharMap.put("ù", "3");
        tmpCharMap.put("ø", "4");
        tmpCharMap.put("ö", "5");
        tmpCharMap.put("õ", "6");
        tmpCharMap.put("ô", "7");
        tmpCharMap.put("ò", "8");
        tmpCharMap.put("ï", "9");

        FAKE_ASCII_NUMERIC_MAP = tmpNumericMap;
        FAKE_ASCII_CHAR_MAP = tmpCharMap;
    }
    public static String calculateShortenedPart(Long id) {
        // Map the db id of the persisted original handled part into a shortened handled part (every 2 digits are mapped to a specific character
        // read from the FAKE_ASCII_NUMERIC_MAP map, the last digit is considered as a one digit key if id character number is odd)
        // In the extreme case, the maximum bigint returned by the db sequence will generate a 10 characters shortened id (see unit tests)
        return Arrays.stream(id.toString().split(SPLIT_BY_TWO_REGEX))
                .map(FAKE_ASCII_NUMERIC_MAP::get)
                .collect(Collectors.joining(""));

    }

    public static Long calculateId(String shortenedPart) {
        // Recalculating the db id of the supposed existing handled part using the inverse shortening method, this time FAKE_ASCII_CHAR_MAP is used
        String idAsString = "";

        for (int i = 0; i < shortenedPart.length(); i++) {
            String key = "" + shortenedPart.charAt(i);
            String numericEntry = FAKE_ASCII_CHAR_MAP.get(key);
            if (numericEntry == null) {
                throw new RuntimeException("Character not accepted: "+key);
            }
            idAsString += numericEntry;
        }

        long calculatedId = Long.parseLong(String.format("%0" + idAsString.length() + "d", Long.valueOf(idAsString)));

        return calculatedId;
    }

    public static String[] splitUrlParts(String url) {
        // Parse the input URL and return the handled and the domain parts, an exception is raised if the URL is not valid
        String domainPart = "";
        String handledPart = "";

        Pattern pattern = Pattern.compile("(www\\.|https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)(?<hostGroup>[^/]+).*");

        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            String hostGroup = matcher.group("hostGroup");
            String[] parts = url.split(hostGroup, 2);
            domainPart = parts[0] + hostGroup;
            handledPart = parts[1];
            return new String[]{domainPart, handledPart};
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }

}
