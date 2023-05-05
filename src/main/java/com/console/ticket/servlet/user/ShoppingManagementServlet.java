package com.console.ticket.servlet.user;

import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.entity.Product;
import com.console.ticket.service.impl.ProductServiceImpl;
import com.console.ticket.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/shopping")
public class ShoppingManagementServlet extends HttpServlet {

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
        req.setAttribute("currency", companyEvroopt.getCurrency());
        req.setAttribute("products", products);
        req.getRequestDispatcher(JspHelper.getPath("user", "shopping"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/ticket?cardId")
                .forward(req, resp);
    }

    //int cardId = ServletsUtil.getIntegerParameterFromReq(req, "cardId");
    //                List<Integer> productIdList = getAllIntegerParamsFromReq(req, "productId");
    //                List<Integer> productQuantityList = getAllIntegerParamsFromReq(req, "quantity");
}
