package com.console.ticket.concurrency;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ConcurrencyRunner {
    private static int threadsQuantity = 3;
    private static int queueCapacity = 20;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsQuantity);
        BlockingQueue<Future<Request>> requestsFromClientQueue = new LinkedBlockingQueue<>(queueCapacity);
        BlockingQueue<Future<Response>> responsesFromServerQueue = new LinkedBlockingQueue<>(queueCapacity);

        CopyOnWriteArrayList<Integer> integerList = getIntegerQueue(queueCapacity);
        CopyOnWriteArrayList<Integer> emptyList = getIntegerQueue(0);

        Client client = new Client(integerList);
        Server server = new Server(emptyList);

        while (queueCapacity > 0) {
            Future<Request> requestFuture = threadPool.submit(client::call);
            requestsFromClientQueue.add(requestFuture);
            queueCapacity--;
        }

        while (!requestsFromClientQueue.isEmpty()) {
            Future<Request> maybeDone = requestsFromClientQueue.poll();

            try {
                if (maybeDone.isDone()) {
                    Request requestFromClient = maybeDone.get();
                    threadPool.submit(() -> server.addElement(requestFromClient));

                    Future<Response> responseFuture = threadPool.submit(server::getCurrentSize);
                    responsesFromServerQueue.add(responseFuture);
                } else {
                    requestsFromClientQueue.add(maybeDone);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        while (!responsesFromServerQueue.isEmpty()) {
            Future<Response> maybeResponse = responsesFromServerQueue.poll();
            try {
                if (maybeResponse.isDone()) {
                    Response responseFromServer = maybeResponse.get();
                    threadPool.submit(() -> client.calculateAccumulator(responseFromServer));
                } else {
                    responsesFromServerQueue.add(maybeResponse);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1L, TimeUnit.MINUTES);

        System.out.println("Accumulator = " + client.getAccumulator());
    }

    private static CopyOnWriteArrayList<Integer> getIntegerQueue(int size) throws InterruptedException {
        CopyOnWriteArrayList<Integer> integerQueue = new CopyOnWriteArrayList<>(new Integer[size]);

        IntStream.rangeClosed(0, size-1)
                .forEach(index -> integerQueue.set(index, index + 5));

        return integerQueue;
    }
}
