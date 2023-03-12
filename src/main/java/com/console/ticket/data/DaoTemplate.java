package com.console.ticket.data;

import com.console.ticket.annotation.Cached;
import com.console.ticket.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface DaoTemplate<T> {
    @Cached
    Optional<T> findById(Integer id) throws DatabaseException;
    @Cached
    void delete(Integer id) throws DatabaseException;
    @Cached
    Optional<T> save(T t) throws DatabaseException;
    @Cached
    void update(T t) throws DatabaseException;

    List<Optional<T>> findAll() throws DatabaseException;
}
