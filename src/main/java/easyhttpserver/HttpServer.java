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
            logger.info("created http server on port "+port+"");
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), backlog);
            logger.fine("created server, creating new thread pool with "+threads+" threads");
            server.setExecutor(Executors.newWorkStealingPool(threads));
            logger.fine("created thread pool");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("created http server on port "+port+" with backlog "+backlog+" and "+threads+" working threads");
        }
    }

    public void start() {
        logger.info("starting http server on port "+server.getAddress().getPort());
        server.start();
        logger.info("started http server");
        running = true;
        logger.finer("running = "+true);
    }

    public void stop(int delay) {
        logger.info("stopping server with delay "+delay);
        if(isRunning()) {
            logger.finest("server is running");
            server.stop(delay);
            logger.finest("called stop method with delay "+delay);
            running = false;
            logger.finest("running = "+false);
        }else {
            logger.warning("server is not running");
        }
    }

    public void stop() {
        logger.finest("called stop method without delay");
        stop(0);
    }

    public void createContext(String path, HttpHandler handler) {
        logger.fine("creating context with path = "+path+" and handler = "+handler.getClass().getName());
        server.createContext(path, new RawHandler(handler));
        logger.info("created context with path = "+path+" and handler = "+handler.getClass().getName());
    }

    public void removeContext(String path) {
        logger.fine("removing context on path = "+path);
        server.removeContext(path);
        logger.info("removed context on path = "+path);
    }

    public boolean isRunning() {
        logger.finest("method isRunning() called in HttpServer");
        logger.finest("server on port "+server.getAddress().getPort()+" is running = "+running);
        return running;
    }
}
