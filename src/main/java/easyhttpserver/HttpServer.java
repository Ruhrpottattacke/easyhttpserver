package easyhttpserver;

import easyhttpserver.raw.RawHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServer {
    private com.sun.net.httpserver.HttpServer server;
    private boolean running;

    public HttpServer(int port, int threads, int backlog) {
        try {
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), backlog);
            server.setExecutor(Executors.newWorkStealingPool(threads));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.start();
        running = true;
    }

    public void stop(int delay) {
        if(isRunning()) {
            server.stop(delay);
            running = false;
        }
    }

    public void stop() {
        stop(0);
    }

    public void createContext(String path, HttpHandler handler) {
        server.createContext(path, new RawHandler(handler));
    }

    public void removeContext(String path) {
        server.removeContext(path);
    }

    public boolean isRunning() {
        return running;
    }
}
