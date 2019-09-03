package webserver;

import java.lang.ref.WeakReference;

public class Main {

    public static void main(String[] args) {
        String host = "";
        int port = 3333;


        WebServer webServer = new WebServer(host, port);

        System.out.println("Finish");
    }
}
