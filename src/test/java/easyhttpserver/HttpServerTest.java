package easyhttpserver;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpServerTest {
    @Test
    public void testSimpleRestEndpoint() {
        HttpServer server = new HttpServer(5555, 2, 0);
        server.createContext("/test", exchange -> {
            exchange.setResponse("Test: " + exchange.getParameters().get("ping")
                    + ", " + exchange.getParameters().get("pong"));
        });
        server.start();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("ping", "hallo");
        map.put("pong", "test");
        String response = sendRequest("/test", map);
        Assert.assertEquals("Test: hallo, test", response);
        map.remove("pong");
        response = sendRequest("/test", map);
        Assert.assertEquals("Test: hallo, null", response);
    }

    private static String sendRequest(String path, Map<String, String> parameters) {
        String response = "";
        StringBuilder query = new StringBuilder();
        if (!parameters.isEmpty()) {
            for (String parameter : parameters.keySet()) {
                query.append(parameter).append("=").append(parameters.get(parameter)).append("&");
            }
            query.deleteCharAt(query.length() - 1);
        }
        try {
            URL url = new URL("http://localhost:5555/test?" + query.toString());
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            byte[] res = connection.getInputStream().readAllBytes();
            response = new String(res);
            connection.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
