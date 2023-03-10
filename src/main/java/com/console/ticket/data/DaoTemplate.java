package com.console.ticket.data;

import com.console.ticket.annotation.Cached;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;

import java.util.List;
import java.util.Optional;

public interface DaoTemplate<T> {
    Optional<T> findById(Integer id) throws DatabaseException;

    void delete(Integer id) throws DatabaseException;

    T save(T t) throws DatabaseException;

    void update(T t) throws DatabaseException;

    List<Optional<T>> findAll() throws DatabaseException;
}
