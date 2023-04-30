package com.console.ticket.service.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is a class with LFU implementation for caching data.
 *
 * <p>The LFU cache removes the least frequently used element,
 * taking into account both the frequency and recency of usage,
 * when the cache size exceeds its capacity.</p>
 */

public class LfuCache implements Cache {
    private final static int DEFAULT_CAPACITY = 3;
    private final Map<Integer, ListNode<Object>> keyToNodeMap = new HashMap<>();
    private final Map<Integer, DoublyLinkedList<Object>> freqToListMap = new HashMap<>();
    private final int maxCapacity;
    private int currentCapacity;

    public LfuCache(int capacity) {
        if (capacity > 0) {
            this.maxCapacity = capacity;
        } else {
            maxCapacity = DEFAULT_CAPACITY;
        }
    }

    @Override
    public <T> T get(int key) {
        if (!keyToNodeMap.containsKey(key)) {
            return null;
        }

        ListNode<T> currentNode = getNode(key);
        return Objects.requireNonNull(currentNode).value;
    }

    @Override
    public void delete(int key) {
        if (keyToNodeMap.containsKey(key)) {
            ListNode<Object> node = keyToNodeMap.get(key);
            DoublyLinkedList<Object> list = freqToListMap.get(node.freq);
            list.deleteNode(key);
            keyToNodeMap.remove(key);
            currentCapacity--;
        }
    }

    @Override
    public <T> void put(int key, T value) {
        if (maxCapacity == 0) {
            return;
        }

        if (keyToNodeMap.containsKey(key)) {
            ListNode<Object> currentNode = getNode(key);
            Objects.requireNonNull(currentNode).value = value;
        } else {
            if (currentCapacity == maxCapacity) {
                int lowestFreq = Integer.MAX_VALUE;

                for (Integer freq : freqToListMap.keySet()) {
                    if (freqToListMap.get(freq).map.isEmpty()) {
                        continue;
                    }
                    lowestFreq = Math.min(lowestFreq, freq);
                }

                DoublyLinkedList<Object> list = freqToListMap.get(lowestFreq);
                ListNode<Object> currentNode = list.deleteHead();
                keyToNodeMap.remove(currentNode.key);
                currentCapacity--;
            }

            int currentFreq = 1;
            ListNode<Object> currentNode = new ListNode<>(value, key);
            keyToNodeMap.put(key, currentNode);

            if (!freqToListMap.containsKey(currentFreq)) {
                freqToListMap.put(currentFreq, new DoublyLinkedList<>());
            }

            freqToListMap.get(currentFreq).addNode(currentNode);
            currentCapacity++;
        }
    }

    private <T> ListNode<T> getNode(int key) {
        if (!keyToNodeMap.containsKey(key)) {
            return null;
        }

        ListNode<T> currentNode = (ListNode<T>) keyToNodeMap.get(key);
        DoublyLinkedList<Object> list = freqToListMap.get(currentNode.freq);
        list.deleteNode(key);
        currentNode.freq++;
        if (!freqToListMap.containsKey(currentNode.freq)) {
            freqToListMap.put(currentNode.freq, new DoublyLinkedList<>());
        }
        freqToListMap.get(currentNode.freq).addNode((ListNode<Object>) currentNode);
        return currentNode;
    }

    static class ListNode<T> {
        ListNode<T> prev, next;
        int key, freq;
        T value;

        ListNode() {
        }

        ListNode(T value, int key) {
            this.value = value;
            this.key = key;
            this.freq = 1;
        }
    }

    static class DoublyLinkedList<T> {
        Map<Integer, ListNode<T>> map = new HashMap<>();
        ListNode<T> head, tail;

        public DoublyLinkedList() {
            head = new ListNode<>();
            tail = new ListNode<>();
            tail.prev = head;
            head.next = tail;
        }

        public void addNode(ListNode<T> curNode) {
            ListNode<T> tailPrev = tail.prev;
            tailPrev.next = curNode;
            curNode.prev = tailPrev;
            tail.prev = curNode;
            curNode.next = tail;

            map.put(curNode.key, curNode);
        }

        public ListNode<T> deleteNode(int key) {
            if (!map.containsKey(key)) {
                return null;
            }
            ListNode<T> curNode = map.get(key);
            ListNode<T> prevNode = curNode.prev;
            ListNode<T> nextNode = curNode.next;

            prevNode.next = nextNode;
            nextNode.prev = prevNode;

            map.remove(key);
            return curNode;
        }

        public ListNode<T> deleteHead() {
            if (head.next == tail) {
                return null;
            }
            ListNode<T> firstNode = head.next;
            return deleteNode(firstNode.key);
        }
    }
}
