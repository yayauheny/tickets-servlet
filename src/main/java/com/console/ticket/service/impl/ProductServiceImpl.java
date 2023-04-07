package com.console.ticket.service.impl;

import com.console.ticket.data.DaoTemplate;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Product;
import com.console.ticket.service.proxy.CachingDaoInvocationHandler;

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
    public Optional<Product> findById(Integer id) {
        return proxyInstance.findById(id);
    }

    @Override
    public void delete(Integer id) {
        proxyInstance.delete(id);
    }

    @Override
    public Optional<Product> save(Product product) {
        return proxyInstance.save(product);
    }

    @Override
    public void update(Product product) {
        proxyInstance.update(product);
    }

    @Override
    public List<Optional<Product>> findAll() {
        return proxyInstance.findAll();
    }
}
