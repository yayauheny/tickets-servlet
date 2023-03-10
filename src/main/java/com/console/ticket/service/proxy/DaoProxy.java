package com.console.ticket.service.proxy;

import com.console.ticket.annotation.Cached;
import com.console.ticket.cache.Cache;
import com.console.ticket.cache.CacheFactory;
import com.console.ticket.data.DaoTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DaoProxy implements InvocationHandler {
    private Cache cacheList;
    private DaoTemplate cardDao;

    public DaoProxy(DaoTemplate cardDao) {
        this.cacheList = new CacheFactory().getCacheFromYml();
        this.cardDao = cardDao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cached.class)) {
            String methodName = method.getName();

            if (methodName.contains("findById") && args.length > 0) {
                Integer id = null;
                Object object = null;
                Object idArgument = args[0];

                if (idArgument != null) {
                    id = (Integer) idArgument.getClass().getDeclaredField("id").get(idArgument);
                    object = cacheList.get(id);
                    if (object == null) {
                        object = method.invoke(cardDao, args);
                    }
                }

                cacheList.put(id, object);

                return object;
            } else if (methodName.contains("delete") && args.length > 0) {
                Integer id;
                Object idArgument = args[0];

                if (idArgument != null) {
                    id = (Integer) idArgument.getClass().getDeclaredField("id").get(idArgument);
                    method.invoke(cardDao, args);
                    cacheList.delete(id);
                }

            } else if (args.length > 0 && (methodName.contains("save") || methodName.contains("update"))) {
                Integer id;
                Object idArgument = args[0];

                if (idArgument != null) {
                    id = (Integer) idArgument.getClass().getDeclaredField("id").get(idArgument);
                    method.invoke(cardDao, args);
                    cacheList.put(id, idArgument);
                }
            }
        }

        return method.invoke(cardDao, args);
    }
}
