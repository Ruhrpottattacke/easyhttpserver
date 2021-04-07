package easyhttpserver.raw;

import com.sun.net.httpserver.HttpExchange;
import easyhttpserver.HttpHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class RawHandler implements com.sun.net.httpserver.HttpHandler {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final HttpHandler handler;

    public RawHandler(HttpHandler handler) {
        logger.finer("created instance of RawHandler class");
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("received request "+exchange.getRequestURI().toString()+" from "+exchange.getRemoteAddress().getHostName());
        if(!exchange.getRequestMethod().equals("GET")) {
            logger.finer("method of request is not get");
            exchange.sendResponseHeaders(405,-1);
            logger.finest("sent response header with response code 405 and response length -1");
            exchange.close();
            logger.warning("closed request from"+exchange.getRemoteAddress().getHostName()+" because of wrong method - method was: "+exchange.getRequestMethod());
            return;
        }
        logger.finer("correct request method, creating HttpExchange");
        easyhttpserver.HttpExchange easyExchange = new easyhttpserver.HttpExchange(exchange);
        logger.finest("handling exchange");
        handler.handle(easyExchange);
        logger.finest("handled exchange");
        if(!easyExchange.getResponse().isEmpty()) {
            logger.finest("response is not empty");
            exchange.sendResponseHeaders(200, easyExchange.getResponse().length());
            logger.finer("sent response header with response code 200 and response length "+easyExchange.getResponse().length());
            exchange.getResponseBody().write(easyExchange.getResponse().getBytes());
            logger.finest("written bytes ");
            exchange.close();
            logger.info("sent response "+easyExchange.getResponse()+" to request from "+exchange.getRemoteAddress().getHostName());
        }else {
            logger.warning("response is empty");
            exchange.sendResponseHeaders(500, -1);
            logger.finer("sent response header with response code 500 and length -1");
            exchange.close();
            logger.fine("closed request from "+exchange.getRemoteAddress().getHostName()+" with code 500 because of empty response");
        }
    }
}
