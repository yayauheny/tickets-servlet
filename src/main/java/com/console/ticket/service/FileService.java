package com.console.ticket.service;


import com.console.ticket.constants.Constants;
import com.console.ticket.exception.FileException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileService {
    private static String ticketInputLog;

    public static void writeReceipt(String receipt) throws FileException {
        File file = new File((String.format("tickets/ticket%s.txt", Constants.CASHIER_NUMBER)));
        try {
            Files.write(Path.of(file.toURI()), receipt.getBytes());
        } catch (IOException e) {
            throw new FileException("Exception while writing to file: ", e);
        }
    }

    public static void readReceipt(String inputPath) throws FileException {
        try {
            ticketInputLog = Files.readString(Path.of(inputPath), StandardCharsets.UTF_8);
            System.out.println(ticketInputLog);
        } catch (IOException e) {
            throw new FileException("Exception while reading a file: ", e);
        }
    }
}
