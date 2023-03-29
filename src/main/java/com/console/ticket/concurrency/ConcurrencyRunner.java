package com.console.ticket.concurrency;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyRunner {
    private static int threadsQuantity = 5;
    private static int queueCapacity = 100;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService clientThreadPool = Executors.newFixedThreadPool(threadsQuantity);
        ExecutorService serverThreadPool = Executors.newFixedThreadPool(threadsQuantity);

        CopyOnWriteArrayList<Integer> integerList = getIntegerQueue(queueCapacity);
        CopyOnWriteArrayList<Integer> emptyList = getIntegerQueue(0);

        Client client = new Client(integerList);
        Server server = new Server(emptyList);
    }

    private static CopyOnWriteArrayList<Integer> getIntegerQueue(int size) throws InterruptedException {
        CopyOnWriteArrayList<Integer> integerQueue = new CopyOnWriteArrayList<>(new Integer[size]);

        for (int i = 0; i < size; i++) {
            integerQueue.add(i + 2);
        }

        return integerQueue;
    }


}
