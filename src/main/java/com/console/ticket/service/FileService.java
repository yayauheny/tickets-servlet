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
import java.util.Comparator;

/**
 * Запись и чтение в/из файла
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileService {
    private static String ticketInputLog;

    public static void writeReceipt(String receipt) throws FileException {
        File file = Constants.DEFAULT_RECEIPT_PATH.toFile();
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

    public static void clearReceiptFolder(Path path) throws FileException {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new FileException("Exception while deleting receipt files: ", e);
        }
    }


    public static String parseFileToString(Path path) throws FileException {
        try {
            String receipt = Files.readString(path, StandardCharsets.UTF_8);
            return receipt;
        } catch (IOException e) {
            throw new FileException("Exception while parsing receipt file to String value: ", e);
        }
    }
}
