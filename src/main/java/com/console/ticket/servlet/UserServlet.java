package com.console.ticket.servlet;

import com.console.ticket.data.UserDao;
import com.console.ticket.entity.Role;
import com.console.ticket.entity.User;
import com.console.ticket.service.impl.UserServiceImpl;
import com.console.ticket.util.JspHelper;
import com.console.ticket.util.ServletsUtil;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final UserDao userDao = UserDao.getInstance();
    private static final UserServiceImpl userService = new UserServiceImpl(userDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.findAll().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        req.setAttribute("users", users);

        req.getRequestDispatcher(JspHelper.getPath("users-management"))
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
        User userFromReq = getUserFromReq(req);
        userService.update(userFromReq);
        //to reload all users and show users-management page again
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userIdToDelete = ServletsUtil.getIntegerParameterFromReq(req, "user-id");
        userService.delete(userIdToDelete);
        //to reload all users and show users-management page again
        doGet(req, resp);
    }

    private User getUserFromReq(HttpServletRequest req) {
        return User.builder()
                .id(ServletsUtil.getIntegerParameterFromReq(req, "user-id"))
                .name(ServletsUtil.getStringParameterFromRequest(req, "name"))
                .email(ServletsUtil.getStringParameterFromRequest(req, "email"))
                .password(ServletsUtil.getStringParameterFromRequest(req, "password"))
                .role(Role.valueOf(ServletsUtil.getStringParameterFromRequest(req, "role")))
                .cardId(ServletsUtil.getIntegerParameterFromReq(req, "discount_card"))
                .build();
    }
}
