package com.console.ticket.service.proxy;

import com.console.ticket.annotation.Cached;
import com.console.ticket.service.cache.Cache;
import com.console.ticket.service.cache.CacheFactory;
import com.console.ticket.data.DaoTemplate;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.ParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Class that implements InvocationHandler for adapting original object to caching data.
 * <p>
 * Adapt original method of DaoTemplate instance to use caching for CRUD operations.
 * </p>
 */
public class CachingDaoInvocationHandler<T> implements InvocationHandler {

    private static final String YML_FILENAME = "cache.yml";
    private final Cache cacheList;
    private final DaoTemplate<T> daoTemplate;

    public CachingDaoInvocationHandler(DaoTemplate<T> daoTemplate) {
        this.cacheList = new CacheFactory().getCacheFromYml(YML_FILENAME);
        this.daoTemplate = daoTemplate;
    }

    /**
     * Adapt the original method to work with cached data.
     *
     * <p>This method adds caching for CRUD operations in methods
     * that have been marked for caching. Methods are marked for
     * caching using an annotation or other marker. By default, the
     * method uses LFU caching, but this can be configured in a
     * YAML configuration file.</p>
     *
     * @param proxy  The original object.
     * @param method The original method.
     * @param args   The original method's arguments.
     * @return The original object with modified method.
     * @throws DatabaseException if an error occurs while invoking the method.
     */

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws DatabaseException, InvocationTargetException, IllegalAccessException {
        if (method.isAnnotationPresent(Cached.class) && args.length > 0) {
            String methodName = method.getName();

            switch (methodName) {
                // return object if already exists in cache or get from database by ID
                case "findById" -> {
                    Object idArgument = args[0];
                    Integer id;
                    Object foundedObject;

                    if (idArgument != null) {
                        id = castToInteger(idArgument);
                        foundedObject = cacheList.get(id);

                        if (foundedObject == null) {
                            foundedObject = method.invoke(daoTemplate, args);
                        }
                        cacheList.put(id, foundedObject);

                        return foundedObject;
                    }
                }
                // delete object from cache by ID
                case "delete" -> {
                    Object idArgument = args[0];
                    Integer id;

                    if (idArgument != null) {
                        id = castToInteger(idArgument);
                        cacheList.delete(id);
                    }
                }
                // save received object into cache and database
                case "save" -> {
                    Object objectArgument = args[0];
                    Integer id;

                    if (objectArgument != null) {
                        objectArgument = method.invoke(daoTemplate, args);
                        id = getObjectFieldId(((Optional<?>) objectArgument).get());
                        cacheList.put(id, objectArgument);

                        return objectArgument;
                    }
                }
                // update object in cache
                case "update" -> {
                    Object objectArgument = args[0];
                    Integer id;

                    if (objectArgument != null) {
                        id = getObjectFieldId(objectArgument);
                        cacheList.put(id, Optional.of(objectArgument));
                    }
                }
            }
        }

        return method.invoke(daoTemplate, args);
    }

    private Integer getObjectFieldId(Object argument) {
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

    private Integer castToInteger(Object obj) {
        Integer id;

        try {
            id = (Integer) obj;
        } catch (ClassCastException e) {
            throw new ParseException("Exception parse id: ", e);
        }

        return id;
    }
}
