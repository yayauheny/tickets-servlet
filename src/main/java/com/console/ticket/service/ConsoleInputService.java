package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.CardDao;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.impl.CardServiceImpl;
import com.console.ticket.service.impl.ProductServiceImpl;
import com.console.ticket.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Обработка введенных данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleInputService {
    private static ConsoleInputService INSTANCE;

    public static ConsoleInputService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConsoleInputService();
        }
        return INSTANCE;
    }

    public void readConsole(Company company) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
//            createSqlTables();
            System.out.print(Constants.MENU_MESSAGE);
            String consoleInput;
            while (true) {
                consoleInput = bufferedReader.readLine();

                if (!consoleInput.contains("exit") && !consoleInput.contains(".txt")) {
                    Map<String, String> inputPairsMap = divideInputToStringMapBySeparators(consoleInput);
                    Card foundCard = findCard(inputPairsMap);
                    List<Product> foundProducts = findProducts(inputPairsMap);

                    printReceiptToConsole(company, foundCard, foundProducts);
                } else if (consoleInput.contains(".txt")) {
                    ReceiptBuilder.readReceipt(consoleInput);
                } else break;
            }
        } catch (InputException e) {
            System.out.println("Exception validate input data: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Exception create database: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception console input: " + e.getMessage());
        }
    }

    private static void printReceiptToConsole(Company company, Card foundCard, List<Product> foundProducts) {
        System.out.println(ReceiptBuilder.writeReceipt(company, foundCard, foundProducts));
    }

    private static HashMap<String, String> divideInputToStringMapBySeparators(String input) {
        String[] inputPairList = input.split(" ");

        return (HashMap<String, String>) Arrays.stream(inputPairList)
                .map(s -> s.split("-"))
                .collect(Collectors.toMap(s -> s[0].trim(), s -> s[1].trim()));
    }

    private static Card findCard(Map<String, String> stringMap) throws InputException {
        CardServiceImpl cardService = new CardServiceImpl(CardDao.getInstance());
        int cardNumber;

        try {
            cardNumber = stringMap.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase("card"))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(Integer::valueOf)
                    .orElseThrow();
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new InputException("Invalid card number: " + e.getMessage());
        }

        return cardService.findById(cardNumber)
                .orElse(Card.builder().discountSize(0D).build());
    }

    private static List<Product> findProducts(Map<String, String> stringMap) throws InputException {
        ProductServiceImpl productService = new ProductServiceImpl(ProductDao.getInstance());
        List<Product> productList = new ArrayList<>();

        try {
            stringMap.entrySet().stream()
                    .filter(entry -> Constants.isDigit.matcher(entry.getKey()).find() && Constants.isDigit.matcher(entry.getValue()).find())
                    .forEach(entry -> {
                        Integer id = Integer.valueOf(entry.getKey());
                        int quantity = Integer.parseInt(entry.getValue());
                        Product foundProduct = productService.findById(id).get();
                        foundProduct.setQuantity(quantity);

                        productList.add(foundProduct);
                    });
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new InputException("Invalid product id/quantity: ", e);
        }
        return productList;
    }

//    public static void createSqlTables() throws DatabaseException {
//        try (var connection = ConnectionManager.open();
//             var statement = connection.createStatement()) {
//            statement.execute(Constants.CREATE_TABLES);
//        } catch (SQLException e) {
//            throw new DatabaseException("Exception create table: ", e);
//        }
//    }
}
