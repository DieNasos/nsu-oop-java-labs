package org.belog.poopalkombat.model.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.belog.poopalkombat.model.Model;

public class TCPServer implements Runnable {

    // connection
    private ServerSocket serverSocket;
    private final int port;

    Model model;    // model that this server belongs to

    public TCPServer(int port, Model model) {
        this.port = port;
        this.model = model;
    }

    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    @Override
    public void run() {
        try {
            // creating new server socket
            serverSocket = new ServerSocket(port);
            System.out.println("Creating new server socket on port " + port);

            while (true)
            {
                // waiting for opponent to join
                Socket clientSocket = serverSocket.accept();   // accepting connection
                System.out.println("Accepted connection from: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                // creating and starting thread for requests processing
                Thread t = new Thread(new RequestProcessor(clientSocket, model));
                System.out.println("Creating processor...");
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIP() {
        String ip = null;

        try {
            // getting current machine's ip address
            ip = InetAddress.getLocalHost().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ip;
    }

    public int getPort() { return port; }
}