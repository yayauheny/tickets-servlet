package com.console.ticket.concurrency;

import java.util.Random;
import java.util.concurrent.*;

public class Client implements Callable {
    private Random indexGenerator;
    private static CountDownLatch accumulatorAccessCounter = new CountDownLatch(1);
    private int accumulator;
    private static CopyOnWriteArrayList<Integer> sendData;

    public Client(CopyOnWriteArrayList<Integer> sendData) {
        this.sendData = sendData;
        this.indexGenerator = new Random(sendData.size());
    }

    private Request remove() {
        int deletableIndex = indexGenerator.nextInt(sendData.size());
        Integer removedElement = sendData.remove(deletableIndex);

        return new Request(removedElement);
    }

    public void calculateAccumulator(Response response) throws InterruptedException {
        accumulatorAccessCounter.await();

        int receivedListSize = response.getListSize();
        this.accumulator += receivedListSize;

        accumulatorAccessCounter.countDown();
    }

    @Override
    public Object call() throws Exception {
        return remove();
    }
}
