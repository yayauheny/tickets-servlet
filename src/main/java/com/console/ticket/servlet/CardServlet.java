package com.console.ticket.servlet;

import com.console.ticket.data.CardDao;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.impl.CardServiceImpl;
import com.console.ticket.util.ConnectionManager;
import com.console.ticket.util.ServletsUtil;
import com.console.ticket.util.SqlRequestsUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.console.ticket.util.ServletsUtil.*;

@WebServlet(value = "/cards")
public class CardServlet extends HttpServlet {

    private static final CardDao cardDao = CardDao.getInstance();
    private static final CardServiceImpl cardService = new CardServiceImpl(cardDao);
    private static final Gson gsonParser = new Gson();
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Exception read data from client: ";

//    static {
//        try {
//            DriverManager.registerDriver(new org.postgresql.Driver());
//        } catch (SQLException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput = DEFAULT_EXCEPTION_MESSAGE;
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_PLAINTEXT, HTTP_STATUS_NOT_FOUND);

        try (PrintWriter writer = resp.getWriter()) {
            try {
                if (req.getParameter("id") == null) {
                    List<Card> cardsPerPage = getCardsPerPage(req);

                    ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);
                    responseOutput = gsonParser.toJson(cardsPerPage);
                } else {
                    int cardIdFromReq = ServletsUtil.getIntegerParameterFromReq(req, "id");
                    responseOutput = findProductByIdAndGetResp(resp, cardIdFromReq);
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
                Optional<Card> maybeCardFromRequest = getCardFromReq(req);

                if (maybeCardFromRequest.isPresent()) {
                    responseOutput = saveCardAndGetResp(resp, maybeCardFromRequest.get());
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
                Optional<Card> maybeCardFromRequest = getCardFromReq(req);

                if (maybeCardFromRequest.isPresent()) {
                    responseOutput = updateCardAndGetResp(resp, maybeCardFromRequest.get());
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
                int cardIdFromReq = ServletsUtil.getIntegerParameterFromReq(req, "id");
                responseOutput = deleteCardByIdAndGetResp(resp, cardIdFromReq);
            } catch (NumberFormatException | DatabaseException e) {
                throw new InputException(responseOutput, e);
            } finally {
                writer.write(responseOutput);
            }
        }
    }

    private String saveCardAndGetResp(HttpServletResponse resp, Card cardFromRequest) throws DatabaseException {
        cardService.save(cardFromRequest);
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);

        return "Card %s was saved successfully".formatted(
                gsonParser.toJson(cardFromRequest));
    }

    private String deleteCardByIdAndGetResp(HttpServletResponse resp, int cardId) throws DatabaseException {
        cardService.delete(cardId);
        ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);

        return "Card with id = %s was deleted successfully".formatted(
                gsonParser.toJson(cardId));
    }

    private String findProductByIdAndGetResp(HttpServletResponse resp, int productId) throws DatabaseException {
        Optional<Card> maybeExistingCard = cardService.findById(productId);
        boolean cardExists = maybeExistingCard.isPresent();

        if (cardExists) {
            ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_BAD_REQUEST);
        }

        return cardExists ? gsonParser.toJson(maybeExistingCard.get())
                : "Card with id = %d not found".formatted(productId);
    }

    private boolean cardExists(Card card) {
        return cardService.findById(card.getId()).isPresent();
    }

    private String updateCardAndGetResp(HttpServletResponse resp, Card modifiedCard) throws DatabaseException {
        if (cardExists(modifiedCard)) {
            cardService.update(modifiedCard);
            ServletsUtil.configureResponse(resp, HTTP_CONTENT_TYPE_JSON, HTTP_STATUS_OK);
        } else {
            int productId = modifiedCard.getId();
            return "Product with id = %d not found".formatted(productId);
        }

        return gsonParser.toJson(modifiedCard);
    }

    private List<Card> getCardsPerPage(HttpServletRequest req) {
        int pageSize = getPageSize(req);
        int currentPage = getCurrentPage(req);
        int cardsOffset = currentPage * pageSize;

        return findCardsWithLimit(pageSize, cardsOffset);
    }

    private int getPageSize(HttpServletRequest req) {
        try {
            return ServletsUtil.getIntegerParameterFromReq(req, "page-size");
        } catch (NumberFormatException e) {
            return HTTP_DEFAULT_PAGE_SIZE;
        }
    }

    private int getCurrentPage(HttpServletRequest req) {
        try {
            return ServletsUtil.getIntegerParameterFromReq(req, "page");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private List<Card> findCardsWithLimit(int limit, int offset) {
        List<Card> cards = new ArrayList<>();

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_GET_LIMIT);
        ) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Card card = Card.builder()
                        .id(resultSet.getInt("id"))
                        .discountSize(resultSet.getDouble("discount"))
                        .build();
                cards.add(card);
            }

            resultSet.close();

            return cards;
        } catch (SQLException e) {
            throw new DatabaseException("Error get cards from database: " + e.getMessage());
        }
    }

    public static Optional<Card> getCardFromReq(HttpServletRequest req) throws NumberFormatException {
        int cardId = ServletsUtil.getIntegerParameterFromReq(req, "id");
        double discountSize = ServletsUtil.getDoubleParameterFromRequest(req, "discount-size");

        return Optional.ofNullable(Card.builder()
                .id(cardId)
                .discountSize(discountSize)
                .build());
    }
}
