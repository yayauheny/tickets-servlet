package com.console.ticket.servlet.admin;

import com.console.ticket.data.CardDao;
import com.console.ticket.entity.Card;
import com.console.ticket.service.impl.CardServiceImpl;
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

@WebServlet("/cards")
public class CardsManagementServlet extends HttpServlet {

    private static final CardDao cardDao = CardDao.getInstance();
    private static final CardServiceImpl cardService = new CardServiceImpl(cardDao);

    private static final ArrayList<Card> cards;

    static {
        cards = cardService.findAll().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cards", cards);

        req.getRequestDispatcher(JspHelper.getPath("admin", "cards-management"))
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
        Card cardFromReq = getCardFromReq(req);
        updateCardFromCardsList(cardFromReq, cards);
        cardService.update(cardFromReq);
        //to reload all cards and show cards-management page again
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cardIdToDelete = ServletsUtil.getIntegerParameterFromReq(req, "card-id");
        cards.removeIf(card -> card.getId() == cardIdToDelete);
        cardService.delete(cardIdToDelete);
        //to reload all cards and show cards-management page again
        doGet(req, resp);
    }

    private Card getCardFromReq(HttpServletRequest req) {
        return Card.builder()
                .id(ServletsUtil.getIntegerParameterFromReq(req, "card-id"))
                .discountSize(ServletsUtil.getDoubleParameterFromRequest(req, "discount-size"))
                .build();
    }

    private void updateCardFromCardsList(Card updatedCard, List<Card> cards) {
        int cardIdToReplace = updatedCard.getId();

        Optional<Card> oldCard = cards.stream()
                .filter(card -> card.getId() == cardIdToReplace)
                .findFirst();

        oldCard.ifPresent(oldCardFromList -> {
            int indexOfCard = cards.indexOf(oldCardFromList);
            cards.set(indexOfCard, updatedCard);
        });
    }
}
