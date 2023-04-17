package com.console.ticket.service.impl;

import com.console.ticket.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface DaoService<T> {
    Optional<T> findById(Integer id) throws DatabaseException;

    void delete(Integer id) throws DatabaseException;

    Optional<T> save(T t) throws DatabaseException;

    void update(T t) throws DatabaseException;

    List<Optional<T>> findAll() throws DatabaseException;
}
