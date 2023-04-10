package com.console.ticket.servlet;

import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.impl.ProductServiceImpl;
import com.console.ticket.util.ServletsUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(value = "/products")
public class ProductServlet extends HttpServlet {

    private static final ProductDao productDao = ProductDao.getInstance();
    private static final ProductServiceImpl productService = new ProductServiceImpl(productDao);
    public static final Gson gsonParser = new Gson();

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;

        try (PrintWriter writer = resp.getWriter()) {
            Integer id = ServletsUtil.getIntegerParameterFromRequest(req, "id");

            Optional<Product> product = productService.findById(id);

            if (product.isPresent()) {
                resp.setContentType("application/json");
                responseOutput = gsonParser.toJson(product);
                resp.setStatus(200);
            } else {
                resp.setContentType("text/plain");
                responseOutput = "Product with id = %d not found".formatted(id);
                resp.setStatus(404);
            }

            writer.write(responseOutput);
        } catch (NumberFormatException e) {
            throw new InputException("Exception read id from client: " + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;

        try (PrintWriter writer = resp.getWriter()) {
            int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");
            int quantity = ServletsUtil.getIntegerParameterFromRequest(req, "quantity");
            double price = ServletsUtil.getDoubleParameterFromRequest(req, "price");
            boolean discount = ServletsUtil.getBooleanParameterFromRequest(req, "discount");
            String name = ServletsUtil.getStringParameterFromRequest(req, "name");

            Optional<Product> product = productService.save(Product.builder()
                    .id(id)
                    .name(name)
                    .quantity(quantity)
                    .price(price)
                    .isDiscount(discount)
                    .build());

            if (product.isPresent()) {
                responseOutput = "Product %s was saved successfully".formatted(gsonParser.toJson(product));

                resp.setContentType("application/json");
                resp.setStatus(200);
            } else {
                responseOutput = "Data is invalid. Try again";

                resp.setContentType("text/plain");
                resp.setStatus(400);
            }

            writer.write(responseOutput);
        } catch (NumberFormatException e) {
            throw new InputException("Exception read data from client: " + e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;

        try (PrintWriter writer = resp.getWriter()) {
            Optional<Product> maybeProductFromRequest = getProductFromRequest(req);
            Optional<Product> maybeUpdatedProduct = Optional.empty();

            if (maybeProductFromRequest.isPresent()) {
                Product updatedProduct = updateAndGetProduct(maybeProductFromRequest.get());
                maybeUpdatedProduct = Optional.of(updatedProduct);
            }

            if (maybeUpdatedProduct.isPresent()) {
                responseOutput = configureResponse(resp, maybeUpdatedProduct.get());

            } else {
                resp.setContentType("text/plain");
                resp.setStatus(400);

                responseOutput = "Data is invalid. Try again";
            }

            writer.write(responseOutput);
        } catch (
                NumberFormatException e) {
            throw new InputException("Exception read data from client: " + e);
        }

    }

    private static String configureResponse(HttpServletResponse resp, Product updatedProduct) {
        String responseOutput;
        resp.setContentType("application/json");
        resp.setStatus(200);

        responseOutput = "Product %s was updated successfully".formatted(
                gsonParser.toJson(updatedProduct));
        return responseOutput;
    }

    private Product updateAndGetProduct(Product modifiedProduct) {
        int productId = modifiedProduct.getId();
        updateProductIfExists(modifiedProduct, productId);

        return modifiedProduct;
    }

    private static void updateProductIfExists(Product modifiedProduct, int existingProductId) {
        productService.findById(existingProductId).ifPresent(product -> productService.update(modifiedProduct));
    }

    private Optional<Product> getProductFromRequest(HttpServletRequest req) throws NumberFormatException {
        int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");
        int quantity = ServletsUtil.getIntegerParameterFromRequest(req, "quantity");
        double price = ServletsUtil.getDoubleParameterFromRequest(req, "price");
        boolean discount = ServletsUtil.getBooleanParameterFromRequest(req, "discount");
        String name = ServletsUtil.getStringParameterFromRequest(req, "name");

        return Optional.ofNullable(Product.builder()
                .id(id)
                .quantity(quantity)
                .price(price)
                .isDiscount(discount)
                .name(name)
                .build());
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;
        resp.setContentType("text/plain");

        try (PrintWriter writer = resp.getWriter()) {
            int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");

            productService.delete(id);

            responseOutput = "Product with id = %d was deleted successfully".formatted(id);
            resp.setStatus(200);

            writer.write(responseOutput);
        } catch (NumberFormatException | DatabaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
