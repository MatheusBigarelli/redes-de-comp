package webserver;

import java.io.*;
import java.net.Socket;
import java.security.cert.CRL;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

final class HTTPRequest implements Runnable {
    final static String CRLF = "\r\n";
    Socket clientSocket;
    InputStream clientInputStream;
    BufferedReader bufferedInput;
    DataOutputStream clientOutputStream;
    String requestData;

    public HTTPRequest(Socket clientSocket) throws Exception {
        this.clientSocket = clientSocket;
    }

    private void sendBytes(FileInputStream file, DataOutputStream clientOutputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ( (bytes = file.read(buffer)) != -1 ) {
            clientOutputStream.write(buffer, 0, bytes);
        }
    }

    private String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if(fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }

    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
           System.out.println(e);
        }
    }

    public void processRequest() throws Exception {
        clientInputStream = clientSocket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(clientInputStream);
        bufferedInput = new BufferedReader(inputStreamReader);

        clientOutputStream = new DataOutputStream((OutputStream) clientSocket.getOutputStream());

        String requestLine = bufferedInput.readLine();
        System.out.println();
        System.out.println(requestLine);

        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();
        String fileName = tokens.nextToken();

        String temp = "";
        if (fileName.compareTo("/") == 0) {
            temp = "index.html";
        } else {
            temp = fileName.replace("/", "");
        }

        fileName = "..\\html\\" + temp;


        String headerLine = null;
        while ((headerLine = bufferedInput.readLine()).length() != 0) {
            System.out.println(headerLine);
        }


        FileInputStream file = null;
        boolean fileExists = true;
        try {
            file = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }


        // Building response message
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: text/html" + CRLF;
            entityBody = "<hmtl>" +
                    "<head>" +
                    "<title>Not Found</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1>Page not found</h1>" +
                    "</body>" +
                    "</html>";
        }

        System.out.println("\n\n\n\n\n");
        System.out.println(statusLine);
        System.out.println(contentTypeLine);
        System.out.println(fileName);
        System.out.println(CRLF);

        // Sending headers
        clientOutputStream.writeBytes(statusLine);
        clientOutputStream.writeBytes(contentTypeLine);
        clientOutputStream.writeBytes(CRLF);

        // Sending entity
        if (!fileExists) {
            file = new FileInputStream("..\\html\\notfound.html");
            clientOutputStream.writeBytes(entityBody);

        }
        sendBytes(file, clientOutputStream);

        clientInputStream.close();
        bufferedInput.close();
        clientOutputStream.close();
        clientSocket.close();
    }
}
