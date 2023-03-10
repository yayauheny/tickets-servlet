package com.console.ticket.service.proxy;

import com.console.ticket.annotation.Cached;
import com.console.ticket.cache.Cache;
import com.console.ticket.cache.CacheFactory;
import com.console.ticket.data.CardDao;
import com.console.ticket.data.CardDaoTemplate;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class CardDaoProxy implements InvocationHandler {
    private Cache cacheList;
    private CardDaoTemplate cardDao;

    public CardDaoProxy(CardDaoTemplate cardDao) {
        this.cacheList = new CacheFactory().getCacheFromYml();
        this.cardDao = cardDao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cached.class)) {
            String methodName = method.getName();

            if (methodName.contains("findById") && args.length > 0) {
                Integer id = (Integer) args[0];
                Card card = cacheList.get(id);

                if (card == null) {
                    card = (Card) method.invoke(cardDao, args);
                }

                cacheList.put(id, card);

                return card;
            } else if (methodName.contains("delete") && args.length > 0) {
                Integer id = (Integer) args[0];

                method.invoke(cardDao, args);
                cacheList.delete(id);
            } else if (args.length > 0 && (methodName.contains("save") || methodName.contains("update"))) {
                Card card = (Card) args[0];

                method.invoke(cardDao, args);
                cacheList.put(card.getCardNumber(), card);
            }
        }

        return method.invoke(cardDao, args);
    }
}
