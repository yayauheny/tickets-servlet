package com.console.ticket.service.impl;

import com.console.ticket.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface DaoService<T> {
    Optional<T> findById(Integer id);
    void delete(Integer id);

    T save(T t);

    void update(T t);

    List<Optional<T>> findAll();
}
