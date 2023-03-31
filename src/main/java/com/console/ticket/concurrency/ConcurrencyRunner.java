package com.console.ticket.concurrency;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ConcurrencyRunner {
    private static int threadsQuantity = 2;
    private static int queueCapacity = 20;

    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<Integer> integerList = getIntegerQueue(queueCapacity);
//        CopyOnWriteArrayList<Integer> emptyList = getIntegerQueue(0);

        Client client = new Client(integerList);
        Server server = new Server();

        client.start(server, threadsQuantity, queueCapacity);
        System.out.println(client.getAccumulator());
    }

    private static CopyOnWriteArrayList<Integer> getIntegerQueue(int size) throws InterruptedException {
        CopyOnWriteArrayList<Integer> integerQueue = new CopyOnWriteArrayList<>(new Integer[size]);

        IntStream.rangeClosed(0, size - 1)
                .forEach(index -> integerQueue.set(index, index + 5));

        return integerQueue;
    }
}
