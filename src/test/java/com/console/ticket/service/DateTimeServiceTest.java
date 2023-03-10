package com.console.ticket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


class DateTimeServiceTest {
    public LocalDateTime currentDateTime;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @BeforeEach
    void initialize() {
        currentDateTime = LocalDateTime.now();
    }

    @Test
    void checkGetCurrentDateReturnCurrentDate() {
        String expectedDateTime = currentDateTime.format(dateTimeFormatter);
        String actualDateTime = DateTimeService.getCurrentDate();

        assertThat(expectedDateTime).isEqualTo(actualDateTime);
    }

    @Test
    void checkGetCurrentTimeReturnCurrentTime() {
        String expectedDateTime = currentDateTime.format(timeFormatter);
        String actualDateTime = DateTimeService.getCurrentTime();

        assertThat(expectedDateTime).isEqualTo(actualDateTime);
    }
}