package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    String host;
    int port;
    final static int defaultPorts[] = {3333, 4444, 5555, 6666};

    ServerSocket listenSocket;

    public WebServer(String host, int port) {
        createSocket(port);

        while (true) {
            Socket clientSocket = acceptClient();
            HTTPRequest clientRequest = null;
            try {
                clientRequest = new HTTPRequest(clientSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread clientThread = new Thread(clientRequest);
            clientThread.start();
        }
    }

    private Socket acceptClient() {
        try {
            Socket clientSocket = listenSocket.accept();
            return clientSocket;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    private void createSocket(int port) {
        try {
            if (port < 1024) {
                System.out.println("Port too low");
                port = defaultPorts[0];
            }
            this.listenSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
