package com.console.ticket.servlet;

import com.console.ticket.data.CardDao;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.service.impl.CardServiceImpl;
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
import java.util.Optional;

@WebServlet("/cards")
public class CardServlet extends HttpServlet {

    private static final CardDao cardDao = CardDao.getInstance();
    private static final CardServiceImpl cardService = new CardServiceImpl(cardDao);

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
            int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");

            Optional<Card> maybeCard = cardService.findById(id);

            Gson parser = new Gson();

            if (maybeCard.isPresent()) {
                resp.setContentType("application/json");
                responseOutput = parser.toJson(maybeCard.get());
                resp.setStatus(200);
            } else {
                resp.setContentType("text/plain");
                responseOutput = "Card with id = %d not found".formatted(id);
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
            try {
                double discountSize = ServletsUtil.getDoubleParameterFromRequest(req, "discount");

                Optional<Card> card = cardService.save(Card.builder()
                        .discountSize(discountSize)
                        .build());

                Gson parser = new Gson();

                if (card.isPresent()) {
                    resp.setContentType("application/json");
                    responseOutput = "Card %s was saved successfully".formatted(parser.toJson(card));

                    resp.setStatus(200);
                } else {
                    responseOutput = "Data is invalid. Try again.";
                    sendExceptionMessage(resp, writer, responseOutput);
                }

                writer.write(responseOutput);
            } catch (NumberFormatException e) {
                String message = "Data is invalid: ";
                sendExceptionMessage(resp, writer, message + e.getMessage());

                throw new InputException(message, e);
            } catch (DatabaseException e) {
                String message = "Can't save object: ";
                sendExceptionMessage(resp, writer, message + e.getMessage());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;

        try (PrintWriter writer = resp.getWriter()) {
            try {
                int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");
                double discountSize = ServletsUtil.getDoubleParameterFromRequest(req, "discount");

                Optional<Card> cardToUpdate = cardService.findById(id);

                Gson parser = new Gson();
                if (cardToUpdate.isPresent()) {
                    cardToUpdate.get().setDiscountSize(discountSize);

                    cardService.update(cardToUpdate.get());
                    responseOutput = "Card %s was updated successfully".formatted(parser.toJson(cardToUpdate.get()));

                    writer.write(responseOutput);

                    resp.setContentType("application/json");
                    resp.setStatus(200);
                } else {
                    responseOutput = "Data is invalid. Try again";
                    sendExceptionMessage(resp, writer, responseOutput);
                }

            } catch (NumberFormatException e) {
                throw new InputException("Exception read data from client: " + e);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseOutput;
        resp.setContentType("text/plain");

        try (PrintWriter writer = resp.getWriter()) {
            int id = ServletsUtil.getIntegerParameterFromRequest(req, "id");

            cardService.delete(id);

            responseOutput = "Card with id = %d was deleted successfully".formatted(id);
            resp.setStatus(200);

            writer.write(responseOutput);
        } catch (NumberFormatException | DatabaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void sendExceptionMessage(HttpServletResponse resp, PrintWriter writer, String message) {
        resp.setContentType("text/plain");
        writer.write(message);
        resp.setStatus(400);
    }
}
