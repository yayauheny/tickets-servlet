package com.console.ticket.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ConcurrencyRunner {

    private static int threadsQuantity = 3;
    private static int queueCapacity = 100;

    public static void main(String[] args){
        List<Integer> integerList = getIntegerQueue(queueCapacity);
        List<Integer> empty = getIntegerQueue(0);

        Client client = new Client(integerList);
        Server server = new Server(empty);

        client.start(server, threadsQuantity, queueCapacity);
        System.out.println(client.getAccumulator());
    }

    private static CopyOnWriteArrayList<Integer> getIntegerQueue(int size){
        CopyOnWriteArrayList<Integer> integerQueue = new CopyOnWriteArrayList<>();


        IntStream.range(0, size)
                .forEach(index -> integerQueue.add(index, index + 5));

        return integerQueue;
    }
}
