package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

public class WebClientHandler implements Runnable{

    private Socket clientSocket;

    public WebClientHandler(Socket clientSocket){
        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {

        try {

            System.out.println(Thread.currentThread().getName());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            String[] requestMessage = in.readLine().split(" ");
            String requestVerb = requestMessage[0];
            String requestResource = requestMessage[1];
            String filePath = "resources";

            if(requestVerb.equals("GET")) {

                if (requestResource.equals("/")) {
                    requestResource = "/Home.html";
                }

                String fileName = filePath + requestResource;

                File file = new File(fileName);

                if (file.exists()) {
                    sendHeader(fileName, out);
                    sendFile(file, out);
                } else {
                    fileNotFound(out);
                }

                System.out.println("Closing client socket");
                clientSocket.close();
                in.close();
                out.close();

            }

        } catch (IOException e) {
            System.out.println("Error - " + e.getMessage());
        }

    }

    public void fileNotFound(DataOutputStream out) throws IOException {

        out.writeBytes("HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: <file_byte_size> \r\n" + "\r\n");

    }

    public void sendHeader (String resource, DataOutputStream out) throws IOException {

        if(resource.endsWith(".html")){
            System.out.println("FILE HTML");
            out.writeBytes("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: <file_byte_size> \r\n" + "\r\n");
        } else {
            System.out.println("FILE IMAGE");
            out.writeBytes("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: image;<image_file_extension>\r\n" +
                    "Content-Length: <file_byte_size> \r\n" + "\r\n");
        }

    }

    public void sendFile (File file, DataOutputStream outData) throws IOException {

        FileInputStream fileStream = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int num;

        while((num = fileStream.read(buffer))!=-1){
            outData.write(buffer, 0, num);
        }

        outData.close();
        fileStream.close();
    }
}
