package com.console.ticket.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Callable {

    private ReentrantLock clientLock = new ReentrantLock(true);
    private AtomicInteger accumulator;
    private static List<Integer> sendData;

    public Client(List<Integer> sendData) {
        this.sendData = sendData;
    }

    public int getAccumulator() {
        return accumulator.get();
    }

    private Integer remove() {
        Integer removedElement;

        try {
            clientLock.lock();

            int deletableIndex = ThreadLocalRandom.current().nextInt(sendData.size());
            removedElement = sendData.remove(deletableIndex);
        } finally {
            clientLock.unlock();
        }

        return removedElement;
    }

    public void calculateAccumulator(Response response) {
        if (!clientLock.isLocked()) {
            try {
                clientLock.lock();

                int receivedListSize = response.getListSize();
                accumulator.addAndGet(receivedListSize);
            } finally {
                clientLock.unlock();
            }
        }
    }

    @Override
    public Request call() throws Exception {
        Integer elementToSend = remove();
        Request request = new Request(elementToSend);

        return request;
    }
}
