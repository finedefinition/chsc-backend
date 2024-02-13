package ua.dlc.chscbackend.util;

import ua.dlc.chscbackend.model.Ticker;

import java.util.Optional;

public class ChatMessageParser {

//    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})"); // Simplified example

//    public static LocalDate extractDate(String message) {
//        Matcher matcher = DATE_PATTERN.matcher(message);
//        if (matcher.find()) {
//            return LocalDate.parse(matcher.group(1));
//        }
//        return null; // or throw an exception depending on your error handling
//    }

    public static Optional<Ticker> extractTicker(String message) {
        for (Ticker symbol : Ticker.values()) {
            if (message.toLowerCase().contains(symbol.getFullName().toLowerCase())) {
                return Optional.of(symbol);
            }
        }
        return Optional.empty(); // Return an empty Optional when no ticker is found
    }

}

