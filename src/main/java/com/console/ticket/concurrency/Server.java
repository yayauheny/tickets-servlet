package com.console.ticket.concurrency;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final int MS_RANGE_FROM = 1;
    private final int MS_RANGE_TO = 10;
    private final List<Integer> receivedData = new CopyOnWriteArrayList<Integer>();
    private ReentrantLock addElementLock = new ReentrantLock(true);
    private ReentrantLock getCurrentSizeLock = new ReentrantLock(true);

    public Server() {
    }

    public void addElement(Request request) {
        try {
            addElementLock.lock();

            Integer elementFromRequest = request.getValue();
            long delayInMs = ThreadLocalRandom.current().nextInt(MS_RANGE_FROM, MS_RANGE_TO);

            TimeUnit.MILLISECONDS.sleep(delayInMs);

            receivedData.add(elementFromRequest);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            addElementLock.unlock();
        }
    }

    public Response getCurrentSize() {
        Integer currentSize;

        try {
            getCurrentSizeLock.lock();
            currentSize = receivedData.size();
        } finally {
            getCurrentSizeLock.unlock();
        }

        return new Response(currentSize);
    }
}
