package com.console.ticket.servlet;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.CardDao;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.FileService;
import com.console.ticket.service.ReceiptBuilder;
import com.console.ticket.service.impl.CardServiceImpl;
import com.console.ticket.service.impl.ProductServiceImpl;
import com.console.ticket.util.ServletsUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.console.ticket.util.ServletsUtil.*;
import static com.console.ticket.util.ServletsUtil.HTTP_STATUS_NOT_FOUND;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {

    private static final Company companyEvroopt = new Company(
            "Evroopt", "Minsk, Kalvariyskaja 17, 1", Currency.USA.getCurrency());
    private static final CardServiceImpl cardService = new CardServiceImpl(CardDao.getInstance());
    private static final ProductServiceImpl productService = new ProductServiceImpl(ProductDao.getInstance());

    private static final String DEFAULT_EXCEPTION_MESSAGE = "Exception build ticket: ";

//    static {
//        try {
//            DriverManager.registerDriver(new org.postgresql.Driver());
//        } catch (SQLException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_NOT_FOUND);

        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            try {
                File ticketPdf;
                int cardId = ServletsUtil.getIntegerParameterFromReq(req, "cardId");
                List<Integer> productIdList = getAllIntegerParamsFromReq(req, "productId");
                List<Integer> productQuantityList = getAllIntegerParamsFromReq(req, "quantity");

                deleteInvalidProductsFromReq(productIdList, productQuantityList);
                List<Product> products = getExistingProductsFromReq(productIdList, productQuantityList);

                Optional<Card> maybeCard = cardService.findById(cardId);

                if (maybeCard.isEmpty()) {
                    Card card = Card.builder()
                            .id(Constants.INCREMENTED_CASHIER_NUMBER)
                            .discountSize(0D)
                            .build();
                    String receipt = ReceiptBuilder.writeReceipt(companyEvroopt, card, products);
                    ticketPdf = FileService.writeAndGetReceipt(receipt);
                } else {
                    String ticket = ReceiptBuilder.writeReceipt(companyEvroopt, maybeCard.get(), products);
                    ticketPdf = FileService.writeAndGetReceipt(ticket);
                }

                writePdfFileToResponse(resp, outputStream, ticketPdf);
            } catch (IOException e) {
                outputStream.write(DEFAULT_EXCEPTION_MESSAGE.getBytes(StandardCharsets.UTF_8));
                throw new InputException(DEFAULT_EXCEPTION_MESSAGE, e);
            }
        }
    }

    private void writePdfFileToResponse(HttpServletResponse resp,ServletOutputStream outputStream, File ticketPdf) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PDF, HTTP_STATUS_OK);

        try (FileInputStream inputStream = new FileInputStream(ticketPdf)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            resp.setContentLength(buffer.length);
        }
    }

    private List<Product> getExistingProductsFromReq(List<Integer> idList, List<Integer> quantityList) {
        List<Product> products = new ArrayList<>();

        IntStream.range(0, idList.size())
                .forEach(index -> {
                    int productId = idList.get(index);
                    int quantity = quantityList.get(index);
                    Optional<Product> maybeExistingProduct = productService.findById(productId);

                    if (maybeExistingProduct.isPresent()) {
                        Product existingProduct = maybeExistingProduct.get();
                        existingProduct.setQuantity(quantity);

                        products.add(existingProduct);
                    }
                });

        return products;
    }

    private void deleteInvalidProductsFromReq(List<Integer> idList, List<Integer> quantityList) {
        int productsWithQuantity = Math.min(idList.size(), quantityList.size());

        IntStream.range(productsWithQuantity, idList.size())
                .forEach(idList::remove);

        IntStream.range(productsWithQuantity, quantityList.size())
                .forEach(quantityList::remove);
    }

    private List<Integer> getAllIntegerParamsFromReq(HttpServletRequest req, String parameter) {
        return Arrays.stream(req.getParameterValues(parameter))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
