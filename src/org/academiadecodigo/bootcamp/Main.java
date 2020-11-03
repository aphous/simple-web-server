package org.academiadecodigo.bootcamp;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        WebServer server = new WebServer();

        try {
            server.start();
        }
        catch (IOException e) {
            System.out.println("Error - " + e.getMessage());
        }
        finally {
            server.close();
        }
    }
}
