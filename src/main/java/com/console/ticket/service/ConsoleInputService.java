package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.DataBase;
import com.console.ticket.exception.DataBaseException;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Company;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработка введенных данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsoleInputService {

    public static void readConsole(Company company, DataBase dataBase) {
        System.out.print(Constants.MENU_MESSAGE);
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                line = bufferedReader.readLine();
                if (!line.contains("exit") && !line.contains(".txt")) {
                    dataBase.setCard(findCardAndProducts(line.split(" "), dataBase));
                    printReceiptToConsole(company, dataBase);
                } else if (line.contains(".txt")) {
                    ReceiptBuilder.readReceipt(line);
                } else break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printReceiptToConsole(Company company, DataBase dataBase) {
        System.out.println(ReceiptBuilder.writeReceipt(company, dataBase));
    }

    private static Card findCardAndProducts(String[] array, DataBase dataBase) {
        Map<String, String> stringMap = Arrays.stream(array)
                .map(s -> s.split("-"))
                .collect(Collectors.toMap(s -> s[0].trim(), s -> s[1].trim()));

        for (Map.Entry<String, String> map : stringMap.entrySet()) {
            try {
                if (map.getKey().equalsIgnoreCase("card")) {
                    dataBase.findCardById(Integer.parseInt(map.getValue()));

                    return dataBase.getCard();
                }

                int quantity = Integer.parseInt(map.getValue());
                dataBase.findProductById(Integer.parseInt(map.getKey()), quantity);

            } // TODO: refactor exception handling
            catch (NumberFormatException e) {
                throw new RuntimeException(new DataBaseException("Input data is invalid"));
            }
        }
        return dataBase.getCard();
    }
}
