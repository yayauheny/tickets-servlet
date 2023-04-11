package com.console.ticket.servlet;

import com.console.ticket.data.CardDao;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.entity.Product;
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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.console.ticket.util.ServletsUtil.HTTP_CONTENT_TYPE_PDF;
import static com.console.ticket.util.ServletsUtil.HTTP_STATUS_OK;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {

    private static final Company companyEvroopt = new Company(
            "Evroopt", "Minsk, Kalvariyskaja 17, 1", Currency.USA.getCurrency());
    private static CardServiceImpl cardService = new CardServiceImpl(CardDao.getInstance());
    private static ProductServiceImpl productService = new ProductServiceImpl(ProductDao.getInstance());

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cardId = ServletsUtil.getIntegerParameterFromRequest(req, "cardId");
        List<Product> products = new ArrayList<>();
        String[] productsIdParams= req.getParameterValues("productId");
        String[] productsQuantityParams = req.getParameterValues("quantity");

        List<Integer> productIdList = Arrays.stream(productsIdParams)
                .map(Integer::parseInt)
                .toList();
        List<Integer> productQuantityList = Arrays.stream(productsQuantityParams)
                .map(Integer::parseInt)
                .toList();

        IntStream.range(0, productIdList.size())
                .forEach(counter -> {
                    Product product = productService.findById(productIdList.get(counter)).get();
                    product.setQuantity(productQuantityList.get(counter));
                    products.add(product);
                });

        Optional<Card> card = cardService.findById(cardId);

        if (card.isPresent()) {
            String s = ReceiptBuilder.writeReceipt(companyEvroopt, card.get(), products);
            File file = FileService.writeAndGetReceipt(s);

            ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PDF, HTTP_STATUS_OK);
            try (ServletOutputStream outputStream = resp.getOutputStream();
                 FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
