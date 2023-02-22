package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.DataBase;
import com.console.ticket.entity.Product;
import com.console.ticket.util.DateTimeUtil;
import com.console.ticket.entity.Company;

import java.time.format.DateTimeFormatter;

public final class ReceiptBuilder {
    private static String RECEIPT;

    public static String buildReceipt(Company company, DataBase dataBase) {
        Constants.CASHIER_NUMBER++;

        StringBuilder stringBuilder = new StringBuilder()
                .append(String.format("%s%25s%n%s", Constants.OUTPUT_LINE, "CASH RECEIPT", Constants.OUTPUT_LINE))
                .append(String.format("%26s%n", company.getName()))
                .append(String.format("%s%n%n", company.getAddress()))
                .append("CASHIER №:" + Constants.CASHIER_NUMBER)
                .append(String.format(("%18s%-17s%n"), "Date: ", getCurrentDate()))
                .append(String.format(("%29s%-17s%n%s"), "Time: ", getCurrentTime(), Constants.OUTPUT_LINE))
                .append("QTY:  ")
                .append("DESCRIPTION:")
                .append(String.format("%13s", "PRICE: "))
                .append(String.format("%7s%n", "TOTAL"))
                .append(appendProducts(company, dataBase));

        RECEIPT = stringBuilder.toString();

        return RECEIPT;
    }

    private static String appendProducts(Company company, DataBase dataBase) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Product product : dataBase.getProductsList()) {
            if (product.getQuantity() > 0) {
                int quantity = product.getQuantity();
                Double price = product.getPrice();

                stringBuilder
                        .append(String.format("%-6d", quantity))
                        .append(String.format("%-18s", product.getName()))
                        .append(String.format("%s%-8.2f", Constants.CURRENCY, price));

                if (product.isDiscount() && quantity > Constants.DISCOUNT_AFTER) {
                    Constants.PRODUCT_DISCOUNT = calculateProductDiscount(price, quantity, company.getSalePercent());

                    stringBuilder
                            .append(String.format("%s%-4.2f%n", Constants.CURRENCY, (price * quantity)))
                            .append(String.format("%16s", "(discount)"))
                            .append(String.format("%18s%-6.2f%n", "-" + Constants.CURRENCY, Constants.PRODUCT_DISCOUNT));
                } else {
                    stringBuilder.append(String.format("%s%-4.2f%n", Constants.CURRENCY, (price * quantity)));
                }
                Constants.TOTAL_SUM += (price * quantity) - Constants.PRODUCT_DISCOUNT;
            }
        }

        Constants.CARD_DISCOUNT = calculateCardDiscount(Constants.TOTAL_SUM, dataBase.getCard().getDiscountSize());

        stringBuilder
                .append(Constants.OUTPUT_LINE + "\n")
                .append(String.format("BUYER ID: [%d]%n", dataBase.getCard().getCardNumber()))
                .append(String.format("discount: %24s%-6.2f%n", Constants.CURRENCY, Constants.CARD_DISCOUNT + Constants.PRODUCT_DISCOUNT))
                .append(String.format("TOTAL: %27s%-6.2f", Constants.CURRENCY, Constants.TOTAL_SUM - Constants.CARD_DISCOUNT));

        dataBase.getProductsList().forEach(product -> product.setQuantity(0));

        return stringBuilder.toString();
    }

    public static String writeReceipt(Company company, DataBase dataBase) {
        RECEIPT = buildReceipt(company, dataBase);
        FileService.writeReceipt(RECEIPT);
        Constants.TOTAL_SUM = 0;
        Constants.CARD_DISCOUNT = 0;
        Constants.PRODUCT_DISCOUNT = 0;
        return RECEIPT;
    }

    public static void readReceipt(String path) {
        FileService.readReceipt(path);
    }

    private static String getCurrentDate() {
        return DateTimeUtil.getCurrentDateTime(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static String getCurrentTime() {
        return DateTimeUtil.getCurrentDateTime(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private static double calculateProductDiscount(double productPrice, int quantity, double salePercent) {
        return productPrice / 100 * salePercent * quantity;
    }

    private static double calculateCardDiscount(double totalPrice, double discountSize) {
        return totalPrice * (discountSize / 100);
    }
}
