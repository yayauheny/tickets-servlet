package com.console.ticket.concurrency;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Callable {
    private int msRangeFrom = 100;
    private int msRangeTo = 900;
    private static final Random delayGenerator = new Random();
    private CopyOnWriteArrayList<Integer> receivedData;

    public Server(CopyOnWriteArrayList<Integer> receivedData) {
        this.receivedData = receivedData;
    }

    public void addElement(Request request) throws InterruptedException {
        Integer elementFromRequest = request.getValue();
        long delayInMs = delayGenerator.nextInt(msRangeTo) + msRangeFrom;

        Thread.currentThread().sleep(delayInMs);

        this.receivedData.add(elementFromRequest);
    }

    private Response getCurrentSize(){
        Integer currentSize = receivedData.size();

        return new Response(currentSize);
    }
    @Override
    public Object call() throws Exception {
        return getCurrentSize();
    }
}
