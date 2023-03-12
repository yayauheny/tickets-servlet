package com.console.ticket.service.proxy;

import com.console.ticket.annotation.Cached;
import com.console.ticket.cache.Cache;
import com.console.ticket.cache.CacheFactory;
import com.console.ticket.data.DaoTemplate;
import com.console.ticket.exception.ParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

public class DaoProxy implements InvocationHandler {
    private final Cache cacheList;
    private final DaoTemplate cardDao;

    public DaoProxy(DaoTemplate cardDao) {
        this.cacheList = new CacheFactory().getCacheFromYml();
        this.cardDao = cardDao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cached.class) && args.length > 0) {
            String methodName = method.getName();
            switch (methodName) {
                case "findById": {
                    Object argument = args[0];
                    Integer id;
                    Object obj;
                    if (argument != null) {
                        id = getIntegerValue(argument);
                        obj = cacheList.get(id);

                        if (obj == null) {
                            obj = method.invoke(cardDao, args);
                        }
                        cacheList.put(id, obj);
                        return obj;
                    }
                    break;
                }
                case "delete": {
                    Object argument = args[0];
                    Integer id;

                    if (argument != null) {
                        id = getIntegerValue(argument);
                        cacheList.delete(id);
                    }
                    break;
                }
                case "save":{
                    Object obj = args[0];
                    Integer id;

                    if (obj != null) {
                        id = getIdField(obj);
                        obj = method.invoke(cardDao, args);
                        cacheList.put(id, obj);
                        return obj;
                    }
                    break;
                }
                case "update": {
                    Object argument = args[0];
                    Integer id;

                    if (argument != null) {
                        id = getIdField(argument);
                        cacheList.put(id, Optional.ofNullable(argument));
                    }
                    break;
                }
            }
        }
        return method.invoke(cardDao, args);
    }

    private Integer getIdField(Object argument) {
        Integer id;
        try {
            Field idField = argument.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            id = (Integer) idField.get(argument);
            idField.setAccessible(false);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private Integer getIntegerValue(Object obj) {
        Integer id;
        try {
            id = (Integer) obj;
        } catch (ClassCastException e) {
            throw new ParseException("Exception parse id: ", e);
        }
        return id;
    }
}
