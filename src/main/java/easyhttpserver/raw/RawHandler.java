package easyhttpserver.raw;

import com.sun.net.httpserver.HttpExchange;
import easyhttpserver.HttpHandler;

import java.io.IOException;

public class RawHandler implements com.sun.net.httpserver.HttpHandler {
    private final HttpHandler handler;

    public RawHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equals("GET")) {
            exchange.sendResponseHeaders(405,-1);
            exchange.close();
            return;
        }
        easyhttpserver.HttpExchange easyExchange = new easyhttpserver.HttpExchange(exchange);
        handler.handle(easyExchange);
        if(!easyExchange.getResponse().isEmpty()) {
            exchange.sendResponseHeaders(200, easyExchange.getResponse().length());
            exchange.getResponseBody().write(easyExchange.getResponse().getBytes());
            exchange.close();
        }
    }
}
