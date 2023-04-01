package com.console.ticket.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static final int MS_RANGE_FROM = 1;
    private static final int MS_RANGE_TO = 10;
    private List<Integer> receivedData = new ArrayList<>();
    private ReentrantLock receivedDataLock = new ReentrantLock(true);

    public Server() {
    }

    public void addElement(Request request) {
        try {
            receivedDataLock.lock();

            Integer elementFromRequest = request.getValue();
            long delayInMs = ThreadLocalRandom.current().nextInt(MS_RANGE_FROM, MS_RANGE_TO);

            TimeUnit.MILLISECONDS.sleep(delayInMs);

            receivedData.add(elementFromRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            receivedDataLock.unlock();
        }
    }

    public Response getCurrentSize() {
        int currentSize;

        try {
            receivedDataLock.lock();
            currentSize = receivedData.size();
            return new Response(currentSize);
        } finally {
            receivedDataLock.unlock();
        }
    }
}
