package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;


    public static void main(String[] args) {

        WebServer server = new WebServer();
        String[] requestMessage;
        String fileName = "resources";


        try {

            int port = server.getPort();

            serverSocket = new ServerSocket(port);

            System.out.println("Waiting for connection...");
            clientSocket = serverSocket.accept();
            System.out.println(serverSocket.getInetAddress() + " Connected");


            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            requestMessage = in.readLine().split(" ");

            if (requestMessage[1].equals("/")) {
                fileName = fileName + "/Home.html";
            } else {
                fileName = fileName + requestMessage[1];
            }

            File file = new File(fileName);

            if(file.exists()) {
                server.sendHeader(fileName);
                server.sendFile(file, clientSocket);
            } else {
                server.fileNotFound();
            }

        } catch (IOException e) {
            System.out.println("Error - " + e.getMessage());
        }

    }


    public void fileNotFound(){
        out.write("HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: <file_byte_size> \r\n" + "\r\n");
        out.flush();
    }

    public void sendHeader (String filename){

        if(filename.endsWith(".html")){
            out.write("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: <file_byte_size> \r\n" + "\r\n");
            out.flush();
        } else {
            out.write("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: image;<image_file_extension>\r\n" +
                    "Content-Length: <file_byte_size> \r\n" + "\r\n");
            out.flush();
        }
    }

    public void sendFile (File file, Socket clientSocket) throws IOException {

        DataOutputStream outData = new DataOutputStream(clientSocket.getOutputStream());
        FileInputStream fileStream = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int num;

        while((num = fileStream.read(buffer))!=-1){
            outData.write(buffer, 0, num);
        }

        outData.close();
        fileStream.close();
    }


    public Integer getPort() throws IOException {

        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Port: 9999");
        return 9999;
        //return Integer.parseInt(reader.readLine());
    }

}
