package net.jacktools.barcode.barcodegenerator.web;

import com.sun.net.httpserver.HttpServer;
import net.jacktools.barcode.barcodegenerator.web.routes.JunitHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    HttpServer httpServerserver;

    public Server(int port, int backlog) throws IOException {
        this.httpServerserver = HttpServer.create(new InetSocketAddress(port), backlog);
        this.httpServerserver.createContext("/junit", new JunitHandler());
        this.httpServerserver.setExecutor(null); // creates a default executor
        this.httpServerserver.start();
    }

    public void shutDown() {
        this.httpServerserver.stop(0);
    }
}
