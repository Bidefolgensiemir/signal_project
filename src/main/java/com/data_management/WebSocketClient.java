package com.data_management;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class WebSocketClient implements WebSocket.Listener {
    private WebSocket webSocket;
    private final String serverUri;


    //Reconnection Variables
    private boolean isConnecting = false;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 5000;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    

    public WebSocketClient(String serverUri) {
        this(serverUri, true);
    }

    WebSocketClient(String serverUri, boolean autoConnect) {
        this.serverUri = serverUri;
        if (autoConnect) {
            connect();
        }
    }


    public void connect(){
        if(isConnecting){
            return;
        }
        isConnecting = true;

        System.out.println("Attemting to connect to server:"+serverUri );
        try {
            HttpClient client = HttpClient.newHttpClient();
            this.webSocket = client.newWebSocketBuilder().buildAsync(URI.create(serverUri), this).join();
        } catch (Exception e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            isConnecting = false;
            scheduleReconnect();


        }
    }


    private void scheduleReconnect() {
    if (retryCount >= MAX_RETRIES){
        System.err.println("Max reconnection attempts reached. Giving up.");
            return;
    }
    retryCount++;
        System.out.println("Reconnecting in " + (RETRY_DELAY_MS / 1000) + 
                           " seconds... (Attempt " + retryCount + " of " + MAX_RETRIES + ")");

        // Wait 5 seconds, then call the connect() method again
        scheduler.schedule(this::connect, RETRY_DELAY_MS, TimeUnit.MILLISECONDS);
    } 

    public void disconnect() {
        if (this.webSocket != null) {
            // 1000 is the standard status code for a normal closure
            this.webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client disconnecting");
        }
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Received message: " + data);
        
        webSocket.request(1);

        return null;
    }
    @Override
    public void onOpen(WebSocket webSocket) {
       System.out.println("Connected to the server successfully!");
        WebSocket.Listener.super.onOpen(webSocket);
        isConnecting = false;
        retryCount = 0;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
       System.out.println("An error occurred: " + error.getMessage());
        WebSocket.Listener.super.onError(webSocket, error);
        isConnecting = false;
        
        // An error killed the connection! Try to reconnect.
        scheduleReconnect();
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
    System.out.println("System closed"+statusCode+"Reason"+reason);
    isConnecting = false;
        
        scheduleReconnect(); 
        return null;
     }


}


