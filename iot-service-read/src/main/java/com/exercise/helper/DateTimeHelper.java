package com.exercise.helper;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateTimeHelper {
    /*Duplicate code should move to an common module*/
    public static final String EMPTY = "";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);


    public static String formatDateTime(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(dt -> DATE_TIME_FORMATTER.format(dateTime))
                .orElse(EMPTY);
    }

}
