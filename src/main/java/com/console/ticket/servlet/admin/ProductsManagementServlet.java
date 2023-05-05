package com.console.ticket.servlet.admin;

import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.entity.Product;
import com.console.ticket.service.impl.ProductServiceImpl;
import com.console.ticket.util.JspHelper;
import com.console.ticket.util.ServletsUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/products")
public class ProductsManagementServlet extends HttpServlet {
    private static final Company companyEvroopt = new Company(
            "Evroopt", "Minsk, Kalvariyskaja 17, 1", Currency.USA.getCurrency());
    private static final ProductDao productDao = ProductDao.getInstance();
    private static final ProductServiceImpl productService = new ProductServiceImpl(productDao);

    private static final ArrayList<Product> products;

    static {
        products = productService.findAll().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("products", products);
        req.setAttribute("currency", companyEvroopt.getCurrency());

        req.getRequestDispatcher(JspHelper.getPath("admin", "products-management"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodParam;

        try {
            methodParam = ServletsUtil.getStringParameterFromRequest(req, "_method");
        } catch (NumberFormatException e) {
            methodParam = "null";
        }

        switch (methodParam) {
            case "delete" -> doDelete(req, resp);
            case "put" -> doPut(req, resp);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product productFromReq = getProductFromReq(req);
        updateProductFromProductsList(productFromReq, products);
        productService.update(productFromReq);
        //to reload all products and show products-management page again
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productIdToDelete = ServletsUtil.getIntegerParameterFromReq(req, "product-id");
        products.removeIf(product -> product.getId() == productIdToDelete);
        productService.delete(productIdToDelete);
        //to reload all products and show products-management page again
        doGet(req, resp);
    }

    private Product getProductFromReq(HttpServletRequest req) {
        return Product.builder()
                .id(ServletsUtil.getIntegerParameterFromReq(req, "product-id"))
                .name(ServletsUtil.getStringParameterFromRequest(req, "name"))
                .quantity(ServletsUtil.getIntegerParameterFromReq(req, "quantity"))
                .price(ServletsUtil.getDoubleParameterFromRequest(req, "price"))
                .isDiscount(ServletsUtil.getBooleanParameterFromRequest(req, "discount"))
                .build();
    }

    private void updateProductFromProductsList(Product updatedProduct, List<Product> products) {
        int productIdToReplace = updatedProduct.getId();

        Optional<Product> oldProduct = products.stream()
                .filter(product -> product.getId() == productIdToReplace)
                .findFirst();

        oldProduct.ifPresent(oldProductFromList -> {
            int indexOfProduct = products.indexOf(oldProductFromList);
            products.set(indexOfProduct, updatedProduct);
        });
    }
}
