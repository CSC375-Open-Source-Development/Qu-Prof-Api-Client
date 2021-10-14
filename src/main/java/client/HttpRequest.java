package client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequest {

    public static Response get(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Accept", "*/*");

            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            String response = extractResponseOutput(conn);
            int statusCode = conn.getResponseCode();
            if (!isStatusCodeSuccessful(statusCode)) {
                String errorMessage = extractErrorOutput(conn);
                throw new RequestFailedException("GET Request to " + endpoint + " failed: " + errorMessage + ", status code: " + statusCode, statusCode);
            }
            return new Response(convertResponseToJsonObject(response), statusCode);
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String extractResponseOutput(HttpURLConnection conn) {
        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            return result.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static String extractErrorOutput(HttpURLConnection conn) {
        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            catch(NullPointerException e) {
            	return "Unable to extract error output";
            }
            return result.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static JsonElement convertResponseToJsonObject(String response) {
        try {
            return JsonParser.parseString(response).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            try {
                return JsonParser.parseString(response).getAsJsonArray();
            } catch (JsonParseException | IllegalStateException e2) {
                return new JsonObject();
            }
        }
    }

    private static boolean isStatusCodeSuccessful(int statusCode) {
        return statusCode / 100 == 2;
    }
}
