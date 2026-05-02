package com.data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Proxy;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

public class WebSocketClientTest {

    @Test
    void onTextRequestsNextMessage() {
        WebSocketClient client = new WebSocketClient("ws://localhost:8080", false);
        AtomicLong requestedMessages = new AtomicLong(0);
        WebSocket webSocket = createFakeWebSocket(requestedMessages);

        CompletionStage<?> result = client.onText(webSocket, "1,1714376789050,ECG,100.0", true);

        assertNull(result);
        assertEquals(1, requestedMessages.get());
    }

    @Test
    void onTextStoresCompletePatientMeasurement() {
        DataStorage storage = DataStorage.getInstance();
        WebSocketClient client = new WebSocketClient("ws://localhost:8080", storage, false);
        WebSocket webSocket = createFakeWebSocket(new AtomicLong(0));

        client.onText(webSocket, "91,1714376789050,Saturation,98.5", true);

        java.util.List<PatientRecord> records = storage.getRecords(91, 1714376789050L, 1714376789050L);
        assertEquals(1, records.size());
        assertEquals(98.5, records.get(0).getMeasurementValue());
        assertEquals("Saturation", records.get(0).getRecordType());
    }

    @Test
    void onTextWaitsForLastFragmentBeforeStoring() {
        DataStorage storage = DataStorage.getInstance();
        WebSocketClient client = new WebSocketClient("ws://localhost:8080", storage, false);
        WebSocket webSocket = createFakeWebSocket(new AtomicLong(0));

        client.onText(webSocket, "92,1714376789051,", false);
        client.onText(webSocket, "ECG,0.25", true);

        java.util.List<PatientRecord> records = storage.getRecords(92, 1714376789051L, 1714376789051L);
        assertEquals(1, records.size());
        assertEquals(0.25, records.get(0).getMeasurementValue());
        assertEquals("ECG", records.get(0).getRecordType());
    }

    private WebSocket createFakeWebSocket(AtomicLong requestedMessages) {
        return (WebSocket) Proxy.newProxyInstance(
                WebSocket.class.getClassLoader(),
                new Class<?>[] { WebSocket.class },
                (proxy, method, args) -> {
                    if ("request".equals(method.getName())) {
                        requestedMessages.addAndGet((long) args[0]);
                        return null;
                    }
                    if (CompletableFuture.class.equals(method.getReturnType())) {
                        return CompletableFuture.completedFuture(proxy);
                    }
                    if (boolean.class.equals(method.getReturnType())) {
                        return false;
                    }
                    if (String.class.equals(method.getReturnType())) {
                        return "";
                    }
                    return null;
                });
    }
}
