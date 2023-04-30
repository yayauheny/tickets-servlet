package com.console.ticket.service.cache;

public interface Cache {
    public abstract <T> T get(int key);

    public abstract <T> void put(int key, T value);

    public abstract void delete(int key);
}
