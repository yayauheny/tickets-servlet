package com.console.ticket.servlet;

import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.impl.ProductServiceImpl;
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
            Integer id = Integer.parseInt(req.getParameter("id"));

            Optional<Product> product = productService.findById(id);

            Gson parser = new Gson();
            if (product.isPresent()) {
                resp.setContentType("application/json");
                responseOutput = parser.toJson(product);
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
            int id = Integer.parseInt(req.getParameter("id"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            double price = Double.parseDouble(req.getParameter("price"));
            boolean discount = Boolean.getBoolean(req.getParameter("discount"));
            String name = req.getParameter("name");

            Optional<Product> product = productService.save(Product.builder()
                    .id(id)
                    .name(name)
                    .quantity(quantity)
                    .price(price)
                    .isDiscount(discount)
                    .build());

            Gson parser = new Gson();
            if (product.isPresent()) {
                responseOutput = "Product %s was saved successfully".formatted(parser.toJson(product));

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
            int id = Integer.parseInt(req.getParameter("id"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            double price = Double.parseDouble(req.getParameter("price"));
            boolean discount = Boolean.getBoolean(req.getParameter("discount"));
            String name = req.getParameter("name");

            Optional<Product> maybeProduct = productService.findById(id);

            Gson parser = new Gson();
            if (maybeProduct.isPresent()) {
                Product updatedProduct = Product.builder()
                        .id(id)
                        .name(name)
                        .quantity(quantity)
                        .price(price)
                        .isDiscount(discount)
                        .build();
                productService.update(updatedProduct);

                responseOutput = "Product %s was updated successfully".formatted(parser.toJson(updatedProduct));

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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;
        resp.setContentType("text/plain");

        try (PrintWriter writer = resp.getWriter()) {
            int id = Integer.parseInt(req.getParameter("id"));

            try {
                productService.delete(id);

                responseOutput = "Product with id = %d was deleted successfully".formatted(id);
                resp.setStatus(200);
            } catch (DatabaseException e) {
                responseOutput = "e.getMessage()";
                resp.setStatus(400);
            }

            writer.write(responseOutput);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
