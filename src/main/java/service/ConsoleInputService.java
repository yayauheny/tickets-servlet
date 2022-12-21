package service;

import constants.Constants;
import data.DataBase;
import entity.Card;
import entity.Company;
import exception.DataBaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработка введенных данных
 */

public final class ConsoleInputService {
    private ConsoleInputService() {
    }

    public static void readConsole(Company company, DataBase dataBase) {
        System.out.print(Constants.MENU_MESSAGE);
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                line = bufferedReader.readLine();
                if (!line.contains("exit") && !line.contains(".txt")) {
                    dataBase.setCard(findCardAndProducts(line.split(" "), dataBase));
                    getReceipt(company, dataBase);
                } else if (line.contains(".txt")) {
                    ReceiptService.readReceipt(line);
                } else break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void getReceipt(Company company, DataBase dataBase) {
        System.out.println(ReceiptService.writeReceipt(company, dataBase));
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

            } catch (NumberFormatException e) {
                throw new RuntimeException(new DataBaseException("Input data is invalid"));
            }
        }
        return dataBase.getCard();
    }
}
