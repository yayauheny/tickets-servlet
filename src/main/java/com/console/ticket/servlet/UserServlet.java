package com.console.ticket.servlet;

import com.console.ticket.data.UserDao;
import com.console.ticket.entity.User;
import com.console.ticket.service.impl.UserServiceImpl;
import com.console.ticket.util.JspHelper;
import com.console.ticket.util.ServletsUtil;
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
        List<User> users = userService.findAll().stream().map(Optional::get).collect(Collectors.toList());
        req.setAttribute("users", users);
        req.getRequestDispatcher(JspHelper.getPath("users-management"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodParam = "";
        if (!((methodParam = req.getParameter("_method")).isEmpty())) {
            if (methodParam.equalsIgnoreCase("delete")) {
                doDelete(req, resp);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userIdToDelete = ServletsUtil.getIntegerParameterFromReq(req, "user");
        userService.delete(userIdToDelete);
        doGet(req, resp);
    }
}
