package com.exercise.helper;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return DATE_TIME_FORMATTER.format(dateTime);
    }

}
