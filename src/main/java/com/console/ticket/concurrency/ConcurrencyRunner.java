package com.console.ticket.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ConcurrencyRunner {
    private static int threadsQuantity = 8;
    private static int queueCapacity = 100;

    public static void main(String[] args){
        List<Integer> integerList = getIntegerQueue(queueCapacity);

        Client client = new Client(integerList);
        Server server = new Server();

        client.start(server, threadsQuantity, queueCapacity);
        System.out.println(client.getAccumulator());
    }

    private static List<Integer> getIntegerQueue(int size){
        ArrayList<Integer> integerQueue = new ArrayList<>();
        integerQueue.ensureCapacity(size);


        IntStream.range(0, size)
                .forEach(index -> integerQueue.add(index, index + 5));

        return integerQueue;
    }
}
