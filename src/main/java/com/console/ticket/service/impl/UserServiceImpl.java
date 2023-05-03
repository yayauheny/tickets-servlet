package com.console.ticket.service.impl;

import com.console.ticket.data.CardDao;
import com.console.ticket.data.DaoTemplate;
import com.console.ticket.data.UserDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.User;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.service.proxy.CachingDaoInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements DaoService<User> {

    private final UserDao userDao;
    private final DaoTemplate<User> proxyInstance;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;

        proxyInstance = (DaoTemplate<User>) Proxy.newProxyInstance(
                this.userDao.getClass().getClassLoader(),
                this.userDao.getClass().getInterfaces(),
                new CachingDaoInvocationHandler<>(userDao));
    }

    @Override
    public Optional<User> findById(Integer id) throws DatabaseException {
        return proxyInstance.findById(id);
    }

    public Optional<User> findByEmail(String email) throws DatabaseException {
        return userDao.findByEmail(email);
    }


    @Override
    public void delete(Integer id) throws DatabaseException {
        proxyInstance.delete(id);
    }

    @Override
    public Optional<User> save(User user) throws DatabaseException {
        return proxyInstance.save(user);
    }

    @Override
    public void update(User user) throws DatabaseException {
        proxyInstance.update(user);
    }

    @Override
    public List<Optional<User>> findAll() throws DatabaseException {
        return proxyInstance.findAll();
    }
}
