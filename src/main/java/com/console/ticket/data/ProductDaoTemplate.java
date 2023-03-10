package com.console.ticket.data;

import com.console.ticket.annotation.Cached;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface ProductDaoTemplate {

    Optional<Product> findById(Integer id) throws DatabaseException;

    void delete(Integer id) throws DatabaseException;

    Product save(Product product) throws DatabaseException;

    void update(Product product) throws DatabaseException;

    List<Optional<Product>> findAll() throws DatabaseException;
}
