package com.console.ticket.service.proxy;

import com.console.ticket.annotation.Cached;
import com.console.ticket.cache.Cache;
import com.console.ticket.cache.CacheFactory;
import com.console.ticket.data.CardDao;
import com.console.ticket.data.ProductDao;
import com.console.ticket.data.ProductDaoTemplate;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProductDaoProxy implements InvocationHandler {
    private Cache cacheList;
    private ProductDaoTemplate productDao;

    public ProductDaoProxy(ProductDaoTemplate productDao) {
        this.cacheList = new CacheFactory().getCacheFromYml();
        this.productDao = productDao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cached.class)) {
            String methodName = method.getName();

            if (methodName.contains("findById") && args.length > 0) {
                Integer id = (Integer) args[0];
                Product product = cacheList.get(id);

                if (product == null) {
                    product = (Product) method.invoke(productDao, args);
                }

                cacheList.put(id, product);

                return product;
            } else if (methodName.contains("delete") && args.length > 0) {
                Integer id = (Integer) args[0];

                method.invoke(productDao, args);
                cacheList.delete(id);
            } else if (args.length > 0 && (methodName.contains("save") || methodName.contains("update"))) {
                Product product = (Product) args[0];

                method.invoke(productDao, args);
                cacheList.put(product.getId(), product);
            }
        }

        return method.invoke(productDao, args);
    }
}
