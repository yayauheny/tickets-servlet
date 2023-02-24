package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.CardDAO;
import com.console.ticket.data.ProductDAO;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Company;
import com.console.ticket.exception.InputException;
import com.console.ticket.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Обработка введенных данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsoleInputService {

    public static void readConsole(Company company) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            createSqlTables();
            System.out.print(Constants.MENU_MESSAGE);
            String consoleInput;
            while (true) {
                consoleInput = bufferedReader.readLine();

                if (!consoleInput.contains("exit") && !consoleInput.contains(".txt")) {
                    Map<String, String> stringMap = divideInputToStringMapBySeparators(consoleInput);
                    Card foundCard = findCard(stringMap);
                    List<Product> foundProducts = findProducts(stringMap);

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
        int cardNumber;
        try {
            cardNumber = stringMap.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase("card"))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(Integer::valueOf)
                    .orElseThrow();
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new InputException("Invalid card/card number: " + e.getMessage());
        }

        return CardDAO.getInstance().findCardById(cardNumber)
                .orElse(Card.builder().cardNumber(Constants.CASHIER_NUMBER).discountSize(0D).build());
    }

    private static List<Product> findProducts(Map<String, String> stringMap) throws InputException {
        Pattern isDigit = Pattern.compile("\\d+");
        List<Product> productList = new ArrayList<>();

        try {
            stringMap.entrySet().stream()
                    .filter(entry -> isDigit.matcher(entry.getKey()).find() && isDigit.matcher(entry.getValue()).find())
                    .forEach(entry -> {
                        Integer id = Integer.valueOf(entry.getKey());
                        int quantity = Integer.parseInt(entry.getValue());
                        Product foundProduct = ProductDAO.getInstance().findProductByIdAndSetQuantity(id, quantity).get();
                        productList.add(foundProduct);
                    });
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new InputException("Invalid product id/quantity: ", e);
        }
        return productList;
    }

    private static void createSqlTables() throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            statement.execute(Constants.CREATE_TABLES);
        } catch (SQLException e) {
            throw new DatabaseException("Exception create table: ", e);
        }
    }
}
