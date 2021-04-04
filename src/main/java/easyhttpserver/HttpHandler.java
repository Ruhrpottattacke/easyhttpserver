package easyhttpserver;

public interface HttpHandler {
    void handle(HttpExchange exchange);
}
