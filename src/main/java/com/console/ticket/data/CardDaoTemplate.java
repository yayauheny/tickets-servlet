package com.console.ticket.data;

import com.console.ticket.annotation.Cached;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;

import java.util.List;
import java.util.Optional;

public interface CardDaoTemplate {
    <T>Optional<T> findById(Integer id) throws DatabaseException, InputException;

    void delete(Integer id) throws DatabaseException, InputException;

    Card save(Card card) throws DatabaseException;

    void update(Card card) throws DatabaseException;

    List<Optional<Card>> findAll() throws DatabaseException;
}
