package org.example.revworkforce.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // Default date format
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    // Default datetime format
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Convert String to LocalDate
     * @param dateStr String like "2026-01-25"
     * @return LocalDate or null if invalid
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr);
            return null;
        }
    }

    /**
     * Convert LocalDate to String
     * @param date LocalDate
     * @return String in yyyy-MM-dd format
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return date.format(formatter);
    }

    /**
     * Convert String to LocalDateTime
     * @param dateTimeStr "2026-01-25 18:30:00"
     * @return LocalDateTime or null if invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid datetime format: " + dateTimeStr);
            return null;
        }
    }

    /**
     * Convert LocalDateTime to String
     * @param dateTime LocalDateTime
     * @return String in yyyy-MM-dd HH:mm:ss format
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
        return dateTime.format(formatter);
    }

    /**
     * Check if a date is before today
     * @param date LocalDate
     * @return true if before today
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Check if a date is after today
     * @param date LocalDate
     * @return true if future date
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Check if two dates are equal
     */
    public static boolean isSameDate(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) return false;
        return date1.isEqual(date2);
    }

}

