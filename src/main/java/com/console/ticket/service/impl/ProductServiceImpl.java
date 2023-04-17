package com.console.ticket.service.impl;

import com.console.ticket.data.DaoTemplate;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.service.proxy.CachingDaoInvocationHandler;
import lombok.Data;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements DaoService<Product> {
    private ProductDao productDao;

    private DaoTemplate<Product> proxyInstance;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;

        proxyInstance = (DaoTemplate<Product>) Proxy.newProxyInstance(
                this.productDao.getClass().getClassLoader(),
                this.productDao.getClass().getInterfaces(),
                new CachingDaoInvocationHandler(productDao));
    }

    @Override
    public Optional<Product> findById(Integer id) throws DatabaseException {
        return proxyInstance.findById(id);
    }

    @Override
    public void delete(Integer id) throws DatabaseException {
        proxyInstance.delete(id);
    }

    @Override
    public Optional<Product> save(Product product) throws DatabaseException {
        return proxyInstance.save(product);
    }

    @Override
    public void update(Product product) throws DatabaseException {
        proxyInstance.update(product);
    }

    @Override
    public List<Optional<Product>> findAll() throws DatabaseException {
        return proxyInstance.findAll();
    }
}
