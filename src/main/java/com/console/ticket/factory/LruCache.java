package com.console.ticket.factory;

import java.util.HashMap;


public class LruCache {
    private HashMap<Integer, LinkedNode> map;
    int capacity;
    int size;

    LinkedNode head;

    LinkedNode tail;

    public LruCache(int capacity) {
        map = new HashMap<>(capacity);
        this.capacity = capacity;
        head = new LinkedNode();
        tail = new LinkedNode();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        LinkedNode element = map.get(key);
        if (element == null) {
            return -1;
        }
        remove(element);
        moveToHead(element);
        return element.value;
    }

    public void put(int key, int value) {
        LinkedNode element = map.get(key);

        if (element != null) {
            element.value = value;
            remove(element);
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
                remove(tail.prev);
                size--;
            }
        }

    }

    private void remove(LinkedNode element) {
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


    class LinkedNode {
        int key;
        int value;
        LinkedNode next;
        LinkedNode prev;
    }
}
