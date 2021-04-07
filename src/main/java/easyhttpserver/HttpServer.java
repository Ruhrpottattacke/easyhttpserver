package easyhttpserver;

import easyhttpserver.raw.RawHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class HttpServer {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private com.sun.net.httpserver.HttpServer server;
    private boolean running;

    public HttpServer(int port, int threads, int backlog) {
        logger.fine("created instance of HttpServer class");
        logger.finest("port = "+port+", threads = "+threads+", backlog = "+backlog);
        try {
            logger.info("starting http server on port "+port+"");
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), backlog);
            logger.fine("started server, creating new thread pool with "+threads+" threads");
            server.setExecutor(Executors.newWorkStealingPool(threads));
            logger.fine("created thread pool");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("started http server on port "+port+" with backlog "+backlog+" and "+threads+" working threads");
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
