package com.console.ticket.service.impl;

import com.console.ticket.data.CardDao;
import com.console.ticket.data.DaoTemplate;
import com.console.ticket.entity.Card;
import com.console.ticket.service.proxy.DaoProxy;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

public class CardServiceImpl implements DaoService<Card> {
    private CardDao cardDao;
    private DaoTemplate<Card> proxyInstance;

    public CardServiceImpl(CardDao cardDao) {
        this.cardDao = cardDao;

        proxyInstance = (DaoTemplate<Card>) Proxy.newProxyInstance(
                cardDao.getClass().getClassLoader(),
                cardDao.getClass().getInterfaces(),
                new DaoProxy(cardDao));
    }

    @Override
    public Optional<Card> findById(Integer id) {
        return proxyInstance.findById(id);
    }

    @Override
    public void delete(Integer id) {
        proxyInstance.delete(id);
    }

    @Override
    public Optional<Card> save(Card card) {
        return proxyInstance.save(card);
    }

    @Override
    public void update(Card card) {
        proxyInstance.update(card);
    }

    @Override
    public List<Optional<Card>> findAll() {
        return proxyInstance.findAll();
    }
}
