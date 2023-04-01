package com.console.ticket.concurrency;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;


class ClientTest {
    private Client client;
    private Server server;
    private List<Integer> inputDataList;
    private List<Integer> emptyList;
    private static int listSize;
    private static int threadsQuantity;

    @BeforeAll
    static void setStaticData() {
        listSize = 100;
        threadsQuantity = 5;
    }

    @BeforeEach
    void initialize() {
        inputDataList = IntStream.range(0, listSize)
                .boxed()
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        emptyList = new CopyOnWriteArrayList<>();


        client = new Client(inputDataList);
        server = new Server(emptyList);
    }

    @DisplayName("assert that client send all the data to server")
    @Test
    void checkClientListSizeIsEmpty() {
        client.start(server, threadsQuantity, listSize);

        int expectedListSize = 0;
        int actualListSize = client.getSendDataSize();

        assertThat(actualListSize).isEqualTo(expectedListSize);
    }
}