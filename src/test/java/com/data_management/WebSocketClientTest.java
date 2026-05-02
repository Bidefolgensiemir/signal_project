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

        CompletionStage<?> result = client.onText(webSocket, "1, 100.0, ECG, 1714376789050", true);

        assertNull(result);
        assertEquals(1, requestedMessages.get());
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
