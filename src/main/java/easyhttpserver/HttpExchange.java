package easyhttpserver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpExchange {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String response;
    private final Map<String, String> parameters;

    public HttpExchange(com.sun.net.httpserver.HttpExchange exchange) {
        logger.finer("created instance of HttpExchange class with exchange "+exchange.toString());
        parameters = getParameters(exchange.getRequestURI());
        logger.finest("parameters = "+parameters.toString());
    }

    private static String[] decodeParameter(String s) {
        String[] array = new String[2];
        array[0] = s.split("=")[0];
        array[1] = s.split("=")[1];
        return array;
    }
    
    private static Map<String, String> getParameters(URI uri) {
        Map<String, String> map = new HashMap<>();
        if(!uri.getQuery().equalsIgnoreCase("null")) {
            if(uri.getQuery().contains("&")) {
                for(String param : uri.getQuery().split("&")) {
                    String[] array = decodeParameter(param);
                    map.put(array[0], array[1]);
                }
            }else {
                String[] array = decodeParameter(uri.getQuery());
                map.put(array[0], array[1]);
            }
        }
        return map;
    }

    public Map<String, String> getParameters() {
        logger.finest("method getParameters() called in HttpExchange");
        logger.finest("parameters = "+parameters.toString());
        return parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        logger.finest("method setResponse() called in HttpExchange");
        this.response = response;
        logger.fine("set response to "+response);
    }
}
