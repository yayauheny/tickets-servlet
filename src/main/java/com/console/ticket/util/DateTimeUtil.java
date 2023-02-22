package com.console.ticket.util;
/**
 * Получение текущего времени (даты) по переданному паттерну
 *
 */

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    private static LocalDateTime currentDateTime;

    public static String getCurrentDateTime(DateTimeFormatter formatter){
        try {
            return currentDateTime.now().format(formatter);
        } catch (DateTimeException e) {
            e.printStackTrace();
            return LocalDateTime.now().toString();
        }
    }
}
