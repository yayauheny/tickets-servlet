package com.console.ticket.service.impl;

import com.console.ticket.data.CardDao;
import com.console.ticket.data.DaoTemplate;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import com.console.ticket.service.proxy.DaoProxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements DaoService<Product> {
    private ProductDao productDao;

    private DaoTemplate proxyInstance;

    public ProductServiceImpl() {
        productDao = ProductDao.getInstance();

        proxyInstance = (DaoTemplate) Proxy.newProxyInstance(
                DaoTemplate.class.getClassLoader(),
                new Class[]{DaoTemplate.class},
                new DaoProxy(productDao));
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
    public Product save(Product product) {
        return (Product) proxyInstance.save(product);
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
