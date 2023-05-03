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
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final UserDao userDao = UserDao.getInstance();
    private static final UserServiceImpl userService = new UserServiceImpl(userDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String emailFromReq = ServletsUtil.getStringParameterFromRequest(req, "email");
        String passwordFromReq = ServletsUtil.getStringParameterFromRequest(req, "password");
        Optional<User> maybeUserFromReq = getUserFromReq(emailFromReq);

        if (maybeUserFromReq.isEmpty()) {
            setAttrUserNotExistsAndForwardReq(req, resp);
        } else {
            User userFromReq = maybeUserFromReq.get();
            boolean isPasswordValid = verifyUserPassword(userFromReq, passwordFromReq);

            setAttrInvalidPasswordOrForwardReq(userFromReq, isPasswordValid, req, resp);
        }
    }

    private void setAttrUserNotExistsAndForwardReq(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("userExists", Boolean.FALSE);
        req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
    }

    private void setAttrInvalidPasswordOrForwardReq(User user, boolean isPasswordValid, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isPasswordValid) {
            req.setAttribute("invalidPassword", Boolean.TRUE);
            req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
        } else {
            forwardToUserOrAdminPage(user, req, resp);
        }
    }

    private void forwardToUserOrAdminPage(User user, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (user.getRole()) {
            case ADMIN: {
                req.getRequestDispatcher(JspHelper.getPath("admin-actions")).forward(req, resp);
                break;
            }
            case USER: {
                req.getRequestDispatcher(JspHelper.getPath("user-actions")).forward(req, resp);
                break;
            }
        }
    }

    private boolean verifyUserPassword(User user, String password) {
        return (user.getPassword().equals(password));
    }

    private Optional<User> getUserFromReq(String email) {
        return userService.findByEmail(email);
    }
}
