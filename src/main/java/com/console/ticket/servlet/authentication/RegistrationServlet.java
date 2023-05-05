package com.console.ticket.servlet.authentication;

import com.console.ticket.data.UserDao;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.entity.Role;
import com.console.ticket.entity.User;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.service.impl.UserServiceImpl;
import com.console.ticket.util.JspHelper;
import com.console.ticket.util.ServletsUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static com.console.ticket.entity.Role.ADMIN;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private static final Company companyEvroopt = new Company(
            "Evroopt", "Minsk, Kalvariyskaja 17, 1", Currency.USA.getCurrency());
    private static final UserDao userDao = UserDao.getInstance();
    private static final UserServiceImpl userService = new UserServiceImpl(userDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("authentication", "registration")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> maybeUserFromReq = getUserFromReqAndSave(req);

        if (maybeUserFromReq.isEmpty()) {
            setAttrIsInvalidAndForwardToAuthPage(req, resp);
        } else {
            User currentUser = maybeUserFromReq.get();

            HttpSession currentSession = req.getSession();
            currentSession.setAttribute("cardId", currentUser.getCardId());
            currentSession.setAttribute("currency", companyEvroopt.getCurrency());

            ServletsUtil.forwardToPageByRole(req, resp, currentUser);
        }
    }



    private void setAttrIsInvalidAndForwardToAuthPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("isValid", Boolean.FALSE);
        req.getRequestDispatcher(JspHelper.getPath("authentication", "registration")).forward(req, resp);
    }

    private Optional<User> getUserFromReqAndSave(HttpServletRequest req) {
        try {
            User userFromReq = User.builder()
                    .name(ServletsUtil.getStringParameterFromRequest(req, "name"))
                    .email(ServletsUtil.getStringParameterFromRequest(req, "email"))
                    .password(ServletsUtil.getStringParameterFromRequest(req, "password"))
                    .role(Role.valueOf(ServletsUtil.getStringParameterFromRequest(req, "role")))
                    .cardId(ServletsUtil.getIntegerParameterFromReq(req, "discount_card"))
                    .build();
            return userService.save(userFromReq);
        } catch (NumberFormatException | DatabaseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
