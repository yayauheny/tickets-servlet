package service;


import constants.Constants;
import exception.FileException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Запись и чтение в/из файла
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileService {
    private static String ticketInputLog;

    public static void writeReceipt(String ticketOutputLog) {
        File file = Path.of(String.format("tickets/ticket%s.txt", Constants.CASHIER_NUMBER)).toFile();
        try {
            Files.write(Path.of(file.getPath()), ticketOutputLog.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(new FileException("Exception while writing to file"));
        }
    }

    public static void readReceipt(String inputPath) {
        try {
            ticketInputLog = Files.readString(Path.of(inputPath), StandardCharsets.UTF_8);
            System.out.println(ticketInputLog);
        } catch (IOException e) {
            throw new RuntimeException(new FileException("Exception while reading from file"));
        }
    }
}
