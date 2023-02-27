package com.console.ticket.service;
/**
 * Получение текущего времени (даты) по переданному паттерну
 *
 */

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeService {
    private static LocalDateTime currentDateTime;
    private static DateTimeFormatter currentDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter currentTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String getCurrentDateTime(DateTimeFormatter formatter){
        try {
            return currentDateTime.now().format(formatter);
        } catch (DateTimeException e) {
            e.printStackTrace();
            return LocalDateTime.now().toString();
        }
    }

    public static String getCurrentDate() {
        return getCurrentDateTime(currentDateFormat);
    }

    public static String getCurrentTime() {
        return getCurrentDateTime(currentTimeFormat);
    }
}
