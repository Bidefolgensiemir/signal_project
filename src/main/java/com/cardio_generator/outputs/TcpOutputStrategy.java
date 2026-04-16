package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
/**
 * @author Hanna Serafin
 * mplementation of {@code OutputStrategy} that sends data over TCP network
 * class starts a server on a specified port and waits for a client to connect 
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
/**
 * initializes a TCP server on a specific port 
 * to prevent blocking main simulator thread the connection acceptance is handled not synchtonically 
 * @param port the port number to listen on
 * @throws IOException if the server cant be open on a given port
 */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * sends patient's data to connected client as a comma separated string
 * data is dropped if no client is connected 
 * @param patientId unique id for each patient (positive integer)
 * @param timestamp time when the data was generated in miliseconds
 * @param label category of data
 * @param data specific value to record
 */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
