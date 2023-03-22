package com.console.ticket.cache;

import java.util.HashMap;
/**
 * Least Recently Used (LRU) cache implementation for
 * storing, getting, deleting and updating data.
 * This class provides similar functionality to a collection,
 * but removes the least recently used element when the cache
 * size exceeds its capacity.
 */

public class LruCache implements Cache {
    private HashMap<Integer, LinkedNode> map;
    private int capacity, size, DEFAULT_CAPACITY = 3;

    private LinkedNode head, tail;

    public LruCache(int capacity) {
        map = new HashMap<>(capacity);
        if (capacity > 0) {
            this.capacity = capacity;
        } else this.capacity = DEFAULT_CAPACITY;

        head = new LinkedNode();
        tail = new LinkedNode();

        head.next = tail;
        tail.prev = head;
    }

    @Override
    public <T> T get(int key) {
        LinkedNode element = map.get(key);
        if (element == null) {
            return null;
        }
        delete(element);
        moveToHead(element);

        return (T) element.value;
    }

    @Override
    public <T> void put(int key, T value) {
        LinkedNode element = map.get(key);

        if (element != null) {
            element.value = value;
            delete(element);
            moveToHead(element);
        } else {
            element = new LinkedNode();
            element.key = key;
            element.value = value;
            moveToHead(element);
            map.put(key, element);
            size++;

            if (size > capacity) {
                map.remove(tail.prev.key);
                delete(tail.prev);
                size--;
            }
        }

    }

    @Override
    public void delete(int key) {
        if (map.containsKey(key)) {
            LinkedNode node = map.get(key);
            delete(node);
            map.remove(key);
            size--;
        }
    }

    private void delete(LinkedNode element) {
        element.prev.next = element.next;
        element.next.prev = element.prev;
    }

    private void moveToHead(LinkedNode element) {
        LinkedNode temp = head.next;
        head.next = element;

        element.prev = head;
        element.next = temp;

        temp.prev = element;
    }


    class LinkedNode<T> {
        private int key;
        T value;
        private LinkedNode next;
        private LinkedNode prev;
    }
}