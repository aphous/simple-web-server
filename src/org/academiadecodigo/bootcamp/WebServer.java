package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    private ServerSocket serverSocket;

    public void start() throws IOException {

        int port = getPort();

        serverSocket = new ServerSocket(port);
        ExecutorService service = Executors.newSingleThreadExecutor();

        while (true) {

            System.out.println("Waiting for connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println(serverSocket.getInetAddress() + " Connected");

            //Thread thread = new Thread(new WebClientHandler(clientSocket));
            //thread.start();
            service.submit(new WebClientHandler(clientSocket));
        }
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error - " + e.getMessage());
        }
    }


    public Integer getPort() throws IOException {

        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Port: 9999");
        return 9999;
        //return Integer.parseInt(reader.readLine());
    }

}
