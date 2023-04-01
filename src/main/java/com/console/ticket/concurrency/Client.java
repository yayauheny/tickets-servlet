package com.console.ticket.concurrency;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Client implements Callable<Request> {

    private final ReentrantLock removeLock = new ReentrantLock(true);
    private final AtomicInteger accumulator = new AtomicInteger(0);
    private ExecutorService clientExecutor;
    private List<Integer> sendData;
    private Queue<Future<Request>> requestsFromClientQueue;
    private Queue<Future<Response>> responsesFromServerQueue;

    public Client(List<Integer> sendData) {
        this.sendData = sendData;
    }

    public int getAccumulator() {
        return accumulator.get();
    }

    public void start(Server server, int threadsQuantity, int queueSize) {
        clientExecutor = Executors.newFixedThreadPool(threadsQuantity);
        requestsFromClientQueue = new LinkedBlockingQueue<>(queueSize);
        responsesFromServerQueue = new LinkedBlockingQueue<>(queueSize);

        putAllRequestsFromClient(queueSize, requestsFromClientQueue);
        putAllResponsesFromServer(server, requestsFromClientQueue, responsesFromServerQueue);
        executeResponsesFromServer(responsesFromServerQueue);

        clientExecutor.shutdown();
        try {
            clientExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeResponsesFromServer(Queue<Future<Response>> responsesQueue) {
        Stream.generate(responsesQueue::poll)
                .takeWhile(Objects::nonNull)
                .forEach(responseFuture -> {
                    try {
                        if (responseFuture.isDone()) {
                            Response responseFromServer = responseFuture.get();
                            clientExecutor.submit(() -> calculateAccumulator(responseFromServer));
                        } else {
                            responsesQueue.add(responseFuture);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void putAllRequestsFromClient(int queueSize, Queue<Future<Request>> requestsQueue) {
        IntStream.range(0, queueSize)
                .forEach(i -> {
                    Future<Request> requestFuture = clientExecutor.submit(this);
                    requestsQueue.add(requestFuture);
                });
    }

    private void putAllResponsesFromServer(Server server, Queue<Future<Request>> requestsQueue, Queue<Future<Response>> responsesQueue) {
        Stream.generate(requestsQueue::poll)
                .takeWhile(Objects::nonNull)
                .forEach(requestFuture -> {
                    try {
                        if (requestFuture.isDone()) {
                            Request requestFromClient = requestFuture.get();
                            clientExecutor.submit(() -> server.addElement(requestFromClient));

                            Future<Response> responseFuture = clientExecutor.submit(server::getCurrentSize);
                            responsesQueue.add(responseFuture);
                        } else {
                            requestsQueue.add(requestFuture);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
    }

    private Integer remove() {
        Integer removedElement;

        try {
            removeLock.lock();

            int deletableIndex = ThreadLocalRandom.current().nextInt(sendData.size());
            removedElement = sendData.remove(deletableIndex);
            return removedElement;
        } finally {
            removeLock.unlock();
        }
    }

    public void calculateAccumulator(Response response) {
        int receivedListSize = response.getListSize();
        accumulator.addAndGet(receivedListSize);
    }

    @Override
    public Request call() throws Exception {
        Integer elementToSend = remove();

        return new Request(elementToSend);
    }
}
