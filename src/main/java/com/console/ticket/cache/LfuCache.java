package com.console.ticket.cache;

import java.util.HashMap;
import java.util.Map;

class LfuCache implements Cache {
    Map<Integer, ListNode> keyMap = new HashMap<>();
    Map<Integer, DoublyList> freqMap = new HashMap<>();
    int DEFAULT_CAPACITY = 3;
    int curCapacity = 0;
    int maxCapacity;

    public LfuCache(int capacity) {
        if (capacity > 0) {
            this.maxCapacity = capacity;
        } else maxCapacity = DEFAULT_CAPACITY;
    }

    private ListNode getNode(int key) {
        if (!keyMap.containsKey(key))
            return null;
        // Retrive current node
        ListNode curNode = keyMap.get(key);

        // Remove curNode from current freq list
        DoublyList list = freqMap.get(curNode.freq);
        list.deleteNode(key);

        // Update the freq of current node
        curNode.freq++;

        // Add curNode onto a higher freq list
        if (!freqMap.containsKey(curNode.freq)) {
            freqMap.put(curNode.freq, new DoublyList());
        }
        freqMap.get(curNode.freq).addNode(curNode);
        return curNode;
    }

    public <T> T get(int key) {
        if (!keyMap.containsKey(key))
            return null;
        // Retrive current node from current freq list
        ListNode curNode = getNode(key);
        return (T) curNode.value;
    }

    @Override
    public void delete(int key) {
        ListNode node = keyMap.get(key);
        DoublyList list = freqMap.get(node.freq);
        list.deleteNode(key);
        keyMap.remove(key);

        curCapacity--;
    }

    public <T> void put(int key, T value) {
        if (maxCapacity == 0) {
            return;
        }
        // Update value
        if (keyMap.containsKey(key)) {
            // Retrieve current node from current freq list
            ListNode curNode = getNode(key);
            curNode.value = value;
        } else {
            // Insert value (maybe adjust the size)
            if (curCapacity == maxCapacity) {
                int lowestFreq = Integer.MAX_VALUE;
                for (Integer freq : freqMap.keySet()) {
                    if (freqMap.get(freq).map.isEmpty())
                        continue;
                    lowestFreq = Math.min(lowestFreq, freq);
                }
                DoublyList list = freqMap.get(lowestFreq);
                ListNode curNode = list.deleteHead();
                keyMap.remove(curNode.key);
                curCapacity--;
            }
            int curFreq = 1;
            ListNode curNode = new ListNode(value, key);
            keyMap.put(key, curNode);
            if (!freqMap.containsKey(curFreq)) {
                freqMap.put(curFreq, new DoublyList());
            }
            freqMap.get(curFreq).addNode(curNode);
            curCapacity++;
        }
    }
}

class ListNode<T> {
    ListNode prev, next;
    int key, freq;
    T value;

    ListNode() {

    }

    ListNode(T val, int key) {
        this.value = val;
        this.key = key;
        this.freq = 1;
    }
}

class DoublyList {
    Map<Integer, ListNode> map = new HashMap<>();
    ListNode head, tail;

    public DoublyList() {
        head = new ListNode();
        tail = new ListNode();
        tail.prev = head;
        head.next = tail;
    }

    public void addNode(ListNode curNode) {
        ListNode tailPrev = tail.prev;
        tailPrev.next = curNode;
        curNode.prev = tailPrev;
        tail.prev = curNode;
        curNode.next = tail;

        map.put(curNode.key, curNode);
    }

    public ListNode deleteNode(int key) {
        if (!map.containsKey(key)) {
            return null;
        }
        ListNode curNode = map.get(key);
        ListNode prevNode = curNode.prev;
        ListNode nextNode = curNode.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        map.remove(key);
        return curNode;
    }

    public ListNode deleteHead() {
        if (head.next == tail) {
            return null;
        }
        ListNode firstNode = head.next;
        return deleteNode(firstNode.key);
    }
}