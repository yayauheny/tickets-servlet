package com.console.ticket.concurrency;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private int msRangeFrom = 100;
    private int msRangeTo = 1000;
    private ReentrantLock serverLock = new ReentrantLock(true);
    private List<Integer> receivedData;

    public Server(List<Integer> receivedData) {
        this.receivedData = receivedData;
    }

    public void addElement(Request request) {
        try {
            serverLock.lock();

            Integer elementFromRequest = request.getValue();
            long delayInMs = ThreadLocalRandom.current().nextInt(msRangeFrom, msRangeTo);

            Thread.currentThread().sleep(delayInMs);

            receivedData.add(elementFromRequest);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            serverLock.unlock();
        }
    }

    public Response getCurrentSize() {
        Integer currentSize;

        try {
            serverLock.lock();
            currentSize = receivedData.size();

        } finally {
            serverLock.unlock();
        }

        return new Response(currentSize);
    }
}
