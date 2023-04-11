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
import java.util.List;
import java.util.Optional;

import static com.console.ticket.util.ServletsUtil.*;

@WebServlet(value = "/products")
public class ProductServlet extends HttpServlet {

    private static final ProductDao productDao = ProductDao.getInstance();
    private static final ProductServiceImpl productService = new ProductServiceImpl(productDao);
    private static final Gson gsonParser = new Gson();
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Exception read data from client: ";


    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput = DEFAULT_EXCEPTION_MESSAGE;
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_NOT_FOUND);

        try (PrintWriter writer = resp.getWriter()) {
            try {
                if (req.getParameter("id") == null) {
                    responseOutput = gsonParser.toJson(getAllProducts());
                    ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);
                } else {
                    int productIdFromReq = ServletsUtil.getIntegerParameterFromRequest(req, "id");
                    responseOutput = findProductByIdAndGetResp(resp, productIdFromReq);
                }
            } catch (NumberFormatException e) {
                throw new InputException(responseOutput, e);
            } finally {
                writer.write(responseOutput);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput = DEFAULT_EXCEPTION_MESSAGE;
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_BAD_REQUEST);

        try (PrintWriter writer = resp.getWriter()) {
            try {
                Optional<Product> maybeProductFromRequest = ServletsUtil.getProductFromReq(req);

                if (maybeProductFromRequest.isPresent()) {
                    responseOutput = saveProductAndGetResp(resp, maybeProductFromRequest.get());
                }
            } catch (NumberFormatException e) {
                throw new InputException(responseOutput, e);
            } finally {
                writer.write(responseOutput);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput = DEFAULT_EXCEPTION_MESSAGE;
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_BAD_REQUEST);

        try (PrintWriter writer = resp.getWriter()) {
            try {
                Optional<Product> maybeProductFromRequest = ServletsUtil.getProductFromReq(req);

                if (maybeProductFromRequest.isPresent()) {
                    responseOutput = updateProductAndGetResp(resp, maybeProductFromRequest.get());
                }
            } catch (NumberFormatException e) {
                throw new InputException(responseOutput, e);
            } finally {
                writer.write(responseOutput);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput = DEFAULT_EXCEPTION_MESSAGE;
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_BAD_REQUEST);

        try (PrintWriter writer = resp.getWriter()) {
            try {
                int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");
                responseOutput = deleteProductByIdAndGetResp(resp, id);
            } catch (NumberFormatException | DatabaseException e) {
                throw new InputException(responseOutput, e);
            } finally {
                writer.write(responseOutput);
            }
        }
    }

    private String saveProductAndGetResp(HttpServletResponse resp, Product productFromRequest) throws DatabaseException {
        productService.save(productFromRequest);
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);

        return "Product %s was saved successfully".formatted(
                gsonParser.toJson(productFromRequest));
    }

    private String deleteProductByIdAndGetResp(HttpServletResponse resp, int productId) throws DatabaseException {
        productService.delete(productId);
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);

        return "Product %s was deleted successfully".formatted(
                gsonParser.toJson(productId));
    }

    private String findProductByIdAndGetResp(HttpServletResponse resp, int productId) throws DatabaseException {
        Optional<Product> maybeExistingProduct = productService.findById(productId);
        boolean productExists = maybeExistingProduct.isPresent();

        if (productExists) {
            ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_BAD_REQUEST);
        }

        return productExists ? gsonParser.toJson(maybeExistingProduct.get())
                : "Product with id = %d not found".formatted(productId);
    }

    private boolean productExists(Product product) {
        return productService.findById(product.getId()).isPresent();
    }

    private List<Product> getAllProducts() throws DatabaseException {
        return productService.findAll().stream()
                .flatMap(Optional::stream)
                .toList();
    }

    private String updateProductAndGetResp(HttpServletResponse resp, Product modifiedProduct) throws DatabaseException {
        if (productExists(modifiedProduct)) {
            productService.update(modifiedProduct);
            ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);
        } else {
            int productId = modifiedProduct.getId();
            return "Product with id = %d not found".formatted(productId);
        }

        return gsonParser.toJson(modifiedProduct);
    }
}
